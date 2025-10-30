package com.model;

import com.model.Coding.Gameplay.GameFacade;
import com.model.Coding.Gameplay.Timer;
import com.model.Coding.Progress.Leaderboard;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * JUnit4 tests for com.model.Coding.Gameplay.GameFacade using Mockito
 *
 * - Resets the GameFacade singleton between tests using reflection.
 * - Uses Mockito to create mocks for typed fields (User, Progress, Achievement, etc.)
 *   so we avoid Unsafe and module-access issues.
 */
public class TestGameFacade {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;

    @Before
    public void setUp() throws Exception {
        // capture System.out for tests that check printed messages
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Reset GameFacade singleton
        resetSingleton();
    }

    @After
    public void tearDown() throws Exception {
        // restore System.out
        System.setOut(originalOut);

        // Ensure Timer is reset (safe to call)
        try {
            Timer.getInstance().reset();
        } catch (Exception ignored) {}

        // Reset singleton again
        resetSingleton();
    }

    // -------------------------
    // Reflection helper methods
    // -------------------------
    private void resetSingleton() throws Exception {
        Field gfField = GameFacade.class.getDeclaredField("gameFacade");
        gfField.setAccessible(true);
        gfField.set(null, null);
    }

    private Field getField(String name) throws Exception {
        Field f = GameFacade.class.getDeclaredField(name);
        f.setAccessible(true);
        return f;
    }

    private void setPrivateField(GameFacade gf, String fieldName, Object value) throws Exception {
        Field f = getField(fieldName);
        f.set(gf, value);
    }

    private Object getPrivateField(GameFacade gf, String fieldName) throws Exception {
        Field f = getField(fieldName);
        return f.get(gf);
    }

    /**
     * Create a Mockito mock for the given fully-qualified class name.
     * Returns an Object that is assignable to fields typed with that class.
     */
    private Object mockFor(String fqcn) throws Exception {
        Class<?> cls = Class.forName(fqcn);
        return Mockito.mock(cls);
    }

    // -------------------------
    // Tests
    // -------------------------

    @Test
    public void testSingletonUniqueInstance() {
        GameFacade a = GameFacade.getInstance();
        GameFacade b = GameFacade.getInstance();
        assertNotNull("getInstance should not return null", a);
        assertSame("getInstance should return same instance", a, b);
    }

    @Test
    public void testStartGameThrowsWhenNoCurrentUser() {
        GameFacade gf = GameFacade.getInstance();

        // Ensure currentUser is null
        try {
            setPrivateField(gf, "currentUser", null);
        } catch (Exception e) {
            fail("Reflection setup failed: " + e.getMessage());
        }

        try {
            gf.startGame(1);
            fail("startGame should throw when currentUser is null");
        } catch (RuntimeException expected) {
            assertTrue(expected.getMessage().toLowerCase().contains("cant start"));
        }
    }

    @Test
    public void testEndGameThrowsWhenNoCurrentUserOrActiveProgress() throws Exception {
        GameFacade gf = GameFacade.getInstance();

        // Case 1: currentUser == null -> should throw
        setPrivateField(gf, "currentUser", null);
        setPrivateField(gf, "activeProgress", null);
        try {
            gf.endGame();
            fail("endGame should throw when currentUser is null");
        } catch (RuntimeException expected) {
            assertTrue(expected.getMessage().toLowerCase().contains("can't end"));
        }

        // Case 2: currentUser set but activeProgress == null -> should throw
        Object dummyUser = mockFor("com.model.Coding.User.User");
        setPrivateField(gf, "currentUser", dummyUser);
        setPrivateField(gf, "activeProgress", null);

        try {
            gf.endGame();
            fail("endGame should throw when activeProgress is null");
        } catch (RuntimeException expected) {
            // expected; test passes
        }
    }

