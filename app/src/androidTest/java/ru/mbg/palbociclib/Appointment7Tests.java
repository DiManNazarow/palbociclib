package ru.mbg.palbociclib;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import ru.mbg.palbociclib.helpers.DateHelper;
import ru.mbg.palbociclib.models.AppointmentState;
import ru.mbg.palbociclib.models.Menopause;
import ru.mbg.palbociclib.models.Oak;
import ru.mbg.palbociclib.models.Treatment;
import ru.mbg.palbociclib.models.TreatmentDose;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class Appointment7Tests {

    private Realm testRealm;
    private PatientModel sut;
    private DateHelper dateHelper;

    @Before
    public void setUp() {
        Realm.init(InstrumentationRegistry.getTargetContext());

        RealmConfiguration.Builder builder = new RealmConfiguration.Builder()
                .inMemory();
        RealmConfiguration configuration = builder.build();
        testRealm = Realm.getInstance(configuration);

        dateHelper = new DateHelper();
        dateHelper.mockDate = new Date();

        try {
            sut = new PatientModel("Василиса Иванова", Menopause.perimenopause, false, new TestSettings(), testRealm, dateHelper);
            sut.saveOAK(3000, 0.4, 200_000, 0, 0, false);
            sut.appointment(Calendar.getInstance().getTime(), TreatmentDose.dose125);
            dateHelper.mockDate = DateHelper.advancingDays(dateHelper.mockDate, 14);
            sut.saveOAK(3000, 0.4, 200_000, 0, 0, false);
            sut.appointment(Calendar.getInstance().getTime(), TreatmentDose.dose125);
            dateHelper.mockDate = DateHelper.advancingDays(dateHelper.mockDate, 14);
            sut.saveOAK(2000, 0.4, 200_000, 0, 0, false);
            testRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Treatment fakeTreatment = testRealm.createObject(Treatment.class);
                    sut.getPatient().getTreatments().add(1, fakeTreatment);
                    sut.getPatient().getTreatments().get(2).setDose(TreatmentDose.dose100);
                }
            });
            sut.appointment(Calendar.getInstance().getTime(), TreatmentDose.dose125);
            dateHelper.mockDate = DateHelper.advancingDays(dateHelper.mockDate, 7);
        } catch (AppError appError) {
            appError.printStackTrace();
            fail();
        }
    }

    @After
    public void tearDown() {
        testRealm.beginTransaction();
        testRealm.deleteAll();
        testRealm.commitTransaction();
    }

    @Test
    public void appointmentOn7DayAssigned() {
        Treatment treatment = sut.getPatient().getTreatments().last();

        assertEquals(7, dateHelper.daysFrom(treatment.getStartDate(), sut.getPatient().getAppointments().last().getDate()));
    }

    @Test
    public void continueTreatmentOnGrade1WithoutGrade3() {
        try {
            sut.saveOAK(4000, 0.4, 200_000, 0, 0, false);
            assertEquals(1, sut.getPatient().getTreatments().last().getOaks().last().getGrade());

            AppointmentState result = sut.appointment(Calendar.getInstance().getTime(), TreatmentDose.dose125);
            assertEquals(AppointmentState.done, result);

            Treatment treatment = sut.getPatient().getTreatments().last();
            Oak oak = treatment.getOaks().last();
            assertNull(oak.getReadyDate());
            assertEquals(29, dateHelper.daysFrom(treatment.getStartDate(), oak.getAssignmentDate()));
            assertNotNull(sut.getPatient().getAppointments().last());
            assertEquals(29, dateHelper.daysFrom(treatment.getStartDate(), sut.getPatient().getAppointments().last().getDate()));
        } catch (AppError appError) {
            appError.printStackTrace();
            fail();
        }
    }

    @Test
    public void continueTreatmentOnGrade2WithoutGrade3() {
        try {
            sut.saveOAK(3000, 0.4, 200_000, 0, 0, false);
            assertEquals(2, sut.getPatient().getTreatments().last().getOaks().last().getGrade());

            AppointmentState result = sut.appointment(Calendar.getInstance().getTime(), TreatmentDose.dose125);
            assertEquals(AppointmentState.done, result);

            Treatment treatment = sut.getPatient().getTreatments().last();
            Oak oak = treatment.getOaks().last();
            assertNull(oak.getReadyDate());
            assertEquals(29, dateHelper.daysFrom(treatment.getStartDate(), oak.getAssignmentDate()));
            assertNotNull(sut.getPatient().getAppointments().last());
            assertEquals(29, dateHelper.daysFrom(treatment.getStartDate(), sut.getPatient().getAppointments().last().getDate()));
        } catch (AppError appError) {
            appError.printStackTrace();
            fail();
        }
    }

    @Test
    public void lowerDoseOnGrade1WithGrade3InPast() {
        testRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Oak oak = testRealm.createObject(Oak.class);
                oak.setGrade(3);
                sut.getPatient().getTreatments().get(1).getOaks().add(oak);
            }
        });
        try {
            sut.saveOAK(4000, 0.4, 200_000, 0, 0, false);
            assertEquals(1, sut.getPatient().getTreatments().last().getOaks().last().getGrade());

            AppointmentState result = sut.appointment(Calendar.getInstance().getTime(), TreatmentDose.dose125);
            assertEquals(AppointmentState.maybeLowerDose, result);
        } catch (AppError appError) {
            appError.printStackTrace();
            fail();
        }
    }

    @Test
    public void lowerDoseOnGrade2WithGrade3InPast() {
        testRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Oak oak = testRealm.createObject(Oak.class);
                oak.setGrade(3);
                sut.getPatient().getTreatments().get(1).getOaks().add(oak);
            }
        });
        try {
            sut.saveOAK(3000, 0.4, 200_000, 0, 0, false);
            assertEquals(2, sut.getPatient().getTreatments().last().getOaks().last().getGrade());

            AppointmentState result = sut.appointment(Calendar.getInstance().getTime(), TreatmentDose.dose125);
            assertEquals(AppointmentState.maybeLowerDose, result);
        } catch (AppError appError) {
            appError.printStackTrace();
            fail();
        }
    }

    @Test
    public void lowerDoseOnGrade3() {
        try {
            sut.saveOAK(2000, 0.4, 200_000, 0, 0, false);
            assertEquals(3, sut.getPatient().getTreatments().last().getOaks().last().getGrade());

            AppointmentState result = sut.appointment(Calendar.getInstance().getTime(), TreatmentDose.dose125);
            assertEquals(AppointmentState.maybeLowerDose, result);
        } catch (AppError appError) {
            appError.printStackTrace();
            fail();
        }
    }

    @Test
    public void lowerDoseOnGrade4() {
        try {
            sut.saveOAK(1000, 0.4, 200_000, 0, 0, false);
            assertEquals(4, sut.getPatient().getTreatments().last().getOaks().last().getGrade());

            AppointmentState result = sut.appointment(Calendar.getInstance().getTime(), TreatmentDose.dose125);
            assertEquals(AppointmentState.maybeLowerDose, result);
        } catch (AppError appError) {
            appError.printStackTrace();
            fail();
        }
    }

    @Test
    public void setGreaterDoseInstedOfLowering() {
        try {
            sut.saveOAK(1000, 0.4, 200_000, 0, 0, false);
            assertEquals(4, sut.getPatient().getTreatments().last().getOaks().last().getGrade());
            assertEquals(TreatmentDose.dose100, sut.getPatient().getTreatments().last().getDose());

            AppointmentState result = sut.appointment(Calendar.getInstance().getTime(), TreatmentDose.dose125);
            assertEquals(AppointmentState.maybeLowerDose, result);

            try {
                sut.selectLowerDose(TreatmentDose.dose125);
                fail();
            } catch (AppError appError) {
                assertEquals(AppError.selectedLargerDose, appError.type);
            }
        } catch (AppError appError) {
            appError.printStackTrace();
            fail();
        }
    }

    @Test
    public void setLowerDose() {
        try {
            sut.saveOAK(1000, 0.4, 200_000, 0, 0, false);
            assertEquals(4, sut.getPatient().getTreatments().last().getOaks().last().getGrade());
            assertEquals(TreatmentDose.dose100, sut.getPatient().getTreatments().last().getDose());

            AppointmentState result = sut.appointment(Calendar.getInstance().getTime(), TreatmentDose.dose125);
            assertEquals(AppointmentState.maybeLowerDose, result);

            sut.selectLowerDose(TreatmentDose.dose75);

            Treatment treatment = sut.getPatient().getTreatments().last();
            assertEquals(TreatmentDose.dose75, treatment.getDose());
            Oak oak = treatment.getOaks().last();
            assertNull(oak.getReadyDate());
            assertEquals(29, dateHelper.daysFrom(treatment.getStartDate(), oak.getAssignmentDate()));
            assertNotNull(sut.getPatient().getAppointments().last());
            assertEquals(29, dateHelper.daysFrom(treatment.getStartDate(), sut.getPatient().getAppointments().last().getDate()));
            assertNotNull(treatment.getPauseDate());
            assertEquals(22, dateHelper.daysFrom(treatment.getStartDate(), treatment.getPauseDate()));
        } catch (AppError appError) {
            appError.printStackTrace();
            fail();
        }
    }

    @Test
    public void setSameDose() {
        try {
            sut.saveOAK(1000, 0.4, 200_000, 0, 0, false);
            assertEquals(4, sut.getPatient().getTreatments().last().getOaks().last().getGrade());
            assertEquals(TreatmentDose.dose100, sut.getPatient().getTreatments().last().getDose());

            AppointmentState result = sut.appointment(Calendar.getInstance().getTime(), TreatmentDose.dose125);
            assertEquals(AppointmentState.maybeLowerDose, result);

            sut.selectLowerDose(TreatmentDose.dose100);

            Treatment treatment = sut.getPatient().getTreatments().last();
            assertEquals(TreatmentDose.dose100, treatment.getDose());
            Oak oak = treatment.getOaks().last();
            assertNull(oak.getReadyDate());
            assertEquals(29, dateHelper.daysFrom(treatment.getStartDate(), oak.getAssignmentDate()));
            assertNotNull(sut.getPatient().getAppointments().last());
            assertEquals(29, dateHelper.daysFrom(treatment.getStartDate(), sut.getPatient().getAppointments().last().getDate()));
            assertNotNull(treatment.getPauseDate());
            assertEquals(22, dateHelper.daysFrom(treatment.getStartDate(), treatment.getPauseDate()));
        } catch (AppError appError) {
            appError.printStackTrace();
            fail();
        }
    }
}
