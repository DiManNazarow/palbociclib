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
import ru.mbg.palbociclib.models.Appointment;
import ru.mbg.palbociclib.models.BackgroundTherapyType;
import ru.mbg.palbociclib.models.Menopause;
import ru.mbg.palbociclib.models.Oak;
import ru.mbg.palbociclib.models.Treatment;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class PatientTests {

    private Realm testRealm;
    private Settings settings = new TestSettings();

    @Before
    public void setUp() {
        Realm.init(InstrumentationRegistry.getTargetContext());

        RealmConfiguration.Builder builder = new RealmConfiguration.Builder()
                .inMemory();
        RealmConfiguration configuration = builder.build();
        testRealm = Realm.getInstance(configuration);
    }

    @After
    public void tearDown() {
        testRealm.beginTransaction();
        testRealm.deleteAll();
        testRealm.commitTransaction();
    }

    @Test
    public void throwsOnWrongPatient() {
        try {
            new PatientModel("Василиса Иванова", Menopause.none, false, settings, testRealm);
            fail();
        } catch (AppError appError) {
            assertEquals(appError.type, AppError.notSupportedPatient);
        }

        try {
            new PatientModel("Василиса Иванова", Menopause.menopause, false, settings, testRealm);
            fail();
        } catch (AppError appError) {
            assertEquals(appError.type, AppError.notSupportedPatient);
        }
    }

    @Test
    public void recommendFulvestrantWithoutHormonalTherapy() {
        try {
            PatientModel sut = new PatientModel("Василиса Иванова", Menopause.perimenopause, false, settings, testRealm);

            assertNotNull(sut.getPatient().getBackgroundTherapy());
            assertEquals(BackgroundTherapyType.fulvestrant, sut.getPatient().getBackgroundTherapy().getType());
        } catch (AppError appError) {
            fail();
        }
    }

    @Test
    public void recommendFulvestrantWithHormonalTherapy() {
        try {
            PatientModel sut = new PatientModel("Василиса Иванова", Menopause.postmenopause, true, settings, testRealm);

            assertNotNull(sut.getPatient().getBackgroundTherapy());
            assertEquals(BackgroundTherapyType.fulvestrant, sut.getPatient().getBackgroundTherapy().getType());
        } catch (AppError appError) {
            fail();
        }
    }

    @Test
    public void recommendLetrozole() {
        try {
            PatientModel sut = new PatientModel("Василиса Иванова", Menopause.postmenopause, false, settings, testRealm);

            assertNotNull(sut.getPatient().getBackgroundTherapy());
            assertEquals(BackgroundTherapyType.letrozole, sut.getPatient().getBackgroundTherapy().getType());
        } catch (AppError appError) {
            fail();
        }
    }

    @Test
    public void assignOAKAndAppointmentOnCreationOfPatient() {
        try {
            DateHelper dateHelper = new DateHelper();
            PatientModel sut = new PatientModel("Василиса Иванова", Menopause.postmenopause, false, settings, testRealm, dateHelper);

            assertEquals(1, sut.getPatient().getTreatments().size());
            Treatment treatment = sut.getPatient().getTreatments().first();

            assertEquals(1, treatment.getOaks().size());

            Oak oak = treatment.getOaks().first();
            assertTrue(dateHelper.isToday(oak.getAssignmentDate()));
            assertNull(oak.getReadyDate());

            assertNotNull(sut.getPatient().getAppointments().last());
            Appointment appointment = sut.getPatient().getAppointments().last();
            assertTrue(dateHelper.isToday(appointment.getDate()));
        } catch (AppError appError) {
            fail();
        }
    }
}
