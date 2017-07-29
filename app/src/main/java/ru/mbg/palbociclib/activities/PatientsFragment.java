package ru.mbg.palbociclib.activities;


import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import ru.mbg.palbociclib.R;
import ru.mbg.palbociclib.helpers.AvatarHelper;
import ru.mbg.palbociclib.helpers.DateHelper;
import ru.mbg.palbociclib.models.Oak;
import ru.mbg.palbociclib.models.Patient;
import ru.mbg.palbociclib.models.Treatment;
import ru.mbg.palbociclib.models.TreatmentDose;


public class PatientsFragment extends Fragment {
    private PatientListAdapter adapter;
    private Realm realm;
    private RecyclerView recyclerView;

    public PatientsFragment() {
    }

    public static PatientsFragment newInstance() {
        return new PatientsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        recyclerView.setAdapter(null);
        realm.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patients, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        final View empty = view.findViewById(R.id.empty_view);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        final RealmResults<Patient> patients = realm.where(Patient.class).findAllAsync();
        adapter = new PatientListAdapter(patients);
        recyclerView.setAdapter(adapter);

        patients.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<Patient>>() {
            @Override
            public void onChange(RealmResults<Patient> collection, OrderedCollectionChangeSet changeSet) {
                if (collection.isEmpty()) {
                    empty.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    empty.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.title_patients);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.removeGroup(0);
        menu.add(0, 0, 0, "Добавить").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AddPatientFragment addPatient = AddPatientFragment.newInstance(null);
        this.getFragmentManager().beginTransaction()
                .replace(R.id.content, addPatient, null)
                .addToBackStack(null)
                .commit();
        return true;
    }

    void itemClicked(Patient patient) {
        PatientCardFragment patientCard = PatientCardFragment.newInstance(patient.getId());
        this.getFragmentManager().beginTransaction()
                .replace(R.id.content, patientCard, null)
                .addToBackStack(null)
                .commit();
    }

    class PatientListAdapter extends RealmRecyclerViewAdapter<Patient, PatientListAdapter.ItemViewHolder> {

        class ItemViewHolder extends RecyclerView.ViewHolder {
            CardView cardView;
            TextView title;
            ImageView avatarImageView;
            View treatmentView;
            View treatmentNotStartedView;
            ImageView cycleImageView;
            TextView cycleTextView;
            ImageView drugDoseImageView;
            TextView drugDoseTextView;
            ImageView oakImageView;
            TextView oakTextView;
            TextView gradeTextView;

            ItemViewHolder(View itemView) {
                super(itemView);
                cardView = (CardView) itemView.findViewById(R.id.card);
                title = (TextView) itemView.findViewById(R.id.title);
                avatarImageView = (ImageView) itemView.findViewById(R.id.avatar);
                treatmentView = itemView.findViewById(R.id.treatment);
                treatmentNotStartedView = itemView.findViewById(R.id.treatment_not_started);
                cycleImageView = (ImageView) itemView.findViewById(R.id.cycle_image);
                cycleTextView = (TextView) itemView.findViewById(R.id.cycle);
                drugDoseImageView = (ImageView) itemView.findViewById(R.id.drug_dose_image);
                drugDoseTextView = (TextView) itemView.findViewById(R.id.drug_dose);
                oakImageView = (ImageView) itemView.findViewById(R.id.oak_image);
                oakTextView = (TextView) itemView.findViewById(R.id.oak);
                gradeTextView = (TextView) itemView.findViewById(R.id.grade);
            }
        }

        PatientListAdapter(RealmResults<Patient> items) {
            super(items, true);
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_list_item, parent, false);
            final ItemViewHolder viewHolder = new ItemViewHolder(v);
            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int adapterPosition = viewHolder.getAdapterPosition();
                    if( adapterPosition != RecyclerView.NO_POSITION ) {
                        itemClicked(getItem(adapterPosition));
                    }
                }
            });
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            Patient patient = getItem(position);
            holder.title.setText(patient.getName());
            holder.avatarImageView.setImageDrawable(AvatarHelper.getAvatarForPatient(patient, getContext()));
            int cycle = patient.getTreatments().size() - 1;
            if (cycle > 0) {
                holder.treatmentView.setVisibility(View.VISIBLE);
                holder.treatmentNotStartedView.setVisibility(View.GONE);

                Treatment treatment = patient.getTreatments().last();

                holder.cycleTextView.setText(cycle + "-й");

                TreatmentDose dose = treatment.getDose();
                holder.drugDoseImageView.setImageResource(dose.getImageResource());
                holder.drugDoseTextView.setText(dose.description());

                Oak oak = treatment.getOaks().where().isNull("readyDate").findAll().last();
                if (oak != null) {
                    int daysToOak = DateHelper.instance.daysTo(oak.getAssignmentDate());
                    holder.oakImageView.setVisibility(View.VISIBLE);
                    holder.oakTextView.setVisibility(View.VISIBLE);

                    holder.oakTextView.setText("до ОАК: " + daysToOak + " дней");
                } else {
                    holder.oakImageView.setVisibility(View.GONE);
                    holder.oakTextView.setVisibility(View.GONE);
                }

                int week = DateHelper.instance.weeksFrom(treatment.getStartDate());
                holder.gradeTextView.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.treatment_color), PorterDuff.Mode.SRC_ATOP);
                Oak readyOak = treatment.getOaks().where().isNotNull("readyDate").findAll().last();
                if (readyOak != null) {
                    holder.gradeTextView.setText(week + "-я неделя (Грейд " + readyOak.getGrade() + ")");
                } else {
                    holder.gradeTextView.setText(week + "-я неделя");
                }

                switch (cycle) {
                    case 1:
                        holder.cycleImageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.cycle1_color), PorterDuff.Mode.SRC_ATOP);
                        break;
                    case 2:
                        holder.cycleImageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.cycle2_color), PorterDuff.Mode.SRC_ATOP);
                        break;
                    default:
                        holder.cycleImageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.cycle_default_color), PorterDuff.Mode.SRC_ATOP);
                        break;
                }
            } else {
                holder.treatmentView.setVisibility(View.GONE);
                holder.treatmentNotStartedView.setVisibility(View.VISIBLE);

                holder.gradeTextView.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.treatment_not_started_color), PorterDuff.Mode.SRC_ATOP);
                holder.gradeTextView.setText("До начала приема");
            }
        }
    }
}
