package com.model;

import com.model.Coding.Progress.Leaderboard;
import com.model.Coding.User.User;
import com.model.Coding.User.UserList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * JUnit4 tests for com.model.Coding.Progress.Leaderboard
 */
public class TestLeaderboard {

    @Before
    public void setUp() throws Exception {
        // Ensure we start with a fresh Leaderboard singleton
        resetSingleton(Leaderboard.class, "leaderboard");

        // Reset UserList singleton as well (we'll inject a mock in tests)
        resetSingleton(UserList.class, "userList");
    }

    @After
    public void tearDown() throws Exception {
        // Clean singletons after each test to avoid cross-test leakage
        resetSingleton(Leaderboard.class, "leaderboard");
        resetSingleton(UserList.class, "userList");
    }

    // -------------------------
    // Reflection helpers
    // -------------------------
    private void resetSingleton(Class<?> clazz, String fieldName) throws Exception {
        Field instanceField = clazz.getDeclaredField(fieldName);
        instanceField.setAccessible(true);
        instanceField.set(null, null);
    }

    private void injectUserListMock(UserList mock) throws Exception {
        Field f = UserList.class.getDeclaredField("userList");
        f.setAccessible(true);
        f.set(null, mock);
    }

    // -------------------------
    // Tests
    // -------------------------

    @Test
    public void testGetInstanceReturnsSingleton() {
        Leaderboard a = Leaderboard.getInstance();
        Leaderboard b = Leaderboard.getInstance();
        assertNotNull(a);
        assertSame("getInstance should return same singleton instance", a, b);
    }

    @Test
    public void testGetFormattedOrderedTimes_emptyUserList_returnsEmptyList() throws Exception {
        Leaderboard lb = Leaderboard.getInstance();

        // Mock UserList to return empty users list
        UserList mockUL = Mockito.mock(UserList.class);
        Mockito.when(mockUL.getUsers()).thenReturn(new ArrayList<User>());
        injectUserListMock(mockUL);

        ArrayList<String> formatted = lb.getFormattedOrderedTimes(1);
        assertNotNull(formatted);
        assertTrue("formatted list should be empty when there are no users", formatted.isEmpty());
    }

    @Test
    public void testGetFormattedOrderedTimes_excludesUsersWithNoTimes() throws Exception {
        Leaderboard lb = Leaderboard.getInstance();

        // userA has no times (returns null)
        User userA = Mockito.mock(User.class);
        Mockito.when(userA.getUserName()).thenReturn("Alice");
        Mockito.when(userA.getCompletionTimes(2)).thenReturn(null);

        // userB has times
        User userB = Mockito.mock(User.class);
        Mockito.when(userB.getUserName()).thenReturn("Bob");
        // Bob's times for difficulty 2 are [300, 250] -> best is 250
        Mockito.when(userB.getCompletionTimes(2)).thenReturn(new ArrayList<Integer>(Arrays.asList(300, 250)));

        UserList mockUL = Mockito.mock(UserList.class);
        Mockito.when(mockUL.getUsers()).thenReturn(new ArrayList<User>(Arrays.asList(userA, userB)));
        injectUserListMock(mockUL);

        ArrayList<String> formatted = lb.getFormattedOrderedTimes(2);

        // Only Bob should appear
        assertEquals(1, formatted.size());
        assertTrue(formatted.get(0).contains("Bob"));
        // 250 seconds = 00:04:10
        assertTrue("should format seconds to HH:MM:SS", formatted.get(0).endsWith("00:04:10"));
    }

    @Test
    public void testGetFormattedOrderedTimes_ordersByBestTimeAndFormatsProperly() throws Exception {
        Leaderboard lb = Leaderboard.getInstance();

        // Create three users with best times:
        // Charlie -> best 90 (00:01:30)
        User charlie = Mockito.mock(User.class);
        Mockito.when(charlie.getUserName()).thenReturn("Charlie");
        Mockito.when(charlie.getCompletionTimes(3)).thenReturn(new ArrayList<Integer>(Arrays.asList(120, 90)));

        // Delta -> best 3605 (01:00:05)
        User delta = Mockito.mock(User.class);
        Mockito.when(delta.getUserName()).thenReturn("Delta");
        Mockito.when(delta.getCompletionTimes(3)).thenReturn(new ArrayList<Integer>(Arrays.asList(4000, 3605)));

        // Echo -> best 3661 (01:01:01)
        User echo = Mockito.mock(User.class);
        Mockito.when(echo.getUserName()).thenReturn("Echo");
        Mockito.when(echo.getCompletionTimes(3)).thenReturn(new ArrayList<Integer>(Arrays.asList(3661)));

        UserList mockUL = Mockito.mock(UserList.class);
        Mockito.when(mockUL.getUsers()).thenReturn(new ArrayList<User>(Arrays.asList(charlie, delta, echo)));
        injectUserListMock(mockUL);

        ArrayList<String> formatted = lb.getFormattedOrderedTimes(3);

        // Expect order: Charlie (90), Delta (3605), Echo (3661)
        assertEquals(3, formatted.size());
        assertTrue("1st entry should be Charlie", formatted.get(0).startsWith("1. Charlie: "));
        assertTrue("Charlie formatted as 00:01:30", formatted.get(0).endsWith("00:01:30"));

        assertTrue("2nd entry should be Delta", formatted.get(1).startsWith("2. Delta: "));
        assertTrue("Delta formatted as 01:00:05", formatted.get(1).endsWith("01:00:05"));

        assertTrue("3rd entry should be Echo", formatted.get(2).startsWith("3. Echo: "));
        assertTrue("Echo formatted as 01:01:01", formatted.get(2).endsWith("01:01:01"));
    }

