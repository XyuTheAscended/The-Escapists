package com.model;

import com.model.Coding.Gameplay.Timer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.Assert.*;
/**
 * JUnit 4 tests for com.model.Coding.Gameplay.Timer
 *
 * Notes:
 * - Timer is a singleton; tests reset the private static instance between tests via reflection.
 * - The Timer spawns threads that sleep; tests avoid waiting by manipulating internal fields
 *   (remainingTime, isRunning, cachedTimePassed) using reflection rather than relying on real time passing.
 */
public class TestTimer {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;

    @Before
    public void setUp() throws Exception {
        // Reset System.out capture
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Ensure singleton is reset before each test
        resetSingleton();
    }

    @After
    public void tearDown() throws Exception {
        // Restore original System.out
        System.setOut(originalOut);

        // Try to stop any running timer thread and then reset singleton again
        Timer t = Timer.getInstance();
        stopTimerThreadIfAlive(t);
        resetSingleton();
    }

    private void resetSingleton() throws Exception {
        Field timerField = Timer.class.getDeclaredField("timer");
        timerField.setAccessible(true);
        timerField.set(null, null); // static field -> null root
    }

    private Field getField(String name) throws Exception {
        Field f = Timer.class.getDeclaredField(name);
        f.setAccessible(true);
        return f;
    }

    private void setPrivateField(Timer t, String fieldName, Object value) throws Exception {
        Field f = getField(fieldName);
        f.set(t, value);
    }

    private Object getPrivateField(Timer t, String fieldName) throws Exception {
        Field f = getField(fieldName);
        return f.get(t);
    }

    private void callPrivateStart(Timer t) throws Exception {
        Method m = Timer.class.getDeclaredMethod("start");
        m.setAccessible(true);
        m.invoke(t);
    }

    private void stopTimerThreadIfAlive(Timer t) throws Exception {
        Thread th = (Thread) getPrivateField(t, "timerThread");
        if (th != null && th.isAlive()) {
            th.interrupt();
            // Give thread a moment to stop
            th.join(50);
        }
        // Also ensure isRunning=false
        try {
            setPrivateField(t, "isRunning", false);
        } catch (NoSuchFieldException ignored) {}
    }

    @Test
    public void testSingletonUniqueInstance() {
        Timer a = Timer.getInstance();
        Timer b = Timer.getInstance();
        assertNotNull("getInstance should not return null", a);
        assertSame("getInstance should return the same instance every time", a, b);
    }

    @Test
    public void testStartInitializesFieldsAndStarts() throws Exception {
        Timer t = Timer.getInstance();

        // start with startTime 120 and remainingTime defaulting to same value
        t.start(120);

        // Immediately after start, isRunning should be true and fields set
        Boolean isRunning = (Boolean) getPrivateField(t, "isRunning");
        assertTrue("Timer should be running immediately after start()", isRunning);

        Integer startTime = (Integer) getPrivateField(t, "startTime");
        Integer remainingTime = (Integer) getPrivateField(t, "remainingTime");

        assertEquals("startTime should be set to provided value", Integer.valueOf(120), startTime);
        assertEquals("remainingTime should be set to provided value", Integer.valueOf(120), remainingTime);

        // Pause quickly to avoid letting the background thread decrement anything
        t.pause();

        // After pause, isRunning should be false
        isRunning = (Boolean) getPrivateField(t, "isRunning");
        assertFalse("Timer should not be running after pause()", isRunning);
    }

    @Test
    public void testPauseCachesTimePassed() throws Exception {
        Timer t = Timer.getInstance();

        // simulate a start where some time has already passed by setting fields directly
        setPrivateField(t, "startTime", 100);
        setPrivateField(t, "remainingTime", 80);
        setPrivateField(t, "isRunning", true);

        // getTimePassed while running should compute startTime - remainingTime
        int runningPassed = t.getTimePassed();
        assertEquals("When running, time passed is startTime - remainingTime", 20, runningPassed);

        // Pause should set cachedTimePassed and clear isRunning
        t.pause();
        Boolean isRunning = (Boolean) getPrivateField(t, "isRunning");
        assertFalse(isRunning);

        Integer cached = (Integer) getPrivateField(t, "cachedTimePassed");
        assertEquals("cachedTimePassed should be set to startTime - remainingTime on pause()", Integer.valueOf(20), cached);

        // Now time passed when not running should return cached value
        int pausedPassed = t.getTimePassed();
        assertEquals("When paused, getTimePassed() should return cachedTimePassed", 20, pausedPassed);
    }

    @Test
    public void testResetClearsFields() throws Exception {
        Timer t = Timer.getInstance();

        // Set some fields to non-zero values
        setPrivateField(t, "startTime", 200);
        setPrivateField(t, "remainingTime", 150);
        setPrivateField(t, "cachedTimePassed", 50);
        setPrivateField(t, "isRunning", true);

        t.reset();

        Integer startTime = (Integer) getPrivateField(t, "startTime");
        Integer remainingTime = (Integer) getPrivateField(t, "remainingTime");
        Integer cached = (Integer) getPrivateField(t, "cachedTimePassed");
        Boolean isRunning = (Boolean) getPrivateField(t, "isRunning");

        assertEquals("reset should set startTime to 0", Integer.valueOf(0), startTime);
        assertEquals("reset should set remainingTime to 0", Integer.valueOf(0), remainingTime);
        assertEquals("reset should set cachedTimePassed to previous computed value", Integer.valueOf(50), cached); // method saved it first
        assertFalse("reset should set isRunning to false", isRunning);
    }

