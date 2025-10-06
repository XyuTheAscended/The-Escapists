package com.model.Coding.User;
import java.util.ArrayList;

import com.model.Coding.Data.DataManager;


public class UserList {
    private ArrayList<User> users;
    private static UserList userList; 

    private UserList() {
        DataManager dataMan = DataManager.getInstance();
        users = dataMan.getUsers();
    }

    public UserList getInstance(){
        if (userList == null) {
            userList = new UserList();
        }
        return userList;
    }

    public void createUser(User user){

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

    public boolean checkAvailability(String desiredUserName) {
        // not efficient at all if we were to have a bunch of users but cant do anything about it cus we dont use SQL
        for (User user : users) {
            if (user.getUserName().equals(desiredUserName))
                return false;
        }
        return true;
    }

}
