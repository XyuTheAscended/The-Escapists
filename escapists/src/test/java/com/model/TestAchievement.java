package com.model;

import com.model.Coding.Progress.Achievement;
import com.model.Coding.User.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Tests for com.model.Coding.Progress.Achievement (non-getter/setter behavior)
 */
public class TestAchievement {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;

    @Before
    public void setUp() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void testConstructorInitializesCollectionsAndUser() {
        User mockUser = Mockito.mock(User.class);
        Achievement ach = new Achievement(mockUser);

        assertNotNull("userAchievements should be initialized", ach.getUserAchievements());
        assertTrue("userAchievements should be empty initially", ach.getUserAchievements().isEmpty());
    }

    @Test
    public void testAddAchievementAddsNewAchievementAndPrintsMessage() {
        User mockUser = Mockito.mock(User.class);
        when(mockUser.getUserName()).thenReturn("Alice");

        Achievement ach = new Achievement(mockUser);
        ach.addAchievement("FirstWin", "Win the first level");

        ArrayList<com.model.Coding.Progress.Achievement> list = ach.getUserAchievements();
        assertEquals("One achievement should have been added", 1, list.size());

        com.model.Coding.Progress.Achievement stored = list.get(0);
        assertEquals("Stored achievement name should match", "FirstWin", stored.getName());
        assertEquals("Stored achievement description should match", "Win the first level", stored.getDescription());

        String printed = outContent.toString();
        assertTrue("Should print a message indicating the achievement was added",
                printed.contains("Achievement added for Alice: FirstWin"));
    }

    @Test
    public void testAddAchievementDuplicateIsIgnoredAndPrintsAlreadyEarned() {
        User mockUser = Mockito.mock(User.class);
        when(mockUser.getUserName()).thenReturn("Bob");

        Achievement ach = new Achievement(mockUser);
        ach.addAchievement("Treasure", "Find the hidden treasure");

        // attempt to add the same achievement again
        outContent.reset();
        ach.addAchievement("Treasure", "Find the hidden treasure");

        ArrayList<com.model.Coding.Progress.Achievement> list = ach.getUserAchievements();
        assertEquals("Duplicate add should not increase list size", 1, list.size());

        String printed = outContent.toString();
        assertTrue("Should print 'Achievement already earned' message", printed.contains("Achievement already earned: Treasure"));
    }

    @Test
    public void testAddAchievementIsCaseInsensitiveWhenCheckingDuplicates() {
        User mockUser = Mockito.mock(User.class);
        when(mockUser.getUserName()).thenReturn("Carol");

        Achievement ach = new Achievement(mockUser);
        ach.addAchievement("Gold", "Get gold medal");

        // attempt with different case
        outContent.reset();
        ach.addAchievement("gold", "Get gold medal");

        ArrayList<com.model.Coding.Progress.Achievement> list = ach.getUserAchievements();
        assertEquals("Case-insensitive duplicate should not be added", 1, list.size());

        String printed = outContent.toString();
        assertTrue("Should print already-earned message for case-insensitive duplicate", printed.contains("Achievement already earned: gold"));
    }

    @Test
    public void testGetUserAchievementsReturnsCopyNotBackingList() {
        User mockUser = Mockito.mock(User.class);
        Achievement ach = new Achievement(mockUser);

        ach.addAchievement("One", "desc");
        ArrayList<com.model.Coding.Progress.Achievement> returned = ach.getUserAchievements();
        int before = returned.size();

        // mutate returned copy
        returned.clear();
        // original should remain unchanged
        ArrayList<com.model.Coding.Progress.Achievement> after = ach.getUserAchievements();
        assertEquals("Mutating returned list should not change internal list", before, after.size());
    }

    // --- Null-handling tests ---

    @Test
    public void testConstructorWithNullUserDoesNotThrow() {
        try {
            Achievement ach = new Achievement(null);
            assertNotNull("Constructor should still create Achievement object", ach);
            assertTrue("userAchievements should still be initialized", ach.getUserAchievements().isEmpty());
        } catch (Exception e) {
            fail("Constructor should not throw even if user is null: " + e.getMessage());
        }
    }

    @Test
    public void testAddAchievementWithNullNameHandledGracefully() {
        User mockUser = Mockito.mock(User.class);
        when(mockUser.getUserName()).thenReturn("NullTester");

        Achievement ach = new Achievement(mockUser);
        // Expect no exception, though output may contain 'null'
        try {
            ach.addAchievement(null, "Description");
        } catch (Exception e) {
            fail("addAchievement(null, ...) should not throw: " + e.getMessage());
        }

        // Verify achievement added with null name
        ArrayList<Achievement> list = ach.getUserAchievements();
        assertEquals(1, list.size());
        assertNull("Name should be stored as null", list.get(0).getName());
    }

    @Test
    public void testAddAchievementWithNullDescriptionHandledGracefully() {
        User mockUser = Mockito.mock(User.class);
        when(mockUser.getUserName()).thenReturn("NullTester");

        Achievement ach = new Achievement(mockUser);
        try {
            ach.addAchievement("NullDescription", null);
        } catch (Exception e) {
            fail("addAchievement(..., null) should not throw: " + e.getMessage());
        }

        ArrayList<Achievement> list = ach.getUserAchievements();
        assertEquals(1, list.size());
        assertNull("Description should be stored as null", list.get(0).getDescription());
    }

    @Test
    public void testAddAchievementWhenUserIsNullStillAddsAchievement() {
        // This simulates Achievement constructed with null user
        Achievement ach = new Achievement(null);

        // addAchievement should not throw NPE when printing message (defensive test)
        try {
            ach.addAchievement("NullUserAch", "Testing null user");
        } catch (Exception e) {
            fail("addAchievement should not throw even if user is null: " + e.getMessage());
        }

        ArrayList<Achievement> list = ach.getUserAchievements();
        assertEquals(1, list.size());
        assertEquals("NullUserAch", list.get(0).getName());
    }

    @Test(expected = NullPointerException.class)
    public void testAddingNullNameTwiceThrowsNPE() {
        User mockUser = Mockito.mock(User.class);
        when(mockUser.getUserName()).thenReturn("NullUser");
        Achievement ach = new Achievement(mockUser);

        // First add with null name — allowed (list was empty)
        ach.addAchievement(null, "desc");

        // Second add with null name — the duplicate-check uses equalsIgnoreCase and will throw NPE
        ach.addAchievement(null, "desc2");
    }
    @Test
    public void testStoredAchievementIsDistinctObjectButSharesUser() {
        User mockUser = Mockito.mock(User.class);
        when(mockUser.getUserName()).thenReturn("SharedUser");
        Achievement ach = new Achievement(mockUser);

        ach.addAchievement("UniqueOne", "desc1");
        ArrayList<Achievement> list = ach.getUserAchievements();
        assertEquals(1, list.size());

        Achievement stored = list.get(0);
        assertNotSame("Stored Achievement should be a different object than parent", ach, stored);
        // Can't access private user directly, but we can test that adding another achievement uses the same user
        // by checking the printed username (or by using reflection to read the private 'user' field)
        // For a robust assertion use reflection:
        try {
            java.lang.reflect.Field f = Achievement.class.getDeclaredField("user");
            f.setAccessible(true);
            Object storedUser = f.get(stored);
            assertSame("Stored achievement should reference the same user object", mockUser, storedUser);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Reflection failed: " + e.getMessage());
        }
    }
}