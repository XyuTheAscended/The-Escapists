package com.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.model.Coding.User.User;
import com.model.Coding.User.UserList;

import java.util.ArrayList;

public class UserTests {
    private UserList userList;

    @Before
    public void setUp() {
        userList = UserList.getInstance();
    }

    // =======================
    // User Creation Tests
    // =======================
    
    @Test
    public void testCreateUserNormal() {
        User user = userList.createUser("TestUser", "password123");
        assertNotNull(user);
        assertEquals("TestUser", user.getUserName());
        assertEquals("password123", user.getPassword());
        assertNotNull(user.getSaves());
        assertEquals(0, user.getSaves().size());
    }

    @Test
    public void testCreateUserNullUsername() {
        User user = userList.createUser(null, "pass");
        assertNotNull(user);
        assertNull(user.getUserName());
        assertEquals("pass", user.getPassword());
    }

    @Test
    public void testCreateUserNullPassword() {
        User user = userList.createUser("NullPassUser", null);
        assertNotNull(user);
        assertEquals("NullPassUser", user.getUserName());
        assertNull(user.getPassword());
    }

    @Test
    public void testCreateUserEmptyFields() {
        User user1 = userList.createUser("", "pass");
        User user2 = userList.createUser("User2", "");
        assertEquals("", user1.getUserName());
        assertEquals("pass", user1.getPassword());
        assertEquals("User2", user2.getUserName());
        assertEquals("", user2.getPassword());
    }

    // =======================
    // UserList Tests
    // =======================
    
    @Test
    public void testCheckAvailabilityReturnsFalseIfExists() {
        userList.createUser("TakenUser", "pass");
        assertFalse(userList.checkAvailability("TakenUser"));
    }

    @Test
    public void testCheckAvailabilityReturnsTrueIfNotExists() {
        assertTrue(userList.checkAvailability("FreeUser"));
    }

    @Test
    public void testGetUserByNameExists() {
        User user = userList.createUser("FindMe", "p");
        User retrieved = userList.getUser("FindMe");
        assertNotNull(retrieved);
        assertEquals("FindMe", retrieved.getUserName());
    }

    @Test
    public void testGetUserByNamePassword() {
        User user = userList.createUser("AuthUser", "secret");
        User retrieved = userList.getUser("AuthUser", "secret");
        assertNotNull(retrieved);
        assertEquals("AuthUser", retrieved.getUserName());
        assertEquals("secret", retrieved.getPassword());
    }

    @Test
    public void testGetUserReturnsNullIfNotFound() {
        assertNull(userList.getUser("NonExistent"));
        assertNull(userList.getUser("NonExistent", "pw"));
    }

    @Test
    public void testCheckAvailabilityNullUsername() {
        assertFalse(userList.checkAvailability(null));
    }
}
