package com.model.Coding.Data;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.model.Coding.Progress.Progress;
import com.model.Coding.User.User;
import com.model.Coding.Data.DataLoader;
import com.model.Coding.Gameplay.Map.Room;

/**
 * Coordinates data loading and writing operations.
 * @author Liam and Jeffen
 */
public class DataManager {

    private static DataManager dataManager;
    private DataLoader dataLoader;
    private DataWriter dataWriter;

    /**
     * Constructor that creates datamanager and initializes the classes that it is a facade for
     */
    private DataManager() {
        dataLoader = DataLoader.getInstance();
        dataWriter = DataWriter.getInstance();
    }

    /**
     * Returns instance.
     *
     * @return DataManager instance
     */
    public static DataManager getInstance() {
        if (dataManager == null) {
            dataManager = new DataManager();
        }
        return dataManager;
    }

    /**
     * Loads all users via DataLoader.
     *
     * @return list of users
     */
    public ArrayList<User> getUsers() {
        return dataLoader.getUsers();
    }

    /**
     * Adds user to persistent storage.
     *
     * @param user user to add
     */
    public void addUser(User user) {
        dataWriter.addUser(user);
    }

    /**
     * Loads progress by ID.
     *
     * @param progressId progress identifier
     * @return progress object or null if not found
     */
    public Progress loadProgress(UUID progressId) {
        if (progressId == null) {
            System.out.println("Cannot load null progress");
            return null;
        }
        return dataLoader.loadProgress(progressId);
    }

    /**
     * Loads all rooms via DataLoader.
     *
     * @return list of rooms
     */
    public ArrayList<Room> loadRooms() {
        return dataLoader.loadRooms();
    }

   

    /**
     * Saves all rooms progress via DataWriter.
     * Originally was using copy and pasted code from data writer, but then we just commented it all out
     * and used the data writer instead
     */
    private static final String USER_FILE = "escapists/src/main/java/com/model/Coding/json/users.json";

    @SuppressWarnings("unchecked")
    public void saveProgress(User user, Progress progress) {
        DataWriter.getInstance().saveProgress(user, progress);

        // if (user == null || progress == null) {
        //     System.out.println("Cannot save progress: null user or progress");
        //     return;
        // }
        // String filePath = USER_FILE;
        // JSONParser parser = new JSONParser();

        // try {
        //     File file = new File(filePath);
        //     JSONObject root;
        //     JSONArray usersArray;

        //     /* Read JSON file  */
        //     if (file.exists() && file.length() > 0) {
        //         FileReader reader = new FileReader(file);
        //         root = (JSONObject) parser.parse(reader);
        //         reader.close();
        //         usersArray = (JSONArray) root.get("users");

        //     } else {
        //         System.out.println("No user file -- cannot save.");
        //         return;
        //     }

        //     /* Find user in JSON  */
        //     for (Object userObj : usersArray) {
        //         JSONObject userJson = (JSONObject) userObj;
        //         if (user.getUserName().equals(userJson.get("userName"))) {
        //             JSONArray savesArray = (JSONArray) userJson.get("saves");
        //             if (savesArray == null) {
        //                 savesArray = new JSONArray();
        //                 userJson.put("saves", savesArray);
        //             }

        //             boolean saveFound = false;
        //             for (int i = 0; i < savesArray.size(); i++) {
        //                 JSONObject saveJson = (JSONObject) savesArray.get(i);
        //                 String idStr = (String) saveJson.get("progressId");
        //                 if (idStr != null && idStr.equals(progress.getProgressId().toString())) {
        //                     changeCurrSaveJSON(saveJson, progress);
        //                     saveFound = true;
        //                     break;
        //                 }
        //             }

        //             if (!saveFound) {
        //                 JSONObject newSave = new JSONObject();
        //                 changeCurrSaveJSON(newSave, progress);
        //                 savesArray.add(newSave);

        //             }

        //             /* Update current save pointer */
        //             userJson.put("currSave", progress.getProgressId().toString());

        //             /* Write back to file */
        //             FileWriter writer = new FileWriter(filePath);
        //             root.put("users", usersArray);
        //             writer.write(root.toJSONString());
        //             writer.flush();
        //             writer.close();
        //             System.out.println("Progress saved successfully for user: " + user.getUserName());
        //             return;

        //         }

        //     }

        //     System.out.println("User not found in JSON -- cannot save progress.");

        // } catch (Exception e) {
        //     e.printStackTrace();

        // }

    }

    /**
     * Wrapper method for updating users using data writer
     * @param user
     */
    public void updateUser(User user) {
        DataWriter.getInstance().updateUser(user);
    }

    /**
     * Wrapper method for calling data writer's certification functionality
     * @param difficulty
     * @param hintsUsed
     * @param score
     */
    public void createCertificate(int difficulty, int hintsUsed, int score) {
        DataWriter.getInstance().createCertificate(difficulty, hintsUsed, score);
    }
}
