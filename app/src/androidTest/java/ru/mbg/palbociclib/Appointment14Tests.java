package ru.mbg.palbociclib;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import ru.mbg.palbociclib.helpers.DateHelper;
import ru.mbg.palbociclib.models.AppointmentState;
import ru.mbg.palbociclib.models.Menopause;
import ru.mbg.palbociclib.models.Oak;
import ru.mbg.palbociclib.models.Treatment;
import ru.mbg.palbociclib.models.TreatmentDose;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class Appointment14Tests {

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
            sut.saveOAK(3000, 0.4, 200_000, false);
            sut.appointment();
            dateHelper.mockDate = DateHelper.advancingDays(dateHelper.mockDate, 14);
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
    public void continueTreatmentOnGrade1() {
        try {
            Treatment treatment = sut.getPatient().getTreatments().last();

            Oak readyOak = treatment.getOaks().last();
            sut.saveOAK(4000, 0.4, 200_000, false);
            assertNotNull(readyOak.getReadyDate());
            assertEquals(1, readyOak.getGrade());

            AppointmentState result = sut.appointment();
            assertEquals(AppointmentState.done, result);

            Oak oak = treatment.getOaks().last();
            assertNull(oak.getReadyDate());
            assertEquals(29, dateHelper.daysFrom(treatment.getStartDate(), oak.getAssignmentDate()));
            assertNotNull(sut.getPatient().getAppointments().last());
            assertEquals(29, dateHelper.daysFrom(treatment.getStartDate(), sut.getPatient().getAppointments().last().getDate()));
            assertNotNull(treatment.getPauseDate());
            assertEquals(21, dateHelper.daysFrom(treatment.getStartDate(), treatment.getPauseDate()));
        } catch (AppError appError) {
            appError.printStackTrace();
            fail();
        }
    }

    @Test
    public void continueTreatmentOnGrade2() {
        try {
            Treatment treatment = sut.getPatient().getTreatments().last();

            Oak readyOak = treatment.getOaks().last();
            sut.saveOAK(3000, 0.4, 200_000, false);
            assertNotNull(readyOak.getReadyDate());
            assertEquals(2, readyOak.getGrade());

            AppointmentState result = sut.appointment();
            assertEquals(AppointmentState.done, result);

            Oak oak = treatment.getOaks().last();
            assertNull(oak.getReadyDate());
            assertEquals(29, dateHelper.daysFrom(treatment.getStartDate(), oak.getAssignmentDate()));
            assertNotNull(sut.getPatient().getAppointments().last());
            assertEquals(29, dateHelper.daysFrom(treatment.getStartDate(), sut.getPatient().getAppointments().last().getDate()));
            assertNotNull(treatment.getPauseDate());
            assertEquals(21, dateHelper.daysFrom(treatment.getStartDate(), treatment.getPauseDate()));
        } catch (AppError appError) {
            appError.printStackTrace();
            fail();
        }
    }

    @Test
    public void continueTreatmentOnGrade3NoFever() {
        try {
            Treatment treatment = sut.getPatient().getTreatments().last();

            Oak readyOak = treatment.getOaks().last();
            sut.saveOAK(2000, 0.4, 200_000, false);
            assertNotNull(readyOak.getReadyDate());
            assertEquals(3, readyOak.getGrade());
            assertFalse(readyOak.isFever());

            AppointmentState result = sut.appointment();
            assertEquals(AppointmentState.done, result);

            Oak oak = treatment.getOaks().last();
            assertNull(oak.getReadyDate());
            assertEquals(21, dateHelper.daysFrom(treatment.getStartDate(), oak.getAssignmentDate()));
            assertNotNull(sut.getPatient().getAppointments().last());
            assertEquals(21, dateHelper.daysFrom(treatment.getStartDate(), sut.getPatient().getAppointments().last().getDate()));
        } catch (AppError appError) {
            appError.printStackTrace();
            fail();
        }
    }

    @Test
    public void pauseTreatmentOnGrade3FeverBigDose() {
        try {
            Treatment treatment = sut.getPatient().getTreatments().last();
            assertEquals(TreatmentDose.dose125, treatment.getDose());

            Oak readyOak = treatment.getOaks().last();
            sut.saveOAK(2000, 0.4, 200_000, true);
            assertNotNull(readyOak.getReadyDate());
            assertEquals(3, readyOak.getGrade());
            assertTrue(readyOak.isFever());

            AppointmentState result = sut.appointment();
            assertEquals(AppointmentState.doseWasReduced, result);
            assertEquals(TreatmentDose.dose100, treatment.getDose());
            assertNotNull(treatment.getPauseDate());
            assertEquals(22, dateHelper.daysFrom(treatment.getStartDate(), treatment.getPauseDate()));
            assertNotNull(sut.getPatient().getAppointments().last());
            assertEquals(29, dateHelper.daysFrom(treatment.getStartDate(), sut.getPatient().getAppointments().last().getDate()));
        } catch (AppError appError) {
            appError.printStackTrace();
            fail();
        }
    }

    @Test
    public void stopTreatmentOnGrade3FeverSmallDose() {
        try {
            final Treatment treatment = sut.getPatient().getTreatments().last();
            testRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    treatment.setDose(TreatmentDose.dose75);
                }
            });

            Oak readyOak = treatment.getOaks().last();
            sut.saveOAK(2000, 0.4, 200_000, true);
            assertNotNull(readyOak.getReadyDate());
            assertEquals(3, readyOak.getGrade());
            assertTrue(readyOak.isFever());

            AppointmentState result = sut.appointment();
            assertEquals(AppointmentState.assignContinueTreatment, result);
            assertEquals(TreatmentDose.dose75, treatment.getDose());
            assertNotNull(treatment.getPauseDate());
            assertEquals(0, dateHelper.daysFrom(dateHelper.currentDate(), treatment.getPauseDate()));
            assertNotNull(sut.getPatient().getAppointments().last());
            assertEquals(29, dateHelper.daysFrom(treatment.getStartDate(), sut.getPatient().getAppointments().last().getDate()));
        } catch (AppError appError) {
            appError.printStackTrace();
            fail();
        }
    }

    @Test
    public void pauseTreatmentOnGrade4BigDose() {
        try {
            Treatment treatment = sut.getPatient().getTreatments().last();
            assertEquals(TreatmentDose.dose125, treatment.getDose());

            Oak readyOak = treatment.getOaks().last();
            sut.saveOAK(1000, 0.4, 200_000, true);
            assertNotNull(readyOak.getReadyDate());
            assertEquals(4, readyOak.getGrade());

            AppointmentState result = sut.appointment();
            assertEquals(AppointmentState.doseWasReduced, result);
            assertEquals(TreatmentDose.dose100, treatment.getDose());
            assertNotNull(treatment.getPauseDate());
            assertEquals(22, dateHelper.daysFrom(treatment.getStartDate(), treatment.getPauseDate()));
            assertNotNull(sut.getPatient().getAppointments().last());
            assertEquals(29, dateHelper.daysFrom(treatment.getStartDate(), sut.getPatient().getAppointments().last().getDate()));
        } catch (AppError appError) {
            appError.printStackTrace();
            fail();
        }
    }

    @Test
    public void pauseTreatmentOnGrade4SmallDose() {
        try {
            final Treatment treatment = sut.getPatient().getTreatments().last();
            testRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    treatment.setDose(TreatmentDose.dose75);
                }
            });

            Oak readyOak = treatment.getOaks().last();
            sut.saveOAK(1000, 0.4, 200_000, true);
            assertNotNull(readyOak.getReadyDate());
            assertEquals(4, readyOak.getGrade());

            AppointmentState result = sut.appointment();
            assertEquals(AppointmentState.assignContinueTreatment, result);
            assertEquals(TreatmentDose.dose75, treatment.getDose());
            assertNotNull(treatment.getPauseDate());
            assertEquals(0, dateHelper.daysFrom(dateHelper.currentDate(), treatment.getPauseDate()));
            assertNotNull(sut.getPatient().getAppointments().last());
            assertEquals(29, dateHelper.daysFrom(treatment.getStartDate(), sut.getPatient().getAppointments().last().getDate()));
        } catch (AppError appError) {
            appError.printStackTrace();
            fail();
        }
    }
}
