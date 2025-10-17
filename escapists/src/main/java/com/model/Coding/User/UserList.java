package com.model.Coding.User;
import java.util.ArrayList;

import com.model.Coding.Data.DataManager;


public class UserList {
    private ArrayList<User> users;
    private static UserList userList; 

    private static final DataManager dataManager; // made this property so i dont have to keep calling getInstance for this thing
    static {
        dataManager = DataManager.getInstance();
    }

    private UserList() {
        users = dataManager.getUsers();
    }

    public static UserList getInstance(){
        if (userList == null) {
            userList = new UserList();
        }
        return userList;
    }

    /**
     * Creates a new user by using user class and data manager for syncing newly created user to data base
     * no checks for username availability here. they're expected to be done before this method is called
     * @param userName
     * @param password
     * @return User that was created
     */
    public User createUser(String userName, String password) {
        User newUser = new User(userName, password); 
        dataManager.addUser(newUser);
        users.add(newUser);
        return newUser;
    }

    public ArrayList<User> getUsers(){
        return users;
    }

    public User getUser(String userName) {
        for (User user : users) {
            if (user.getUserName().equals(userName)) 
                return user;
        }
        return null; 
    }

    public User getUser(String userName, String password) {
        for (User user : users) {
            if (user.getUserName().equals(userName) && user.getPassword().equals(password))
                return user;
        }
        return null;
    }

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
