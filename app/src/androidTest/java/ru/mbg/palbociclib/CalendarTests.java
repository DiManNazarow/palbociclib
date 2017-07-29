package ru.mbg.palbociclib;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class CalendarTests {

    private Realm testRealm;
    private DateHelper dateHelper;
    private Date startDate = new Date();

    @Before
    public void setUp() {
        Realm.init(InstrumentationRegistry.getTargetContext());

        RealmConfiguration.Builder builder = new RealmConfiguration.Builder()
                .inMemory();
        RealmConfiguration configuration = builder.build();
        testRealm = Realm.getInstance(configuration);

        startDate = DateHelper.strippingTime(startDate);

        dateHelper = new DateHelper();
        dateHelper.mockDate = startDate;
    }

    @After
    public void tearDown() {
        testRealm.beginTransaction();
        testRealm.deleteAll();
        testRealm.commitTransaction();
    }

    @Test
    public void noStartOfCycleForFirstTreatment() {
        try {
            new PatientModel("Василиса Иванова", Menopause.perimenopause, false, new TestSettings(), testRealm, dateHelper);
        } catch (AppError appError) {
            fail();
        }

        PatientProvider sut = new PatientProvider(testRealm, dateHelper);
        List<PatientProvider.EventRow> events = sut.getEventsFor(null).getAllEvents();

        assertEquals(2, events.size());
        int nextCounts = 0;
        for (PatientProvider.EventRow e : events) {
            if (e.state == PatientProvider.State.nextTreatment) {
                nextCounts += 1;
            }
        }
        assertEquals(0, nextCounts);
    }

    @Test
    public void twoStartsOfCycleWhenTreatmentStarted() {
        PatientModel patient;
        try {
            patient = new PatientModel("Василиса Иванова", Menopause.perimenopause, false, new TestSettings(), testRealm, dateHelper);
            patient.saveOAK(4000, 0.4, 200_000, false);
            patient.appointment();
        } catch (AppError appError) {
            fail();
            return;
        }

        PatientProvider sut = new PatientProvider(testRealm, dateHelper);
        List<PatientProvider.EventRow> events = sut.getEventsFor(null).getAllEvents();

        List<PatientProvider.EventRow> startCycleEvents = new ArrayList<>(2);
        for (PatientProvider.EventRow e : events) {
            if (e.state == PatientProvider.State.nextTreatment) {
                startCycleEvents.add(e);
            }
        }

        assertEquals(2, patient.getPatient().getTreatments().size());
        assertEquals(2, startCycleEvents.size());
        if (startCycleEvents.get(0).state != PatientProvider.State.nextTreatment || startCycleEvents.get(0).cycleNumber != 1) {
            fail();
        }
        if (startCycleEvents.get(1).state != PatientProvider.State.nextTreatment || startCycleEvents.get(1).cycleNumber != 2) {
            fail();
        }
    }

    @Test
    public void threeStartsOfCycleWhenTreatmentStarted() {
        PatientModel patient;
        try {
            patient = new PatientModel("Василиса Иванова", Menopause.perimenopause, false, new TestSettings(), testRealm, dateHelper);
            patient.saveOAK(4000, 0.4, 200_000, false);
            patient.appointment();
            dateHelper.mockDate = DateHelper.advancingDays(dateHelper.mockDate, 14);
            patient.saveOAK(4000, 0.4, 200_000, false);
            patient.appointment();
            dateHelper.mockDate = DateHelper.advancingDays(dateHelper.mockDate, 14);
            patient.saveOAK(4000, 0.4, 200_000, false);
            patient.appointment();
        } catch (AppError appError) {
            fail();
            return;
        }

        PatientProvider sut = new PatientProvider(testRealm, dateHelper);
        dateHelper.mockDate = startDate;
        List<PatientProvider.EventRow> events = sut.getEventsFor(null).getAllEvents();

        List<PatientProvider.EventRow> startCycleEvents = new ArrayList<>(3);
        for (PatientProvider.EventRow e : events) {
            if (e.state == PatientProvider.State.nextTreatment) {
                startCycleEvents.add(e);
            }
        }

        assertEquals(3, patient.getPatient().getTreatments().size());
        assertEquals(3, startCycleEvents.size());
        if (startCycleEvents.get(0).state != PatientProvider.State.nextTreatment || startCycleEvents.get(0).cycleNumber != 1) {
            fail();
        }
        if (startCycleEvents.get(1).state != PatientProvider.State.nextTreatment || startCycleEvents.get(1).cycleNumber != 2) {
            fail();
        }
        if (startCycleEvents.get(2).state != PatientProvider.State.nextTreatment || startCycleEvents.get(2).cycleNumber != 3) {
            fail();
        }
    }
}
