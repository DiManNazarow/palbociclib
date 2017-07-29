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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class PatientSaveOAKTests {

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
    public void saveOAKThrowsIfNoOAKAssigned() {
        Treatment treatment = sut.getPatient().getTreatments().first();
        final Oak oak = treatment.getOaks().first();
        testRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                oak.setReadyDate(dateHelper.currentDate());
            }
        });
        try {
            sut.saveOAK(10, 1, 10, false);
            fail();
        } catch (AppError appError) {
            assertEquals(AppError.oakNotAssigned, appError.type);
        }
    }

    @Test
    public void saveOAKGrade4() {
        try {
            sut.saveOAK(1000, 0.45, 20, false);

            Treatment treatment = sut.getPatient().getTreatments().first();
            Oak oak = treatment.getOaks().first();
            assertEquals(1000, oak.getLeukocytes());
            assertEquals(0.45, oak.getNeutrophils(), 0.001);
            assertEquals(20, oak.getPlatelets());
            assertFalse(oak.isFever());
            assertNotNull(oak.getReadyDate());
            assertTrue(dateHelper.isToday(oak.getReadyDate()));

            assertEquals(4, oak.getGrade());
        } catch (AppError appError) {
            fail();
        }
    }

    @Test
    public void saveOAKGrade3() {
        try {
            sut.saveOAK(2000, 0.45, 20, false);

            Treatment treatment = sut.getPatient().getTreatments().first();
            Oak oak = treatment.getOaks().first();
            assertEquals(3, oak.getGrade());
        } catch (AppError appError) {
            fail();
        }
    }

    @Test
    public void saveOAKGrade2() {
        try {
            sut.saveOAK(3000, 0.45, 20, false);

            Treatment treatment = sut.getPatient().getTreatments().first();
            Oak oak = treatment.getOaks().first();
            assertEquals(2, oak.getGrade());
        } catch (AppError appError) {
            fail();
        }
    }

    @Test
    public void saveOAKGrade1() {
        try {
            sut.saveOAK(4000, 0.45, 20, false);

            Treatment treatment = sut.getPatient().getTreatments().first();
            Oak oak = treatment.getOaks().first();
            assertEquals(1, oak.getGrade());
        } catch (AppError appError) {
            fail();
        }
    }
}
