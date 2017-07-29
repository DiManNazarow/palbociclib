package ru.mbg.palbociclib.activities;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import ru.mbg.palbociclib.AppError;
import ru.mbg.palbociclib.Constants;
import ru.mbg.palbociclib.PatientModel;
import ru.mbg.palbociclib.R;
import ru.mbg.palbociclib.Settings;
import ru.mbg.palbociclib.UserDefaultsSettings;
import ru.mbg.palbociclib.helpers.AvatarHelper;
import ru.mbg.palbociclib.helpers.ImageHelper;
import ru.mbg.palbociclib.models.Menopause;
import ru.mbg.palbociclib.models.Patient;


public class AddPatientFragment extends Fragment {
    private static final String PATIENT_ID = "patient_id";

    private Patient patient;
    private Uri cropImageUri;

    private ImageView avatarImageView;
    private TextView menopauseTextView;
    private EditText nameEditText;
    private Switch wasHormonalTherapySwitch;
    private Bitmap avatarImage;
    private Menopause menopause = Menopause.none;

    public AddPatientFragment() {
    }

    public static AddPatientFragment newInstance(String patientID) {
        AddPatientFragment fragment = new AddPatientFragment();
        Bundle args = new Bundle();
        args.putString(PATIENT_ID, patientID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            final String patientID = getArguments().getString(PATIENT_ID);
            if (patientID != null) {
                patient = PatientModel.getPatientWithID(patientID, null);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (patient == null) {
            getActivity().setTitle(getString(R.string.add_patient_title));
        } else {
            getActivity().setTitle(getString(R.string.save_patient_title));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_patient, container, false);
        avatarImageView = (ImageView) view.findViewById(R.id.avatar);
        nameEditText = (EditText) view.findViewById(R.id.name);
        menopauseTextView = (TextView) view.findViewById(R.id.menopause);
        Button saveButton = (Button) view.findViewById(R.id.add_button);
        wasHormonalTherapySwitch = (Switch) view.findViewById(R.id.was_hormonal_therapy);

        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (avatarImage == null) {
                    avatarImageView.setImageDrawable(AvatarHelper.createAvatarForName(editable.toString(), getContext()));
                }
            }
        });
        avatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCropImageActivity(null);
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePatient();
            }
        });

        if (patient != null) {
            avatarImageView.setImageDrawable(AvatarHelper.getAvatarForPatient(patient, getContext()));
            nameEditText.setText(patient.getName());
            wasHormonalTherapySwitch.setChecked(patient.isWasHormonalTherapy());
            wasHormonalTherapySwitch.setEnabled(false);
            menopause = patient.getMenopause();
            saveButton.setText("Сохранить пациента");
        } else {
            wasHormonalTherapySwitch.setChecked(false);
            wasHormonalTherapySwitch.setEnabled(true);
            saveButton.setText("Добавить пациента");
            // Редактировать значение менопаузы можно только для новых пациентов
            view.findViewById(R.id.menopause_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final CharSequence choices[] = new CharSequence[] {
                            Menopause.none.description(),
                            Menopause.perimenopause.description(),
                            Menopause.menopause.description(),
                            Menopause.postmenopause.description()
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Менопауза");
                    builder.setItems(choices, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            menopauseTextView.setText(choices[which]);
                            menopause = Menopause.fromDescription(choices[which].toString());
                        }
                    });
                    builder.show();
                }
            });
        }
        menopauseTextView.setText(menopause.description());

        return view;
    }

    @Override
    @SuppressLint("NewApi")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(getContext(), data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(getContext(), imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                cropImageUri = imageUri;
                requestPermissions(
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE
                );
            } else {
                // no permissions required or already granted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    InputStream imageStream = getContext().getContentResolver().openInputStream(resultUri);
                    Bitmap avatar = BitmapFactory.decodeStream(imageStream);
                    avatarImage = ImageHelper.getRoundedCornerBitmap(avatar, avatar.getWidth()/2);
                    avatarImageView.setImageBitmap(avatarImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                //noinspection ThrowableResultOfMethodCallIgnored
                Exception error = result.getError();
                Toast.makeText(getContext(), "Выбрать аватар не удалось", Toast.LENGTH_LONG).show();
                Log.e(Constants.TAG, "Avatar crop error", error);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            if (cropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // required permissions granted, start crop image activity
                startCropImageActivity(cropImageUri);
            } else {
                Toast.makeText(getContext(), "Для выбора изображения нужно дать права приложению", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setCropShape(CropImageView.CropShape.OVAL)
                .setAspectRatio(1, 1)
                .start(getContext(), this);
    }

    private void savePatient() {
        try {
            String name = nameEditText.getText().toString();
            if (patient != null) {
                PatientModel.updatePatient(patient, name, null);
                if (avatarImage != null) {
                    AvatarHelper.saveAvatarForPatient(avatarImage, patient, getContext());
                } else {
                    AvatarHelper.clearCachedAvatarForPatient(patient);
                }
                // Вернуться
                this.getFragmentManager().popBackStack();
            } else {
                boolean wasHormonalTherapy = wasHormonalTherapySwitch.isChecked();
                Settings settings = new UserDefaultsSettings(getContext());
                PatientModel patientModel = new PatientModel(name, menopause, wasHormonalTherapy, settings);
                if (avatarImage != null) {
                    AvatarHelper.saveAvatarForPatient(avatarImage, patientModel.getPatient(), getContext());
                }
                // Перейти к карточке
                PatientCardFragment patientCard = PatientCardFragment.newInstance(patientModel.getPatient().getId());
                this.getFragmentManager().beginTransaction()
                        .replace(R.id.content, patientCard, null)
                        .commit();
            }
        } catch (IOException | AppError appError) {
            Log.e(Constants.TAG, "Error saving patient", appError);
            Toast.makeText(getContext(), "Ошибка сохранения пациента", Toast.LENGTH_LONG).show();
        }
    }
}
