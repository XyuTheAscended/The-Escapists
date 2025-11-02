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
     * Saves progress wrapper.
     *
     * @param user the user whose progress to save
     * @param progress the progress object to save
     */
    public void saveProgress(User user, Progress progress) {
        DataWriter.getInstance().saveProgress(user, progress);
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