    @Test
    public void testGetFormattedOrderedTimes_tiePreservesUserListOrder() throws Exception {
        Leaderboard lb = Leaderboard.getInstance();

        // Two users with identical best times (e.g. 120 seconds -> 00:02:00)
        User first = Mockito.mock(User.class);
        Mockito.when(first.getUserName()).thenReturn("FirstUser");
        Mockito.when(first.getCompletionTimes(4)).thenReturn(new ArrayList<Integer>(Arrays.asList(120)));

        User second = Mockito.mock(User.class);
        Mockito.when(second.getUserName()).thenReturn("SecondUser");
        Mockito.when(second.getCompletionTimes(4)).thenReturn(new ArrayList<Integer>(Arrays.asList(120)));

        // Important: preserve insertion order in the users list (first then second)
        UserList mockUL = Mockito.mock(UserList.class);
        Mockito.when(mockUL.getUsers()).thenReturn(new ArrayList<User>(Arrays.asList(first, second)));
        injectUserListMock(mockUL);

        ArrayList<String> formatted = lb.getFormattedOrderedTimes(4);

        // Both users should appear and because of stable sort, FirstUser should be listed before SecondUser
        assertEquals(2, formatted.size());
        assertTrue("1st entry should be FirstUser", formatted.get(0).startsWith("1. FirstUser: "));
        assertTrue("2nd entry should be SecondUser", formatted.get(1).startsWith("2. SecondUser: "));
        // verify formatting (120 seconds = 00:02:00)
        assertTrue(formatted.get(0).endsWith("00:02:00"));
        assertTrue(formatted.get(1).endsWith("00:02:00"));
    }

    @Test
    public void testGetFormattedOrderedTimes_zeroSecondsFormatsToZeros() throws Exception {
        Leaderboard lb = Leaderboard.getInstance();

        User u = Mockito.mock(User.class);
        Mockito.when(u.getUserName()).thenReturn("ZeroUser");
        Mockito.when(u.getCompletionTimes(1)).thenReturn(new ArrayList<Integer>(Arrays.asList(0)));

        UserList mockUL = Mockito.mock(UserList.class);
        Mockito.when(mockUL.getUsers()).thenReturn(new ArrayList<User>(Arrays.asList(u)));
        injectUserListMock(mockUL);

        ArrayList<String> formatted = lb.getFormattedOrderedTimes(1);
        assertEquals(1, formatted.size());
        assertTrue(formatted.get(0).endsWith("00:00:00"));
        assertTrue(formatted.get(0).contains("ZeroUser"));
    }

    @Test
    public void testGetFormattedOrderedTimes_differentDifficultiesReturnDifferentResults() throws Exception {
        Leaderboard lb = Leaderboard.getInstance();

        User u = Mockito.mock(User.class);
        Mockito.when(u.getUserName()).thenReturn("MultiUser");
        Mockito.when(u.getCompletionTimes(1)).thenReturn(new ArrayList<Integer>(Arrays.asList(100)));
        Mockito.when(u.getCompletionTimes(2)).thenReturn(new ArrayList<Integer>(Arrays.asList(200)));

        UserList mockUL = Mockito.mock(UserList.class);
        Mockito.when(mockUL.getUsers()).thenReturn(new ArrayList<User>(Arrays.asList(u)));
        injectUserListMock(mockUL);

        ArrayList<String> f1 = lb.getFormattedOrderedTimes(1);
        ArrayList<String> f2 = lb.getFormattedOrderedTimes(2);

        assertEquals(1, f1.size());
        assertEquals(1, f2.size());
        assertTrue(f1.get(0).endsWith("00:01:40")); // 100s
        assertTrue(f2.get(0).endsWith("00:03:20")); // 200s
    }
}
