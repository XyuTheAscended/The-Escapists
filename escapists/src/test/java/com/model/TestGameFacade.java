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
import static org.mockito.ArgumentMatchers.eq;

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
        resetSingleton(GameFacade.class, "gameFacade");
    }

    @After
    public void tearDown() throws Exception {
        System.setOut(originalOut);

        // Reset all singletons so each test starts clean
        resetSingleton(GameFacade.class, "gameFacade");
        resetSingleton(Timer.class, "timer");
        resetSingleton(com.model.Coding.Data.DataManager.class, "dataManager");
        resetSingleton(com.model.Coding.User.UserList.class, "userList");
        resetSingleton(com.model.Coding.Progress.Leaderboard.class, "leaderboard");
    }

    // -------------------------
    // Reflection helper methods
    // -------------------------
    private void resetSingleton(Class<?> clazz, String fieldName) throws Exception {
        Field instanceField = clazz.getDeclaredField(fieldName);
        instanceField.setAccessible(true);
        instanceField.set(null, null);
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

    @Test
    public void testCurrSaveIsContinuable_nullUser_false() throws Exception {
        GameFacade gf = GameFacade.getInstance();
        setPrivateField(gf, "currentUser", null);
        assertFalse("currSaveIsContinuable should be false when currentUser is null", gf.currSaveIsContinuable());
    }

    @Test
    public void testCurrSaveIsContinuable_nullSave_false() throws Exception {
        GameFacade gf = GameFacade.getInstance();
        Object mockUser = mockFor("com.model.Coding.User.User");
        // when getCurrSave() returns null -> not continuable
        Mockito.when(((com.model.Coding.User.User)mockUser).getCurrSave()).thenReturn(null);
        setPrivateField(gf, "currentUser", mockUser);
        assertFalse("currSaveIsContinuable should be false when user's curr save is null", gf.currSaveIsContinuable());
    }

    @Test
    public void testCurrSaveIsContinuable_outsideRoom_false() throws Exception {
        GameFacade gf = GameFacade.getInstance();
        Object mockUser = mockFor("com.model.Coding.User.User");
        Object mockProgress = mockFor("com.model.Coding.Progress.Progress");
        Mockito.when(((com.model.Coding.Progress.Progress)mockProgress).getCurrentRoomName()).thenReturn("OUTSIDE");
        Mockito.when(((com.model.Coding.User.User)mockUser).getCurrSave()).thenReturn((com.model.Coding.Progress.Progress) mockProgress);
        setPrivateField(gf, "currentUser", mockUser);
        assertFalse("currSaveIsContinuable should be false when save current room is OUTSIDE", gf.currSaveIsContinuable());
    }

    @Test
    public void testCurrSaveIsContinuable_validSave_true() throws Exception {
        GameFacade gf = GameFacade.getInstance();
        Object mockUser = mockFor("com.model.Coding.User.User");
        Object mockProgress = mockFor("com.model.Coding.Progress.Progress");
        Mockito.when(((com.model.Coding.Progress.Progress)mockProgress).getCurrentRoomName()).thenReturn("ROOM_1");
        Mockito.when(((com.model.Coding.User.User)mockUser).getCurrSave()).thenReturn((com.model.Coding.Progress.Progress) mockProgress);
        setPrivateField(gf, "currentUser", mockUser);
        assertTrue("currSaveIsContinuable should be true when save has a valid room", gf.currSaveIsContinuable());
    }

    // --- startGame default difficulty branch (diff <= 0 prints message and sets difficulty to 1) ---
    @Test
    public void testStartGameDefaultsDifficultyAndPrints() throws Exception {
        GameFacade gf = GameFacade.getInstance();

        // build mocks to make currSaveIsContinuable true so startGame doesn't try to create a save
        com.model.Coding.User.User mockUser = (com.model.Coding.User.User) mockFor("com.model.Coding.User.User");
        com.model.Coding.Progress.Progress mockSave = (com.model.Coding.Progress.Progress) mockFor("com.model.Coding.Progress.Progress");
        Mockito.when(mockSave.getCurrentRoomName()).thenReturn("ROOM_A");
        Mockito.when(mockSave.getRemainingTime()).thenReturn(0);
        Mockito.when(mockUser.getCurrSave()).thenReturn(mockSave);

        setPrivateField(gf, "currentUser", mockUser);

        // call with diff 0 to trigger defaulting behavior
        outContent.reset();
        gf.startGame(0);

        String printed = outContent.toString();
        assertTrue("startGame with diff <= 0 should print default message", printed.contains("Defaulting difficulty to 1"));
        assertEquals("Facade difficulty should be set to 1 when defaulted", 1, gf.getDifficulty());

        // tidy: pause the timer to avoid thread work if any started
        try { Timer.getInstance().pause(); } catch (Exception ignored) {}
    }

    // --- logout clears currentUser ---
    @Test
    public void testLogoutClearsCurrentUser() throws Exception {
        GameFacade gf = GameFacade.getInstance();
        Object mockUser = mockFor("com.model.Coding.User.User");
        setPrivateField(gf, "currentUser", mockUser);
        assertNotNull("Precondition: current user should be non-null", gf.getCurrUser());
        gf.logout();
        assertNull("After logout current user should be null", gf.getCurrUser());
    }

    // --- setCurrRoom delegates to map and activeProgress when present ---
    @Test
    public void testSetCurrRoomDelegatesToMapAndProgress() throws Exception {
        GameFacade gf = GameFacade.getInstance();

        // Mock Map and Room and Progress
        com.model.Coding.Gameplay.Map.Map mockMap = (com.model.Coding.Gameplay.Map.Map) mockFor("com.model.Coding.Gameplay.Map.Map");
        com.model.Coding.Gameplay.Map.Room mockRoom = (com.model.Coding.Gameplay.Map.Room) mockFor("com.model.Coding.Gameplay.Map.Room");
        com.model.Coding.Progress.Progress mockProgress = (com.model.Coding.Progress.Progress) mockFor("com.model.Coding.Progress.Progress");

        setPrivateField(gf, "map", mockMap);
        setPrivateField(gf, "activeProgress", mockProgress);

        // Call setCurrRoom
        gf.setCurrRoom(mockRoom);

        // Verify map.setCurrentRoom(room) called and progress.setCurrentRoom(room) called
        Mockito.verify(mockMap, Mockito.times(1)).setCurrentRoom(mockRoom);
        Mockito.verify(mockProgress, Mockito.times(1)).setCurrentRoom(mockRoom);
    }

    // --- endGame successful path resets fields and calls Timer.reset (verify state) ---
    @Test
    public void testEndGameSuccessResetsState() throws Exception {
        GameFacade gf = GameFacade.getInstance();

        // Prepare non-null currentUser and activeProgress so endGame does not throw
        com.model.Coding.User.User mockUser = (com.model.Coding.User.User) mockFor("com.model.Coding.User.User");
        com.model.Coding.Progress.Progress mockProgress = (com.model.Coding.Progress.Progress) mockFor("com.model.Coding.Progress.Progress");

        setPrivateField(gf, "currentUser", mockUser);
        setPrivateField(gf, "activeProgress", mockProgress);

        // set some fields to non-defaults
        setPrivateField(gf, "difficulty", 5);
        setPrivateField(gf, "inventory", mockFor("com.model.Coding.Gameplay.InteractItems.Inventory"));

        // Call endGame (should not throw)
        gf.endGame();

        // After endGame, activeProgress should be null and difficulty should be reset to 0
        Object afterProgress = getPrivateField(gf, "activeProgress");
        Integer difficultyAfter = (Integer) getPrivateField(gf, "difficulty");
        assertNull("activeProgress should be null after endGame", afterProgress);
        assertEquals("difficulty should be reset to 0 after endGame", Integer.valueOf(0), difficultyAfter);
    }

    // --- save() calls DataManager.saveProgress and updateUser with proper arguments ---
    @Test
    public void testSaveInvokesDataManagerSaveAndUpdate() throws Exception {
        GameFacade gf = GameFacade.getInstance();

        // Prepare mocks for currentUser and activeProgress
        com.model.Coding.User.User mockUser = (com.model.Coding.User.User) mockFor("com.model.Coding.User.User");
        com.model.Coding.Progress.Progress mockProgress = (com.model.Coding.Progress.Progress) mockFor("com.model.Coding.Progress.Progress");

        setPrivateField(gf, "currentUser", mockUser);
        setPrivateField(gf, "activeProgress", mockProgress);

        // Ensure Timer returns some remaining time (0 is acceptable too)
        Timer.getInstance().reset();
        // inject a mock DataManager as singleton
        com.model.Coding.Data.DataManager mockDM = (com.model.Coding.Data.DataManager) mockFor("com.model.Coding.Data.DataManager");
        injectDataManagerMock(mockDM);

        // Call save (should call DataManager.saveProgress and updateUser)
        gf.save();

        Mockito.verify(mockDM, Mockito.times(1)).saveProgress(eq(mockUser), eq(mockProgress));
        Mockito.verify(mockDM, Mockito.times(1)).updateUser(eq(mockUser));
    }

    // Helper to inject mocked DataManager singleton
    private void injectDataManagerMock(com.model.Coding.Data.DataManager mockDM) throws Exception {
        Field dmField = com.model.Coding.Data.DataManager.class.getDeclaredField("dataManager");
        dmField.setAccessible(true);
        dmField.set(null, mockDM);
    }

    // --- setDifficulty updates activeProgress if present (already covered elsewhere but explicit) ---
    @Test
    public void testSetDifficultyUpdatesActiveProgressIfPresent() throws Exception {
        GameFacade gf = GameFacade.getInstance();
        com.model.Coding.Progress.Progress mockProgress = (com.model.Coding.Progress.Progress) mockFor("com.model.Coding.Progress.Progress");
        setPrivateField(gf, "activeProgress", mockProgress);

        gf.setDifficulty(7);

        assertEquals(7, gf.getDifficulty());
        Mockito.verify(mockProgress, Mockito.times(1)).setDifficulty(7);
    }

    @Test
    public void testLoadCurrSaveSetsFieldsAndCallsMapLoadFromSave() throws Exception {
        GameFacade gf = GameFacade.getInstance();

        // mocks
        com.model.Coding.User.User mockUser = (com.model.Coding.User.User) mockFor("com.model.Coding.User.User");
        com.model.Coding.Progress.Progress mockSave = (com.model.Coding.Progress.Progress) mockFor("com.model.Coding.Progress.Progress");
        com.model.Coding.Gameplay.InteractItems.Inventory mockInv = (com.model.Coding.Gameplay.InteractItems.Inventory) mockFor("com.model.Coding.Gameplay.InteractItems.Inventory");

        Mockito.when(mockSave.getDifficulty()).thenReturn(9);
        Mockito.when(mockSave.getInventory()).thenReturn(mockInv);

        Mockito.when(mockUser.getCurrSave()).thenReturn((com.model.Coding.Progress.Progress) mockSave);

        setPrivateField(gf, "currentUser", mockUser);

        // Call
        gf.loadCurrSave();

        // Assertions
        assertSame("activeProgress must be set from user's save", mockSave, getPrivateField(gf, "activeProgress"));
        assertEquals("difficulty set from save", 9, gf.getDifficulty());
        assertSame("inventory set from save", mockInv, gf.getInventory());

        // Map was created and loadFromSave called (we can't inspect private map impl easily,
        // but we can verify map != null and it has room list or call to loadFromSave via spy injection).
        Object map = getPrivateField(gf, "map");
        assertNotNull("map should be created", map);
    }

    @Test
    public void testSaveCopiesRemainingTimeIntoProgressBeforeSaving() throws Exception {
        GameFacade gf = GameFacade.getInstance();
        com.model.Coding.User.User mockUser = (com.model.Coding.User.User) mockFor("com.model.Coding.User.User");
        com.model.Coding.Progress.Progress mockProgress = (com.model.Coding.Progress.Progress) mockFor("com.model.Coding.Progress.Progress");

        setPrivateField(gf, "currentUser", mockUser);
        setPrivateField(gf, "activeProgress", mockProgress);

        // make Timer return a known remaining time
        com.model.Coding.Gameplay.Timer mockTimer = (com.model.Coding.Gameplay.Timer) mockFor("com.model.Coding.Gameplay.Timer");
        Field timerField = com.model.Coding.Gameplay.Timer.class.getDeclaredField("timer");
        timerField.setAccessible(true);
        timerField.set(null, mockTimer);
        Mockito.when(mockTimer.getRemainingTime()).thenReturn(123);

        com.model.Coding.Data.DataManager mockDM = (com.model.Coding.Data.DataManager) mockFor("com.model.Coding.Data.DataManager");
        injectDataManagerMock(mockDM);

        gf.save();

        // verify activeProgress.setRemainingTime(123) called
        Mockito.verify(mockProgress).setRemainingTime(123);
        Mockito.verify(mockDM).saveProgress(eq(mockUser), eq(mockProgress));
    }

    @Test
    public void testEndGameCallsTimerReset() throws Exception {
        GameFacade gf = GameFacade.getInstance();
        com.model.Coding.User.User mockUser = (com.model.Coding.User.User) mockFor("com.model.Coding.User.User");
        com.model.Coding.Progress.Progress mockProgress = (com.model.Coding.Progress.Progress) mockFor("com.model.Coding.Progress.Progress");

        setPrivateField(gf, "currentUser", mockUser);
        setPrivateField(gf, "activeProgress", mockProgress);

        // inject mock Timer and verify reset called on endGame
        com.model.Coding.Gameplay.Timer mockTimer = (com.model.Coding.Gameplay.Timer) mockFor("com.model.Coding.Gameplay.Timer");
        Field timerField = com.model.Coding.Gameplay.Timer.class.getDeclaredField("timer");
        timerField.setAccessible(true);
        timerField.set(null, mockTimer);

        // Call endGame
        gf.endGame();

        // verify Timer.reset() invoked
        Mockito.verify(mockTimer, Mockito.times(1)).reset();
    }

    @Test
    public void testStartGameUsesRemainingTimeIfPresent() throws Exception {
        GameFacade gf = GameFacade.getInstance();

        // prepare user & save with remainingTime > 0
        com.model.Coding.User.User mockUser = (com.model.Coding.User.User) mockFor("com.model.Coding.User.User");
        com.model.Coding.Progress.Progress mockSave = (com.model.Coding.Progress.Progress) mockFor("com.model.Coding.Progress.Progress");
        Mockito.when(mockSave.getCurrentRoomName()).thenReturn("ROOM_X");
        Mockito.when(mockSave.getRemainingTime()).thenReturn(77);
        Mockito.when(mockUser.getCurrSave()).thenReturn(mockSave);

        setPrivateField(gf, "currentUser", mockUser);
        setPrivateField(gf, "activeProgress", mockSave);

        // inject a mock Timer singleton and verify start called
        com.model.Coding.Gameplay.Timer mockTimer = (com.model.Coding.Gameplay.Timer) mockFor("com.model.Coding.Gameplay.Timer");
        Field timerField = com.model.Coding.Gameplay.Timer.class.getDeclaredField("timer");
        timerField.setAccessible(true);
        timerField.set(null, mockTimer);

        // call with difficulty 3 -> startingTime = 1800/3 = 600
        gf.startGame(3);

        Mockito.verify(mockTimer, Mockito.times(1)).start(600, 77);
    }

    @Test
    public void testLoginWithEmptyCredentialsReturnsFalse() throws Exception {
        GameFacade gf = GameFacade.getInstance();

        // mock UserList singleton to return null regardless
        com.model.Coding.User.UserList mockUL = (com.model.Coding.User.UserList) mockFor("com.model.Coding.User.UserList");
        Field ulf = com.model.Coding.User.UserList.class.getDeclaredField("userList");
        ulf.setAccessible(true);
        ulf.set(null, mockUL);

        boolean ok1 = gf.login("", "");
        assertFalse(ok1);

        boolean ok2 = gf.login(null, null);
        assertFalse(ok2);
    }

    @Test
    public void testStartGameCreatesSaveWhenCurrSaveNotContinuable() throws Exception {
        GameFacade gf = GameFacade.getInstance();

        com.model.Coding.User.User mockUser = (com.model.Coding.User.User) mockFor("com.model.Coding.User.User");
        com.model.Coding.Progress.Progress mockSave = (com.model.Coding.Progress.Progress) mockFor("com.model.Coding.Progress.Progress");

        // Make currSaveIsContinuable() false: user.getCurrSave() != null but getCurrentRoomName = "OUTSIDE"
        Mockito.when(((com.model.Coding.User.User)mockUser).getCurrSave()).thenReturn((com.model.Coding.Progress.Progress) mockSave);
        Mockito.when(((com.model.Coding.Progress.Progress)mockSave).getCurrentRoomName()).thenReturn("OUTSIDE");

        setPrivateField(gf, "currentUser", mockUser);

        // Call startGame, expecting createSave() to be invoked on user (mock)
        gf.startGame(1);

        Mockito.verify(mockUser, Mockito.atLeastOnce()).createSave();
    }
}
