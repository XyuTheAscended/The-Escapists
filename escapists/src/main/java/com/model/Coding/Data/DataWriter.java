package com.model.Coding.Data;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.model.Coding.Progress.Progress;
import com.model.Coding.User.User;

public class DataWriter {
    private static final String USER_FILE = "escapists/src/main/java/com/model/Coding/json/users.json";
    private static DataWriter dataWriter;

    private DataWriter() { }

    public static DataWriter getInstance() {
        if (dataWriter == null) {
            dataWriter = new DataWriter();
        }
        return dataWriter;
    }

    public void addUser(User user) {
        try {
            File file = new File(USER_FILE);
            JSONParser parser = new JSONParser();
            JSONObject root;
            JSONArray usersArray;

            // Read file if exists
            if (file.exists() && file.length() > 0) {
                FileReader reader = new FileReader(file);
                root = (JSONObject) parser.parse(reader);
                reader.close();

                Object usersObj = root.get("users");
                if (usersObj instanceof JSONArray) {
                    usersArray = (JSONArray) usersObj;
                } else {
                    usersArray = new JSONArray();
                    root.put("users", usersArray);
                }
            } else {
                // Create new object if necessary
                root = new JSONObject();
                usersArray = new JSONArray();
                root.put("users", usersArray);
            }

            // Create new user object
            JSONObject newUser = new JSONObject();
            newUser.put("userName", user.getUserName());
            newUser.put("password", user.getPassword());
            newUser.put("currSave", null);
            newUser.put("saves", new JSONArray());

            // Add new user
            usersArray.add(newUser);

            // Write back to the file
            FileWriter writer = new FileWriter(file);
            writer.write(root.toJSONString());
            writer.flush();
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @SuppressWarnings("unchecked")
    
private void changeCurrSaveJSON(JSONObject saveJson, Progress progress) {
    // Progress components
    saveJson.put("progressId", progress.getProgressId().toString());
    saveJson.put("difficulty", progress.getDifficulty());
    saveJson.put("remainingTime", progress.getRemainingTime());

    // Current room
    if (progress.getCurrentRoom() != null)
        saveJson.put("currentRoom", progress.getCurrentRoom().toString());
    else
        saveJson.put("currentRoom", "Unknown");

    // Completed rooms
    JSONArray completedRoomsArray = new JSONArray();
    if (progress.getCompletedRooms() != null) {
        progress.getCompletedRooms().forEach(room -> completedRoomsArray.add(room.toString()));
    }
    saveJson.put("completedRooms", completedRoomsArray);

    // Completed puzzles
    JSONObject completedPuzzlesJson = new JSONObject();
    saveJson.put("completedPuzzles", completedPuzzlesJson);

    // Inventory
    JSONObject inventoryJson = new JSONObject();
    JSONArray itemsArray = new JSONArray();
    if (progress.getInventory() != null && progress.getInventory().getItems() != null) {
        progress.getInventory().getItems().forEach(item -> {
            JSONObject itemJson = new JSONObject();
            itemJson.put("itemId", item.getItemId());
            itemJson.put("name", item.getName());
            itemJson.put("description", item.getDescription());
            itemsArray.add(itemJson);
        });
    }
    inventoryJson.put("items", itemsArray);
    saveJson.put("inventory", inventoryJson);

    // Achievements (need to update this)
    JSONArray achievementsArray = new JSONArray();
    if (progress.getAchievements() != null) {
        progress.getAchievements().forEach(achievement -> {
            JSONObject a = new JSONObject();
            a.put("name", "Sample Achievement");
            achievementsArray.add(a);
        });
    }
    saveJson.put("achievements", achievementsArray);
}

@SuppressWarnings("unchecked")
public void saveProgress(User user, Progress progress) {
    if (user == null || progress == null) {
        System.out.println("Cannot save progress: null user or progress");
        return;
    }

    String filePath = USER_FILE;
    JSONParser parser = new JSONParser();

    try {
        File file = new File(filePath);
        JSONObject root;
        JSONArray usersArray;

        // Read JSON file
        if (file.exists() && file.length() > 0) {
            FileReader reader = new FileReader(file);
            root = (JSONObject) parser.parse(reader);
            reader.close();
            usersArray = (JSONArray) root.get("users");
        } else {
            System.out.println("No user file -- cannot save.");
            return;
        }

        // Find user in JSON
        for (Object userObj : usersArray) {
            JSONObject userJson = (JSONObject) userObj;
            if (user.getUserName().equals(userJson.get("userName"))) {

                JSONArray savesArray = (JSONArray) userJson.get("saves");
                if (savesArray == null) {
                    savesArray = new JSONArray();
                    userJson.put("saves", savesArray);
                }

                boolean saveFound = false;
                for (int i = 0; i < savesArray.size(); i++) {
                    JSONObject saveJson = (JSONObject) savesArray.get(i);
                    String idStr = (String) saveJson.get("progressId");
                    if (idStr != null && idStr.equals(progress.getProgressId().toString())) {
                        changeCurrSaveJSON(saveJson, progress);
                        saveFound = true;
                        break;
                    }
                }

                if (!saveFound) {
                    JSONObject newSave = new JSONObject();
                    changeCurrSaveJSON(newSave, progress);
                    savesArray.add(newSave);
                }

                // Update current save pointer
                userJson.put("currSave", progress.getProgressId().toString());

                // Write back to file
                FileWriter writer = new FileWriter(filePath);
                root.put("users", usersArray);
                writer.write(root.toJSONString());
                writer.flush();
                writer.close();

                System.out.println("Progress saved successfully for user: " + user.getUserName());
                return;
            }
        }

        System.out.println("User not found in JSON -- cannot save progress.");

    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
