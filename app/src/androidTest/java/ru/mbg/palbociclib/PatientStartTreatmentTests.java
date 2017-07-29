package ru.mbg.palbociclib;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

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
public class PatientStartTreatmentTests {

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
        dateHelper.mockDate = null;

        try {
            sut = new PatientModel("Василиса Иванова", Menopause.perimenopause, false, new TestSettings(), testRealm, dateHelper);
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
    public void notStartTreatmentOnBadOAK1() {
        try {
            sut.saveOAK(100, 0.4, 200_000, false);
        } catch (AppError appError) {
            fail();
        }
        try {
            AppointmentState result = sut.appointment();
            assertEquals(AppointmentState.assignNextAppointment, result);
        } catch (AppError appError) {
            fail();
        }
    }

    @Test
    public void notStartTreatmentOnBadOAK2() {
        try {
            sut.saveOAK(3000, 0.4, 2000, false);
        } catch (AppError appError) {
            fail();
        }
        try {
            AppointmentState result = sut.appointment();
            assertEquals(AppointmentState.assignNextAppointment, result);
        } catch (AppError appError) {
            fail();
        }
    }

    @Test
    public void startTreatment() {
        assertEquals(1, sut.getPatient().getTreatments().size());

        try {
            sut.saveOAK(3000, 0.4, 200_000, false);
        } catch (AppError appError) {
            fail();
        }
        try {
            AppointmentState result = sut.appointment();

            assertEquals(AppointmentState.done, result);
            assertEquals(2, sut.getPatient().getTreatments().size());
            Treatment treatment = sut.getPatient().getTreatments().last();
            assertTrue(dateHelper.isToday(treatment.getStartDate()));
            assertEquals(TreatmentDose.dose125, treatment.getDose());

            assertEquals(2, treatment.getOaks().size());
            Oak oldOak = treatment.getOaks().first();
            assertEquals(3000, oldOak.getLeukocytes());
            assertEquals(0.4, oldOak.getNeutrophils(), 0.01);
            assertEquals(200_000, oldOak.getPlatelets());
            assertFalse(oldOak.isFever());

            Oak newOak = treatment.getOaks().last();
            assertEquals(14, dateHelper.daysTo(newOak.getAssignmentDate()));
            assertNull(newOak.getReadyDate());

            assertNotNull(sut.getPatient().getAppointments().last());
            assertEquals(14, dateHelper.daysTo(sut.getPatient().getAppointments().last().getDate()));
        } catch (AppError appError) {
            fail();
        }
    }
}
