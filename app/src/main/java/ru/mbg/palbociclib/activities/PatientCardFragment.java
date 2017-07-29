package ru.mbg.palbociclib.activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import ru.mbg.palbociclib.PatientModel;
import ru.mbg.palbociclib.R;
import ru.mbg.palbociclib.helpers.AvatarHelper;
import ru.mbg.palbociclib.helpers.DateHelper;
import ru.mbg.palbociclib.models.Appointment;
import ru.mbg.palbociclib.models.BackgroundTherapy;
import ru.mbg.palbociclib.models.BackgroundTherapyType;
import ru.mbg.palbociclib.models.Menopause;
import ru.mbg.palbociclib.models.Oak;
import ru.mbg.palbociclib.models.Patient;
import ru.mbg.palbociclib.models.Treatment;
import ru.mbg.palbociclib.models.TreatmentDose;


public class PatientCardFragment extends Fragment {
    private static final String PATIENT_ID = "patient_id";

    private Patient patient;
    private PatientCardAdapter adapter;

    public PatientCardFragment() {
    }

    public static PatientCardFragment newInstance(String patientID) {
        PatientCardFragment fragment = new PatientCardFragment();
        Bundle args = new Bundle();
        args.putString(PATIENT_ID, patientID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String patientID = getArguments().getString(PATIENT_ID);
            patient = PatientModel.getPatientWithID(patientID, null);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.patient_card_title));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patient_card, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        ArrayList<CardType> data = new ArrayList<>();
        data.add(CardType.header);

        Appointment appointment = patient.getAppointments().last();
        final RealmList<Treatment> treatments = patient.getTreatments();
        if (appointment != null && DateHelper.instance.isToday(appointment.getDate()) && !appointment.getState().isTerminal()) {
            if (treatments.size() == 1) {
                data.add(CardType.startTreatmentButton);
            } else {
                data.add(CardType.appointmentButton);
            }
        }
        if (treatments.size() > 1) {
            for(int i = 1; i < treatments.size(); i += 1) {
                data.add(CardType.cycle("Цикл " + treatments.get(i).getCycleNumber()));
                data.add(CardType.grade("Начало приема", treatments.get(i)));
            }
        }
        BackgroundTherapy therapy = patient.getBackgroundTherapy();
        if (therapy != null) {
            BackgroundTherapyType type = therapy.getType();
            data.add(CardType.card("Рекомендована фоновая терапия", type.description()));
        }

