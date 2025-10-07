package com.model.Coding.Data;

import java.util.ArrayList;
import java.util.UUID;

import com.model.Coding.User.User;
import com.model.Coding.Progress.Progress;

public class DataManager {
    private static DataManager dataManager;
    private DataLoader dataLoader;
    private DataWriter dataWriter;

    private DataManager() {
        dataLoader = DataLoader.getInstance();
        dataWriter = DataWriter.getInstance();
    }

    public static DataManager getInstance() {
        if (dataManager == null) {
            dataManager = new DataManager();
        }
        return dataManager;
    }

    public ArrayList<User> getUsers() {
        return dataLoader.getUsers();
    }

    public void addUser(User user) {
        dataWriter.addUser(user);
    }

    public void updateUser(User user) {
    }

    public Progress loadProgress(UUID progressId) {
        return dataLoader.loadProgress(progressId);
    }

    public void saveProgress(User user, Progress progress) {
        if (user == null || progress == null) {
            System.out.println("Cannot save null user or progress.");
            return;
        }
        dataWriter.saveProgress(user, progress);
    }
}