    @Test
    public void testResumeWhenNotStartedPrintsMessage() throws Exception {
        Timer t = Timer.getInstance();

        // Ensure startTime == 0 and remainingTime == 0
        setPrivateField(t, "startTime", 0);
        setPrivateField(t, "remainingTime", 0);
        setPrivateField(t, "isRunning", false);

        t.resume();
        String out = outContent.toString().trim();
        assertTrue("resume with startTime==0 should print appropriate message",
                out.contains("Cannot resume. Timer has not been started"));
    }

    @Test
    public void testResumeWhenNoTimeLeftPrintsMessage() throws Exception {
        Timer t = Timer.getInstance();

        // Set startTime != 0 but remainingTime == 0
        setPrivateField(t, "startTime", 30);
        setPrivateField(t, "remainingTime", 0);
        setPrivateField(t, "isRunning", false);

        t.resume();
        String out = outContent.toString().trim();
        assertTrue("resume with remainingTime==0 should print appropriate message",
                out.contains("Cannot resume. No time left."));
    }

    @Test
    public void testResumeStartsWhenValid() throws Exception {
        Timer t = Timer.getInstance();

        // Set a valid state for resume: startTime != 0, remainingTime > 0, not running
        setPrivateField(t, "startTime", 10);
        setPrivateField(t, "remainingTime", 5);
        setPrivateField(t, "isRunning", false);

        // Call resume (which uses private start())
        t.resume();

        // isRunning should be true (the start() method sets it)
        Boolean isRunning = (Boolean) getPrivateField(t, "isRunning");
        assertTrue("resume() should start the timer (isRunning true) when valid", isRunning);

        // Clean up background thread
        stopTimerThreadIfAlive(t);
    }

    @Test
    public void testGetTimePassedFormattedVariousValues() throws Exception {
        Timer t = Timer.getInstance();

        // Case 1: 0 seconds passed -> 00:00:00
        setPrivateField(t, "cachedTimePassed", 0);
        setPrivateField(t, "isRunning", false);
        assertEquals("00:00:00", t.getTimePassedFormatted());

        // Case 2: 3661 seconds -> 01:01:01
        setPrivateField(t, "cachedTimePassed", 3661);
        assertEquals("01:01:01", t.getTimePassedFormatted());

        // Case 3: Large hours -> 10:12:34 (for 36754 seconds)
        setPrivateField(t, "cachedTimePassed", 36754); // 10h 12m 34s
        assertEquals("10:12:34", t.getTimePassedFormatted());
    }

    @Test
    public void testGetRemainingTimeAccessor() throws Exception {
        Timer t = Timer.getInstance();
        setPrivateField(t, "remainingTime", 77);
        assertEquals("getRemainingTime should return internal remainingTime", 77, t.getRemainingTime());
    }

    @Test
    public void testDoubleStartDoesNotRestartIfAlreadyRunning() throws Exception {
        Timer t = Timer.getInstance();

        t.start(20);
        Boolean isRunning = (Boolean) getPrivateField(t, "isRunning");
        assertTrue(isRunning);

        // Call start again with different times - should be ignored if isRunning true
        t.start(5);

        // The object should still report the original startTime and remainingTime (or at least not have become 5)
        Integer startTime = (Integer) getPrivateField(t, "startTime");
        Integer remainingTime = (Integer) getPrivateField(t, "remainingTime");

        assertEquals("startTime should remain 20 (double start should be ignored)", Integer.valueOf(20), startTime);
        assertEquals("remainingTime should remain 20 (double start should be ignored)", Integer.valueOf(20), remainingTime);

        t.pause();
    }

    @Test
    public void testGetTimePassedWhenRunningAndWhenPaused() throws Exception {
        Timer t = Timer.getInstance();

        // Simulate running state
        setPrivateField(t, "startTime", 90);
        setPrivateField(t, "remainingTime", 70);
        setPrivateField(t, "isRunning", true);
        assertEquals("When running, time passed should be computed from start and remaining", 20, t.getTimePassed());

        // Simulate paused state with cachedTimePassed
        t.pause(); // this will set cachedTimePassed to startTime - remainingTime
        setPrivateField(t, "isRunning", false);
        assertEquals("When paused, getTimePassed should return cached value", 20, t.getTimePassed());
    }

    @Test
    public void testStartWithCustomRemainingLessThanStart() throws Exception {
        Timer t = Timer.getInstance();

        // Start with startTime 100 and remainingTime 60
        t.start(100, 60);

        Integer startTime = (Integer) getPrivateField(t, "startTime");
        Integer remainingTime = (Integer) getPrivateField(t, "remainingTime");
        Boolean isRunning = (Boolean) getPrivateField(t, "isRunning");

        assertEquals(Integer.valueOf(100), startTime);
        assertEquals(Integer.valueOf(60), remainingTime);
        assertTrue(isRunning);

        // clean
        t.pause();
    }

    @Test
    public void testStartWithRemainingGreaterThanStart_behavesPredictably() throws Exception {
        Timer t = Timer.getInstance();

        // Although comment suggests remainingTime should be less than startTime,
        // class does not enforce it. Start with remainingTime > startTime to see behavior.
        t.start(10, 20);

        Integer startTime = (Integer) getPrivateField(t, "startTime");
        Integer remainingTime = (Integer) getPrivateField(t, "remainingTime");

        // It should simply accept the values; we assert that values are what was set
        assertEquals(Integer.valueOf(10), startTime);
        assertEquals(Integer.valueOf(20), remainingTime);

        // Stop to clean
        t.pause();
    }
}