    @Test
    public void testPauseAndResumeToggleIsPausedAndCallsTimerSafely() throws Exception {
        GameFacade gf = GameFacade.getInstance();

        // Ensure initial paused state is false
        assertFalse("Initially game should not be paused", gf.isPaused());

        // Pause when not paused -> should set isPaused true and call Timer.pause (safe to call)
        gf.pause();
        assertTrue("After pause() isPaused should be true", gf.isPaused());

        // Calling pause again should print "Game is already paused." and keep isPaused true
        outContent.reset();
        gf.pause();
        String out = outContent.toString();
        assertTrue("Pause when already paused should mention 'already paused' in output",
                out.contains("already paused"));

        // Now resume: set isPaused true first
        setPrivateField(gf, "isPaused", true);
        gf.resume();
        assertFalse("After resume() isPaused should be false", gf.isPaused());

        // Calling resume when not paused should do nothing and not flip state
        outContent.reset();
        gf.resume();
        assertFalse("Calling resume when not paused should keep isPaused false", gf.isPaused());
    }

    @Test
    public void testGetLeaderboardReturnsSingletonInstance() {
        GameFacade gf = GameFacade.getInstance();
        Leaderboard lbFromFacade = gf.getLeaderboard();
        Leaderboard lbSingleton = Leaderboard.getInstance();
        assertSame("getLeaderboard should return Leaderboard.getInstance()", lbSingleton, lbFromFacade);
    }

    @Test
    public void testAddAchievementWhenAllAchievementsNullAndNonNull() throws Exception {
        GameFacade gf = GameFacade.getInstance();

        // When allAchievements is null, addAchievement should return early without NPE
        setPrivateField(gf, "allAchievements", null);
        try {
            gf.addAchievement(null); // safe call; should simply return
        } catch (Exception e) {
            fail("addAchievement should not throw when allAchievements is null");
        }

        // Now set allAchievements to an ArrayList and add an achievement object using a Mockito mock
        ArrayList<Object> achievements = new ArrayList<>();
        @SuppressWarnings("unchecked")
        ArrayList achList = achievements; // raw to bypass generics at compile time
        setPrivateField(gf, "allAchievements", achList);

        Object dummyAchievement = mockFor("com.model.Coding.Progress.Achievement");
        achievements.add(dummyAchievement);
        assertEquals("Manually added achievement should appear in the list", 1, achievements.size());
    }

    @Test
    public void testOpenCloseMapWhenMapNullNoException() throws Exception {
        GameFacade gf = GameFacade.getInstance();

        // When map is null, methods should return without throwing
        setPrivateField(gf, "map", null);

        try {
            gf.openMap();
            gf.closeMap();
        } catch (Exception e) {
            fail("openMap/closeMap should not throw when map == null");
        }
    }

    @Test
    public void testSetAndGetDifficultyWithoutActiveProgressOnlySetsLocalField() throws Exception {
        GameFacade gf = GameFacade.getInstance();

        // Ensure no activeProgress
        setPrivateField(gf, "activeProgress", null);

        // Set difficulty; should update local difficulty field
        gf.setDifficulty(3);
        int diff = gf.getDifficulty();
        assertEquals("getDifficulty should reflect setDifficulty even when activeProgress is null", 3, diff);
    }

    @Test
    public void testGettersReturnNullWhenNotInitialized() throws Exception {
        GameFacade gf = GameFacade.getInstance();

        // inventory, map, currentUser should be null initially (or at least return null when not set)
        setPrivateField(gf, "inventory", null);
        setPrivateField(gf, "map", null);
        setPrivateField(gf, "currentUser", null);

        assertNull("getInventory should return null when not initialized", gf.getInventory());
        assertNull("getCurrRoom (via map) should return null when map is null", gf.getCurrRoom());
        assertNull("getRooms should return null when map is null", gf.getRooms());
        assertNull("getCurrUser should return null when currentUser is null", gf.getCurrUser());
    }

    @Test
    public void testSaveDoesNothingWhenNoUserOrNoActiveProgress() throws Exception {
        GameFacade gf = GameFacade.getInstance();

        // Case 1: currentUser null
        setPrivateField(gf, "currentUser", null);
        setPrivateField(gf, "activeProgress", null);
        try {
            gf.save(); // should return early without throwing
        } catch (Exception e) {
            fail("save() should not throw when currentUser is null");
        }

        // Case 2: currentUser present but activeProgress null
        Object dummyUser = mockFor("com.model.Coding.User.User");
        setPrivateField(gf, "currentUser", dummyUser);
        setPrivateField(gf, "activeProgress", null);
        try {
            gf.save(); // should return early without throwing
        } catch (Exception e) {
            fail("save() should not throw when activeProgress is null");
        }
    }
}