        adapter = new PatientCardAdapter(data);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, 0, 0, "Правка").setIcon(R.drawable.edit).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AddPatientFragment addPatient = AddPatientFragment.newInstance(patient.getId());
        this.getFragmentManager().beginTransaction()
                .replace(R.id.content, addPatient, null)
                .addToBackStack(null)
                .commit();
        return true;
    }

    void itemClicked(CardType item) {
        if (item == CardType.appointmentButton || item == CardType.startTreatmentButton) {
            AppointmentFragment appointment = AppointmentFragment.newInstance(patient.getId());
            this.getFragmentManager().beginTransaction()
                    .replace(R.id.content, appointment, null)
                    .addToBackStack(null)
                    .commit();
        }
    }

    class PatientCardAdapter extends RecyclerView.Adapter<PatientCardAdapter.ItemViewHolder> {

        class ItemViewHolder extends RecyclerView.ViewHolder {
            // Заголовок
            TextView name;
            TextView menopause;
            TextView backgroundTherapy;
            TextView drugStartDate;
            TextView drugStartLabel;
            ImageView patientPhoto;
            // Начало цикла
            TextView title;
            // Кнопка
            Button button;
            // Описание цикла
            TextView date;
            TextView drugs; // На самом деле — результаты ОАК
            ImageView drugDoseImage;
            TextView drugDose;
            ImageView nextOakImage;
            TextView nextOak;

            ItemViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.name);
                menopause = (TextView) itemView.findViewById(R.id.menopause);
                backgroundTherapy = (TextView) itemView.findViewById(R.id.background_therapy);
                drugStartDate = (TextView) itemView.findViewById(R.id.drug_start_date);
                drugStartLabel = (TextView) itemView.findViewById(R.id.drug_start_label);
                patientPhoto = (ImageView) itemView.findViewById(R.id.patient_photo);
                title = (TextView) itemView.findViewById(R.id.title);
                button = (Button) itemView.findViewById(R.id.button);
                date = (TextView) itemView.findViewById(R.id.date);
                drugs = (TextView) itemView.findViewById(R.id.drugs);
                drugDoseImage = (ImageView) itemView.findViewById(R.id.drug_dose_image);
                drugDose = (TextView) itemView.findViewById(R.id.drug_dose);
                nextOakImage = (ImageView) itemView.findViewById(R.id.oak_image);
                nextOak = (TextView) itemView.findViewById(R.id.oak);
            }
        }

        private List<CardType> data;

        PatientCardAdapter(List<CardType> items) {
            super();
            data = items;
        }

        public void setData(List<CardType> items) {
            data = items;
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            return data.get(position).rawValue;
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v;
            CardType type = CardType.fromRawValue(viewType);
            switch (type) {
                case header:
                    v = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_card_header, parent, false);
                    break;
                case cycle:
                    v = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_card_cycle, parent, false);
                    break;
                case appointmentButton:
                case startTreatmentButton:
                    v = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_card_button, parent, false);
                    break;
                case grade:
                    v = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_card_grade, parent, false);
                    break;
                case card:
                    v = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_card_card, parent, false);
                    break;
                default:
                    v = new View(getActivity());
                    break;
            }
            final ItemViewHolder viewHolder = new ItemViewHolder(v);
            if (type == CardType.appointmentButton || type == CardType.startTreatmentButton) {
                viewHolder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int adapterPosition = viewHolder.getAdapterPosition();
                        if (adapterPosition != RecyclerView.NO_POSITION) {
                            itemClicked(data.get(adapterPosition));
                        }
                    }
                });
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            CardType item = data.get(position);
            switch (item) {
                case header:
                    holder.name.setText(patient.getName());
                    holder.patientPhoto.setImageDrawable(AvatarHelper.getAvatarForPatient(patient, getContext()));
                    Menopause menopause = patient.getMenopause();
                    holder.menopause.setText(menopause.description());
                    if (patient.isWasHormonalTherapy()) {
                        holder.backgroundTherapy.setText("Была гормональная терапия");
                    } else {
                        holder.backgroundTherapy.setText("Гормональной терапии не было");
                    }
                    if (patient.getTreatments().size() > 1) {
                        Treatment treatment = patient.getTreatments().get(1);
                        DateFormat format = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
                        holder.drugStartDate.setText(format.format(treatment.getStartDate()));
                        holder.drugStartLabel.setVisibility(View.VISIBLE);
                        holder.drugStartDate.setVisibility(View.VISIBLE);
                    } else {
                        holder.drugStartLabel.setVisibility(View.INVISIBLE);
                        holder.drugStartDate.setVisibility(View.INVISIBLE);
                    }
                    break;
                case cycle:
                    holder.title.setText(item.getTitle());
                    break;
                case appointmentButton:
                    holder.button.setText("Мониторинг приема");
                    break;
                case startTreatmentButton:
                    holder.button.setText("Назначить лечение");
                    break;
                case grade:
                    holder.title.setText(item.getTitle());
                    DateFormat format = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
                    holder.date.setText("на " + format.format(item.getTreatment().getStartDate()));
                    TreatmentDose dose = item.getTreatment().getDose();
                    holder.drugDose.setText(dose.description());
                    holder.drugDoseImage.setImageResource(dose.getImageResource());
                    Oak oak = item.getTreatment().getOaks().where().isNotNull("readyDate").findFirst();
                    if (oak != null && oak.getReadyDate() != null) {
                        holder.drugs.setText(oak.description());
                    } else {
                        holder.drugs.setText(null);
                    }
                    Oak nextOak = item.getTreatment().getOaks().where().isNull("readyDate").findAll().last();
                    if (nextOak != null) {
                        int daysToOak = DateHelper.instance.daysTo(nextOak.getAssignmentDate());
                        holder.nextOak.setText("ОАК через " + daysToOak + " дней");
                        holder.nextOak.setVisibility(View.VISIBLE);
                        holder.nextOakImage.setVisibility(View.VISIBLE);
                    } else {
                        holder.nextOak.setVisibility(View.GONE);
                        holder.nextOakImage.setVisibility(View.GONE);
                    }
                    break;
                case card:
                    holder.title.setText(item.getTitle());
                    holder.drugs.setText(item.getText());
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    private enum CardType {
        header(0), cycle(1), appointmentButton(2), startTreatmentButton(3), grade(4), card(5);

        public final int rawValue;

        private String title;
        private String text;
        private Treatment treatment;

        CardType(int rawValue) {
            this.rawValue = rawValue;
        }

        public static CardType fromRawValue(int value) {
            switch (value) {
                case 0:
                    return header;
                case 1:
                    return cycle;
                case 2:
                    return appointmentButton;
                case 3:
                    return startTreatmentButton;
                case 4:
                    return grade;
                case 5:
                    return card;
                default:
                    return null;
            }
        }

        public static CardType cycle(String title) {
            CardType o = cycle;
            o.setTitle(title);
            return o;
        }

        public static CardType grade(String title, Treatment treatment) {
            CardType o = grade;
            o.setTitle(title);
            o.setTreatment(treatment);
            return o;
        }

        public static CardType card(String title, String text) {
            CardType o = card;
            o.setTitle(title);
            o.setText(text);
            return o;
        }

        public String getTitle() {
            return title;
        }

        private void setTitle(String title) {
            this.title = title;
        }

        public String getText() {
            return text;
        }

        private void setText(String text) {
            this.text = text;
        }

        public Treatment getTreatment() {
            return treatment;
        }

        private void setTreatment(Treatment treatment) {
            this.treatment = treatment;
        }
    }
}