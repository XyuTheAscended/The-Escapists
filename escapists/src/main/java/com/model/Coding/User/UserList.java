package com.model.Coding.User;
import java.util.ArrayList;


public class UserList {
    private ArrayList<User> users;
    private static UserList userList; 

    private UserList(){
        users = new ArrayList<User>();
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
        return new ArrayList<User>();
    }

    public User getUser(String userName){
        return new User(userName, "password");
    }

    public boolean checkAvailability(String desiredUserName){
        return false;
    }

}
