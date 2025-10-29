package com.model.Coding.Data;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.model.Coding.Progress.Progress;
import com.model.Coding.User.User;

/**
 * Writes user and progress data to JSON files.
 * @author Liam and Jeffen
 */
public class DataWriter {

    private static final String USER_FILE = "escapists/src/main/java/com/model/Coding/json/users.json";
    private static final String CERT_LOCATION = "escapists/../docs";
    private static DataWriter dataWriter;

    private DataWriter() {
    }

    /**
     * Returns instance.
     *
     * @return DataWriter instance
     */
    public static DataWriter getInstance() {
        if (dataWriter == null) {
            dataWriter = new DataWriter();
        }
        return dataWriter;
    }

    /**
     * Adds new user to persistent storage.
     *
     * @param user user to add
     */
    public void addUser(User user) {
        try {
            File file = new File(USER_FILE);
            JSONParser parser = new JSONParser();
            JSONObject root;
            JSONArray usersArray;

            /* Read file if exists */
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
                /* Create new object if necessary  */
                root = new JSONObject();
                usersArray = new JSONArray();
                root.put("users", usersArray);
            }

            /* Create new user object */
            JSONObject newUser = new JSONObject();
            newUser.put("userName", user.getUserName());
            newUser.put("password", user.getPassword());
            newUser.put("currSave", null);
            newUser.put("saves", new JSONArray());
            newUser.put("completionTimes", new JSONObject());

            /* Add new user */
            usersArray.add(newUser);

            /* Write back to file */
            FileWriter writer = new FileWriter(file);
            writer.write(root.toJSONString());
            writer.flush();
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates user's completed times data in json
     * @param user the one we're updating
     */
    public void updateUser(User user) {
        try {
            File file = new File(USER_FILE);
            JSONParser parser = new JSONParser();
            JSONObject root;
            JSONArray usersArray;

            /* Read file if exists */
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
                throw new RuntimeException("User file don't exist bru");
            }

            for (int i = 0; i < usersArray.size(); i++) {
                JSONObject userJSObj = (JSONObject) usersArray.get(i);
                if (((String)userJSObj.get("userName")).equals(user.getUserName())) {
                    JSONObject compTimes = (JSONObject) userJSObj.get("completionTimes");
                    compTimes.clear(); // clear this cause we're gonna rewrite them all
                    for (Integer diffKey : user.getCompletionTimesHashmap().keySet() ) {
                        ArrayList<Integer> times4Diff = user.getCompletionTimesHashmap().get(diffKey);
                        JSONArray timesArr = new JSONArray();
                        for (Integer time : times4Diff) {
                            timesArr.add(time);
                        }
                        compTimes.put(diffKey, timesArr);
                    }
                }
            }

            /* Write back to file */
            FileWriter writer = new FileWriter(file);
            writer.write(root.toJSONString());
            writer.flush();
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")

    /**
     * Method for changing current save assigned to a user
     * @param saveJson
     * @param progress
     */
    private void changeCurrSaveJSON(JSONObject saveJson, Progress progress) {
        saveJson.clear();

        /* Progress components */
        saveJson.put("progressId", progress.getProgressId().toString());
        saveJson.put("difficulty", progress.getDifficulty());
        saveJson.put("remainingTime", progress.getRemainingTime());
        saveJson.put("hintsUsed", progress.getHintsUsed());

        /* Current room */
        if (progress.getCurrentRoom() != null) {
            saveJson.put("currentRoom", progress.getCurrentRoom().getName()); 
        }else {
            String currRoomName = progress.getCurrentRoomName();
            if (currRoomName != null && currRoomName.equals("OUTSIDE")) {
                saveJson.put("currentRoom", "OUTSIDE");
            } else {
                saveJson.put("currentRoom", null);
            }
        }

        /* Completed rooms */
        JSONArray completedRoomsArray = new JSONArray();
        if (progress.getCompletedRooms() != null) {
            progress.getCompletedRooms().forEach(room -> completedRoomsArray.add(room.toString()));
        }
        saveJson.put("completedRooms", completedRoomsArray);

        /* Completed puzzles */
        JSONObject completedPuzzlesJson = new JSONObject();
        for (String roomName : progress.getCompletedPuzzles().keySet()) {
            JSONObject roomCompsEntry = new JSONObject();
            completedPuzzlesJson.put(roomName, roomCompsEntry);
            HashMap<String, Boolean> puzzleComps = progress.getCompletedPuzzles().get(roomName);
            for (String puzName : puzzleComps.keySet()) {
                boolean isComp = puzzleComps.get(puzName);
                roomCompsEntry.put(puzName, isComp);
            }
        }
        saveJson.put("completedPuzzles", completedPuzzlesJson);

        /* Inventory */
        JSONObject inventoryJson = new JSONObject();
        JSONArray itemsArray = new JSONArray();
        if (progress.getInventory() != null && progress.getInventory().getItems() != null) {
            progress.getInventory().getItems().forEach(item -> {


                JSONObject itemJson = new JSONObject();
                // itemJson.put("itemId", item.getItemId()); // removed because Item class creates item Ids based on order item was loaded in. May change in future
                itemJson.put("name", item.getName());
                itemJson.put("description", item.getDescription());
                itemsArray.add(itemJson);
            });
        }
        inventoryJson.put("items", itemsArray);
        saveJson.put("inventory", inventoryJson);

        /* Achievements */
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

    /**
     * Method for creating a certificate based on some basic statistics. its a text files stored in docs folder of this project
     * @param difficulty difficulty player played at
     * @param hintsUsed how many hints used to complete the game
     * @param score score is calculated where this method is called
     */
    public void createCertificate(int difficulty, int hintsUsed, int score) {
        String fileName = "escapist_certificate.txt";

        // MAKE THE DIRECTORY IF IT DOESN'T EXIST
        File directory = new File(CERT_LOCATION);
        if (!directory.exists()) {
            directory.mkdirs(); // CREATE THE WHOLE PATH
            System.out.println("DIRECTORY CREATED: " + CERT_LOCATION);
        }

        // NOW BUILD THE FULL PATH TO THE FILE
        File file = new File(CERT_LOCATION + File.separator + fileName);

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("===============================================\n\n");
            writer.write("Thanks for playing The-Escapists \n");
            writer.write("You completed the game at a level " + difficulty + " difficulty, using " + hintsUsed + " hints.\n");
            writer.write("Final score: " + score + "\n");
            writer.write("\n===============================================");
            System.out.println("FILE WRITTEN SUCCESSFULLY AT: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("SOMETHING WENT WRONG WITH WRITING THIS THING: " + e.getMessage());
        }
    }

    /**
     * Saves progress for user.
     *
     * @param user user whose progress to save
     * @param progress progress to save
     */
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

            /* Read JSON file */
            if (file.exists() && file.length() > 0) {
                FileReader reader = new FileReader(file);
                root = (JSONObject) parser.parse(reader);
                reader.close();
                usersArray = (JSONArray) root.get("users");
            } else {
                System.out.println("No user file -- cannot save.");
                return;
            }

            /* Find user in JSON */
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

                    /* Update current save pointer */
                    userJson.put("currSave", progress.getProgressId().toString());

                    /* Write back to file */
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
