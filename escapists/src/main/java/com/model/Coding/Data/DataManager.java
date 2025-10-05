package com.model.Coding.Data;
import java.util.ArrayList;
import java.util.UUID;
import com.model.Coding.User.User;

import com.model.Coding.Progress.Progress;

public class DataManager {
    private static DataManager dataManager;
    private DataLoader dataLoader;
    private DataWriter dataWriter;

    private DataManager(){
        dataLoader = DataLoader.getInstance();
        dataWriter = DataWriter.getInstance();
    }

    public static DataManager getInstance(){
        if (dataManager == null) {
        dataManager = new DataManager();
        }
        return dataManager;
    }

    public ArrayList<User> getUsers(){ 
        return new ArrayList<User>();
    }

    public void addUser(User user){
        
    }

    public void updateUser(User user){

    }

    public Progress loadProgress(UUID progressId){
        return new Progress(); 
    }

    public void saveProgress(Progress progress){
        
    }

}