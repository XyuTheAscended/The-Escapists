package com.model.Coding.User;
import java.util.ArrayList;

import com.model.Coding.Data.DataManager;

/**
 * List of users
 * @author
 */
public class UserList {
    private ArrayList<User> users;
    private static UserList userList; 

    private static final DataManager dataManager; // made this property so i dont have to keep calling getInstance for this thing
    static {
        dataManager = DataManager.getInstance();
    }

    /**
     * Initializes ArrayList of users via dataManager
     */
    private UserList() {
        users = dataManager.getUsers();
    }

    /**
     * Gets instance of UserList
     * @return UserList object
     */
    public static UserList getInstance(){
        if (userList == null) {
            userList = new UserList();
        }
        return userList;
    }

    /**
     * Creates a new user by using user class and data manager for syncing newly created user to data base
     * no checks for username availability here. they're expected to be done before this method is called
     * @param userName User's username
     * @param password User's password
     * @return User that was created
     */
    public User createUser(String userName, String password) {
        User newUser = new User(userName, password); 
        dataManager.addUser(newUser);
        users.add(newUser);
        return newUser;
    }

    /**
     * Gets ArrayList of users
     * @return ArrayList of users
     */
    public ArrayList<User> getUsers(){
        return users;
    }

    /**
     * Gets a certain user if it exists
     * @param userName Username of the desired user
     * @return A user if found, null if not.
     */
    public User getUser(String userName) {
        for (User user : users) {
            if (user.getUserName().equals(userName)) 
                return user;
        }
        return null; 
    }

    /**
     * Gets use using both their username and password
     * @param userName Username of the desired user
     * @param password Password of the desired user
     * @return A user if found, null if not
     */
    public User getUser(String userName, String password) {
        for (User user : users) {
            if (user.getUserName().equals(userName) && user.getPassword().equals(password))
                return user;
        }
        return null;
    }

    /**
     * Checks if the username is available for use
     * @param desiredUserName Username being checked
     * @return True if it's available, false if not
     */
    public boolean checkAvailability(String desiredUserName) {
        // not efficient at all if we were to have a bunch of users but cant do anything about it cus we dont use SQL
        if (desiredUserName == null) return false; 
        for (User user : users) {
            if (user.getUserName().equals(desiredUserName))
                return false;
        }
        return true;
    }
}
