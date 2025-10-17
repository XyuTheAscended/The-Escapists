package com.model.Coding;

import java.util.UUID;
import java.util.ArrayList;

import com.model.Coding.Data.DataManager;
import com.model.Coding.Data.DataLoader;
import com.model.Coding.Data.DataWriter;
import com.model.Coding.Progress.Progress;
import com.model.Coding.User.User;

public class Main {

    public static void main(String[] args) {
        DataManager manager = DataManager.getInstance();
        DataLoader loader = DataLoader.getInstance();
        DataWriter writer = DataWriter.getInstance();

        System.out.println("=== TEST START ===");
    
        User testUser = new User("Tester", "password123");
        manager.addUser(testUser);
        System.out.println("User added: " + testUser.getUserName());

        Progress progress = new Progress();
        progress.setDifficulty(2);
        progress.setRemainingTime(300);
        UUID progressId = progress.getProgressId();
        System.out.println("Generated UUID: " + progressId);

        testUser.addSave(progress);
        System.out.println("Save added to user " + testUser.getUserName());

        manager.saveProgress(testUser, progress);
        System.out.println("GameFacade save method simulated..");

        System.out.println("\nLoaded progressId from JSON.");
        Progress loadedProgress = loader.loadProgress(progressId);

        if (loadedProgress != null) {
            System.out.println("Progress loaded.");
            System.out.println("Difficulty: " + loadedProgress.getDifficulty());
            System.out.println("Remaining Time: " + loadedProgress.getRemainingTime());
        } else {
            System.out.println("Failed to load progress");
        }

        ArrayList<User> allUsers = manager.getUsers();
        System.out.println("\nUsers Loaded:");
        for (User u : allUsers) {
            System.out.println("- " + u.getUserName() + " | Saves: " + u.getSaves().size());
        }
    }
}
