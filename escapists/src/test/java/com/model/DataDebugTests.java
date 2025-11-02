package com.model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.model.Coding.Data.DataLoader;
import com.model.Coding.Data.DataWriter;
import com.model.Coding.Data.DataManager;
import com.model.Coding.User.User;
import com.model.Coding.User.UserList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class DataDebugTests {

    private DataManager dataManager;
    private UserList userList;

    @Before
    public void setup() {
        dataManager = DataManager.getInstance();
        userList = UserList.getInstance();

    // Ensure the test user exists in JSON
    User testUser = userList.createUser("TestUser1", "password123");
    dataManager.addUser(testUser); // adds to JSON or test JSON file
    }

    // =======================
    // DataLoader Tests
    // =======================
    
    @Test
    public void testLoadUsersNotNull() {
        ArrayList<User> users = dataManager.getUsers();
        
        assertNotNull("Loaded users should not be null", users);
        assertTrue("User list should have at least one user", users.size() > 0);
    }
    
    @Test
    public void testLoadUserHasRequiredFields() {
        ArrayList<User> users = dataManager.getUsers();
        assertFalse("User list should not be empty", users.isEmpty()); // extra safety

        User user = users.get(0);

        assertNotNull("Username should not be null", user.getUserName());
        assertNotNull("Password should not be null", user.getPassword());
        assertNotNull("Saves list should not be null", user.getSaves());
        assertNotNull("Completion times map should not be null", user.getCompletionTimesHashmap());
    }


    @Test
    public void testLoadProgressValidUUID() {
        User user = userList.createUser("ProgressTest", "pass");
        user.createSave();
        user.pushSaves();

        UUID saveId = user.getSaves().get(0).getProgressId();
        assertNotNull("Progress ID should exist", saveId);

        assertNotNull("DataManager should load progress", dataManager.loadProgress(saveId));
    }

    @Test
    public void testLoadProgressInvalidUUIDReturnsNull() {
        UUID fakeId = UUID.randomUUID();
        assertNull("Loading nonexistent progress should return null", dataManager.loadProgress(fakeId));
    }

    // =======================
    // DataWriter Tests
    // =======================
    
    @Test
    public void testAddUserAndUpdate() {
        User user = userList.createUser("WriterTest1", "pass");
        dataManager.addUser(user);
        dataManager.updateUser(user);

        ArrayList<User> users = dataManager.getUsers();
        boolean found = users.stream()
                            .anyMatch(u -> "WriterTest1".equals(u.getUserName()));
        assertTrue("User should be present after update", found);
    }

    @Test
    public void testCreateCertificateDoesNotThrow() {
        try {
            dataManager.createCertificate(2, 3, 100);
        } catch (Exception e) {
            fail("createCertificate should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void testSaveProgressHandlesNull() {
        try {
            dataManager.getInstance().addUser(null);
            dataManager.saveProgress(null, null); // Should not throw
        } catch (Exception e) {
            fail("saveProgress with nulls should not throw: " + e.getMessage());
        }
    }

    @Test
    public void testSaveProgressValidUser() {
        User user = userList.createUser("SaveTestUser", "pass");
        user.createSave();
        try {
            dataManager.saveProgress(user, user.getSaves().get(0));
        } catch (Exception e) {
            fail("Saving progress for valid user should not throw: " + e.getMessage());
        }
    }

    // =======================
    // DataManager Tests
    // =======================
    
    @Test
    public void testAddUserAndRetrieve() {
        String username = "DebugUser1";
        String password = "pass123";

        User newUser = new User(username, password);
        dataManager.addUser(newUser);

        User retrieved = null;
        ArrayList<User> users = dataManager.getUsers();
        for (User u : users) {
            if (u.getUserName().equals(username)) {
                retrieved = u;
                break;
            }
        }

        assertNotNull("User should be retrievable from DataManager", retrieved);
        assertEquals("Username should match", username, retrieved.getUserName());
        assertEquals("Password should match", password, retrieved.getPassword());
    }

    @Test
    public void testGetUsersNotEmpty() {
        ArrayList<User> users = dataManager.getUsers();
        assertNotNull("User list should not be null", users);
        assertTrue("User list should have at least one entry", users.size() > 0);
    }

    @Test
    public void testUpdateUserWithNullDoesNotThrow() {
        try {
            dataManager.updateUser(null);
        } catch (Exception e) {
            fail("updateUser with null should not throw: " + e.getMessage());
        }
    }
}
