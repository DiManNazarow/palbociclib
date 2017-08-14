package ru.mbg.palbociclib;

import java.util.Date;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;
import ru.mbg.palbociclib.helpers.DateHelper;
import ru.mbg.palbociclib.models.Appointment;
import ru.mbg.palbociclib.models.AppointmentState;
import ru.mbg.palbociclib.models.BackgroundTherapy;
import ru.mbg.palbociclib.models.BackgroundTherapyType;
import ru.mbg.palbociclib.models.Menopause;
import ru.mbg.palbociclib.models.Oak;
import ru.mbg.palbociclib.models.Patient;
import ru.mbg.palbociclib.models.Treatment;
import ru.mbg.palbociclib.models.TreatmentDose;

public class PatientModel {
    private final Patient patient;
    private final Realm realm;

    private final DateHelper dateHelper;

    private final Settings settings;

    public static void updatePatient(final Patient patient, final String name, Realm realm) {
        if (realm == null) {
            realm = Realm.getDefaultInstance();
        }
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                patient.setName(name);
            }
        });
    }

    public static Patient getPatientWithID(String patientID, Realm realm) {
        if (realm == null) {
            realm = Realm.getDefaultInstance();
        }
        return realm.where(Patient.class).equalTo("id", patientID).findFirst();
    }

    public static void deletePatient(final String patientID, Realm realm){
        if (realm == null) {
            realm = Realm.getDefaultInstance();
        }
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Patient> patients = realm.where(Patient.class).equalTo("id", patientID).findAll();
                for (Patient patient : patients){
                    patient.getAppointments().deleteAllFromRealm();
                    patient.getTreatments().deleteAllFromRealm();
                }
                patients.deleteAllFromRealm();
            }
        });
    }

    /// Первичный прием, генерация рекомендаций для фоновой терапии
    public PatientModel(String name, Menopause menopause, boolean wasHormonalTherapy, Settings settings) throws AppError {
        this(name, menopause, wasHormonalTherapy, null, null, settings, null, null);
    }

    public PatientModel(String name, Menopause menopause, boolean wasHormonalTherapy, Settings settings, Realm realm) throws AppError {
        this(name, menopause, wasHormonalTherapy, null, null, settings, realm, null);
    }

    public PatientModel(String name, Menopause menopause, boolean wasHormonalTherapy, Settings settings, Realm realm, DateHelper date) throws AppError {
        this(name, menopause, wasHormonalTherapy, null, null, settings, realm, date);
    }


    public PatientModel(PatientModelArgument argument) throws AppError {
        this(argument.mName, argument.mMenopause, argument.wasHormonalTherapy, argument.mAppointment, argument.mBackgroundTherapy, argument.mSettings, argument.mRealm, null);
    }

    public static class PatientModelArgument {
        public String mName;
        public Menopause mMenopause;
        public boolean wasHormonalTherapy;
        public Appointment mAppointment;
        public BackgroundTherapy mBackgroundTherapy;
        public Settings mSettings;
        public Realm mRealm;
    }

    public PatientModel(String name, Menopause menopause, boolean wasHormonalTherapy, Appointment appointment, BackgroundTherapy backgroundTherapy, Settings settings, Realm realm, DateHelper date) throws AppError {
        this.realm = realm == null ? Realm.getDefaultInstance() : realm;
        this.dateHelper = date == null ? DateHelper.instance : date;
        this.settings = settings;

        this.realm.beginTransaction();
        try {
            patient = this.realm.createObject(Patient.class, UUID.randomUUID().toString());
            patient.setName(name);
            patient.setMenopause(menopause);
            patient.setWasHormonalTherapy(wasHormonalTherapy);
            if (appointment != null) {
                patient.getAppointments().add(appointment);
            }
            if (backgroundTherapy != null) {
                patient.setBackgroundTherapy(backgroundTherapy);
            }

            Treatment firstTreatment = this.realm.createObject(Treatment.class);
            firstTreatment.setCycleNumber(0);
            firstTreatment.setStartDate(dateHelper.currentDate());
            patient.getTreatments().add(firstTreatment);

            assignTherapy();

            assignOAKToBeReadyTo(dateHelper.currentDate());
            patient.getTreatments().last().getOaks().last().setAssignmentDate(dateHelper.currentDate());
            assignAppointmentAt(dateHelper.currentDate());

            this.realm.commitTransaction();
        } catch (Throwable e) {
            if(this.realm.isInTransaction()) {
                this.realm.cancelTransaction();
            }
            throw e;
        }
    }

    public PatientModel(String patientID, Settings settings) {
        this(patientID, settings, null, null);
    }

    public PatientModel(String patientID,  Settings settings, Realm realm, DateHelper date) {
        this.realm = realm == null ? Realm.getDefaultInstance() : realm;
        this.dateHelper = date == null ? DateHelper.instance : date;
        this.settings = settings;
        this.patient = this.realm.where(Patient.class).equalTo("id", patientID).findFirst();
    }

    private void assignTherapy() throws AppError {
        if (patient.getMenopause() == Menopause.postmenopause && !patient.isWasHormonalTherapy()) {
            BackgroundTherapy therapy = realm.createObject(BackgroundTherapy.class);
            therapy.setDate(dateHelper.currentDate());
            therapy.setType(BackgroundTherapyType.letrozole);
            patient.setBackgroundTherapy(therapy);
            return;
        }

        if (patient.isWasHormonalTherapy() || patient.getMenopause() == Menopause.perimenopause || patient.getMenopause() == Menopause.postmenopause) {
            BackgroundTherapy therapy = realm.createObject(BackgroundTherapy.class);
            therapy.setDate(dateHelper.currentDate());
            therapy.setType(BackgroundTherapyType.fulvestrant);
            patient.setBackgroundTherapy(therapy);
            return;
        }

        throw new AppError(AppError.notSupportedPatient);
    }

    private void assignOAKToBeReadyTo(Date date) {
        Treatment currentTreatment = patient.getTreatments().last();
        if (currentTreatment == null) {
            return;
        }
        Oak oak = new Oak();
        oak.setAssignmentDate(DateHelper.advancingDays(date, -settings.getOakReadyDays()));

        currentTreatment.getOaks().add(oak);
    }

    private void assignAppointmentAt(Date date) {
        Appointment appointment = new Appointment();
        appointment.setDate(date);
        patient.getAppointments().add(appointment);
    }

    public void saveOAK(final int leukocytes, final double neutrophilis, final int platelets, final boolean fever) throws AppError {
        Treatment currentTreatment = patient.getTreatments().last();
        if (currentTreatment == null) {
            throw new AppError(AppError.notSupportedPatient);
        }
        final Oak oak = currentTreatment.getOaks().last();
        if (oak == null) {
            throw new AppError(AppError.oakNotAssigned);
        }
        if (oak.getReadyDate() != null) {
            throw new AppError(AppError.oakNotAssigned);
        }

        final int grade;
        double ln = leukocytes * neutrophilis;
        if (ln < 500) {
            grade = 4;
        } else if (ln <= 1000) {
            grade = 3;
        } else if (ln < 1500) {
            grade = 2;
        } else {
            grade = 1;
        }

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                oak.setReadyDate(dateHelper.currentDate());
                oak.setLeukocytes(leukocytes);
                oak.setNeutrophils(neutrophilis);
                oak.setPlatelets(platelets);
                oak.setGrade(grade);
                oak.setFever(fever);
            }
        });
    }

    public AppointmentState appointment() throws AppError {
        Treatment currentTreatment = patient.getTreatments().last();
        if (currentTreatment == null) {
            throw new AppError(AppError.notSupportedPatient);
        }
        final Oak oak = currentTreatment.getOaks().last();
        if (oak == null) {
            throw new AppError(AppError.oakNotAssigned);
        }
        if (oak.getReadyDate() == null) {
            return AppointmentState.oakNeeded;
        }
        Appointment currentAppointment = patient.getAppointments().last();
        if (currentAppointment == null) {
            throw new AppError(AppError.notSupportedPatient);
        }
        if (currentAppointment.getState().isTerminal()) {
            return currentAppointment.getState();
        }

        AppointmentState result;
        this.realm.beginTransaction();
        try {
            if (patient.getTreatments().size() == 1) {
                if (startTreatment(oak)) {
                    result = AppointmentState.done;
                } else {
                    result = AppointmentState.assignNextAppointment;
                }
            } else {
                int days = -dateHelper.daysTo(currentTreatment.getStartDate());
                if (days == 28) {
                    days = 0;
                    Treatment treatment = realm.createObject(Treatment.class);
                    treatment.setStartDate(dateHelper.currentDate());
                    treatment.setCycleNumber(currentTreatment.getCycleNumber() + 1);
                    treatment.setDose(currentTreatment.getDose());
                    currentTreatment.getOaks().remove(currentTreatment.getOaks().size()-1);
                    treatment.getOaks().add(oak);
                    patient.getTreatments().add(treatment);
                    currentTreatment = treatment;
                }
                switch (days) {
                    case 0:
                        result = appointmentAtStart(oak, currentTreatment);
                        break;
                    case 7:
                        result = appointment7(oak, currentTreatment);
                        break;
                    case 14:
                        result = appointment14(oak, currentTreatment);
                        break;
                    case 21:
                        result = appointment21(oak, currentTreatment);
                        break;
                    default:
                        throw new AppError(AppError.wrongAppointmentDate);
                }
            }
            currentAppointment.setState(result);
            this.realm.commitTransaction();
            return result;
        } catch (Throwable e) {
            if(this.realm.isInTransaction()) {
                this.realm.cancelTransaction();
            }
            throw e;
        }
    }

    public void assignOAKAndAppointmentAt(final Date date) throws AppError {
        final Appointment currentAppointment = patient.getAppointments().last();
        if (currentAppointment == null) {
            throw new AppError(AppError.notSupportedPatient);
        }
        if (currentAppointment.getState() != AppointmentState.assignNextAppointment) {
            throw new AppError(AppError.wrongAppointmentState);
        }

        Date oakDate = DateHelper.advancingDays(DateHelper.strippingTime(date), -settings.getOakReadyDays());
        if (oakDate.getTime() <= dateHelper.currentDate().getTime()) {
            throw new AppError(AppError.wrongAppointmentDate);
        }

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                currentAppointment.setState(AppointmentState.done);

                assignOAKToBeReadyTo(date);
                assignAppointmentAt(date);
            }
        });
    }

    public void assignContinueTreatmentAt(Date date) throws AppError {
        final Appointment currentAppointment = patient.getAppointments().last();
        if (currentAppointment == null) {
            throw new AppError(AppError.notSupportedPatient);
        }
        if (currentAppointment.getState() != AppointmentState.assignContinueTreatment) {
            throw new AppError(AppError.wrongAppointmentState);
        }

        final Treatment currentTreatment = patient.getTreatments().last();
        if (currentTreatment == null) {
            throw new AppError(AppError.notSupportedPatient);
        }
        if (currentTreatment.getPatients() == null || currentTreatment.getContinueDate() != null) {
            throw new AppError(AppError.notSupportedPatient);
        }
        final Date sDate = DateHelper.strippingTime(date);
        if (sDate.getTime() < dateHelper.currentDate().getTime()) {
            throw new AppError(AppError.wrongAppointmentDate);
        }

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                currentTreatment.setContinueDate(sDate);
                currentAppointment.setState(AppointmentState.done);
            }
        });
    }

    public void selectLowerDose(final TreatmentDose dose) throws AppError {
        final Appointment currentAppointment = patient.getAppointments().last();
        if (currentAppointment == null) {
            throw new AppError(AppError.notSupportedPatient);
        }
        if (currentAppointment.getState() != AppointmentState.maybeLowerDose) {
            throw new AppError(AppError.wrongAppointmentState);
        }

        final Treatment currentTreatment = patient.getTreatments().last();
        if (currentTreatment == null) {
            throw new AppError(AppError.notSupportedPatient);
        }
        TreatmentDose currentDose = currentTreatment.getDose();
        if ((currentDose == TreatmentDose.dose100 && dose == TreatmentDose.dose125) ||
                (currentDose == TreatmentDose.dose75 && dose == TreatmentDose.dose100) ||
                (currentDose == TreatmentDose.dose75 && dose == TreatmentDose.dose125)) {
            throw new AppError(AppError.selectedLargerDose);
        }

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                currentTreatment.setDose(dose);
                currentAppointment.setState(AppointmentState.done);

                Date nextCycleDate = DateHelper.advancingDays(currentTreatment.getStartDate(), 29);
                assignOAKToBeReadyTo(nextCycleDate);
                assignAppointmentAt(nextCycleDate);
                Date thirdWeek = DateHelper.advancingDays(currentTreatment.getStartDate(), 22);
                currentTreatment.setPauseDate(thirdWeek);
                currentTreatment.setContinueDate(nextCycleDate);
            }
        });
    }

    private boolean startTreatment(Oak oak) {
        if (oak.getGrade() < 3 && oak.getPlatelets() > 50_000) {
            Treatment treatment = realm.createObject(Treatment.class);
            treatment.setCycleNumber(1);
            treatment.setStartDate(dateHelper.currentDate());
            treatment.setDose(TreatmentDose.dose125);
            treatment.getOaks().add(oak);
            patient.getTreatments().add(treatment);
            Date date14 = DateHelper.advancingDays(dateHelper.currentDate(), 14);
            assignOAKToBeReadyTo(date14);
            assignAppointmentAt(date14);
            return true;
        } else {
            return false;
        }
    }

    private AppointmentState appointmentAtStart(Oak oak, Treatment treatment) {
        boolean isFirstCycle = patient.getTreatments().size() == 3;
        if (oak.getGrade() == 1 || oak.getGrade() == 2 || (oak.getGrade() == 3 && !oak.isFever() && isFirstCycle)) {
            Date nextDate = DateHelper.advancingDays(dateHelper.currentDate(), 15);
            assignOAKToBeReadyTo(nextDate);
            assignAppointmentAt(nextDate);
        }
        if (oak.getGrade() == 3 && !oak.isFever() && !isFirstCycle) {
            Date nextDate = DateHelper.advancingDays(dateHelper.currentDate(), 7);
            treatment.setPauseDate(dateHelper.currentDate());
            treatment.setContinueDate(nextDate);
            assignOAKToBeReadyTo(nextDate);
            assignAppointmentAt(nextDate);
        }
        if ((oak.getGrade() == 3 && oak.isFever()) || oak.getGrade() == 4) {
            return reduceDoseAndCloseCycle(treatment);
        }
        return AppointmentState.done;
    }

    private AppointmentState appointment7(Oak oak, Treatment treatment) {
        // TODO: https://github.com/realm/realm-java/issues/1598
        // int wasGrade3 = patient.getTreatments().where().greaterThanOrEqualTo("oaks.@max.grade", 3).findAll().size();
        int wasGrade3 = 0;
        for (Treatment t: patient.getTreatments()) {
            for (Oak o: t.getOaks()) {
                if (o.getGrade() >= 3) {
                    wasGrade3 += 1;
                    break;
                }
            }
        }
        if (oak.getGrade() < 3 && wasGrade3 < 2) {
            Date nextCycleDate = DateHelper.advancingDays(dateHelper.currentDate(), 22);
            assignOAKToBeReadyTo(nextCycleDate);
            assignAppointmentAt(nextCycleDate);
            return AppointmentState.done;
        } else {
            return AppointmentState.maybeLowerDose;
        }
    }

    private AppointmentState appointment14(Oak oak, Treatment treatment) {
        if (oak.getGrade() == 1 || oak.getGrade() == 2 ) {
            Date nextCycleDate = DateHelper.advancingDays(dateHelper.currentDate(), 15);
            treatment.setPauseDate(DateHelper.advancingDays(dateHelper.currentDate(), 7));
            treatment.setContinueDate(nextCycleDate);
            assignOAKToBeReadyTo(nextCycleDate);
            assignAppointmentAt(nextCycleDate);
        }
        if (oak.getGrade() == 3 && !oak.isFever()) {
            Date nextCycleDate = DateHelper.advancingDays(dateHelper.currentDate(), 7);
            assignOAKToBeReadyTo(nextCycleDate);
            assignAppointmentAt(nextCycleDate);
        }
        if ((oak.getGrade() == 3 && oak.isFever()) || oak.getGrade() == 4) {
            return reduceDoseAndCloseCycle(treatment);
        }
        return AppointmentState.done;
    }

    private AppointmentState appointment21(Oak oak, Treatment treatment) {
        if (oak.getGrade() == 1 || oak.getGrade() == 2 || (oak.getGrade() == 3 && !oak.isFever())) {
            Date nextCycleDate = DateHelper.advancingDays(dateHelper.currentDate(), 8);
            treatment.setPauseDate(dateHelper.currentDate());
            treatment.setContinueDate(nextCycleDate);
            assignOAKToBeReadyTo(nextCycleDate);
            assignAppointmentAt(nextCycleDate);
        }
        if ((oak.getGrade() == 3 && oak.isFever()) || oak.getGrade() == 4) {
            return reduceDoseAndCloseCycle(treatment);
        }
        return AppointmentState.done;
    }

    private AppointmentState reduceDoseAndCloseCycle(Treatment treatment) {
        Date nextCycleDate = DateHelper.advancingDays(treatment.getStartDate(), 29);
        assignOAKToBeReadyTo(nextCycleDate);
        assignAppointmentAt(nextCycleDate);

        TreatmentDose dose = treatment.getDose();
        switch (dose) {
            case dose125:
                treatment.setDose(TreatmentDose.dose100);
                break;
            case dose100:
                treatment.setDose(TreatmentDose.dose75);
                break;
            case dose75:
                treatment.setPauseDate(dateHelper.currentDate());
                return AppointmentState.assignContinueTreatment;
        }

        Date thirdWeek = DateHelper.advancingDays(treatment.getStartDate(), 22);
        treatment.setPauseDate(thirdWeek);
        treatment.setContinueDate(DateHelper.advancingDays(thirdWeek, 7));

        return AppointmentState.doseWasReduced;
    }

    public Patient getPatient() {
        return patient;
    }

    public AppointmentState getLastAppointmentState() {
        return patient.getAppointments().last().getState();
    }
}
