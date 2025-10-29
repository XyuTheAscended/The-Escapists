package com.model.Coding.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import com.model.Coding.Progress.Progress;

/**
 * A user
 * @author Liam
 */
public class User {
    private String userName;
    private String password;
    private Progress currSave;
    private ArrayList<Progress> saves;
    private HashMap<Integer, ArrayList<Integer>> completionTimes; // array list of times in seconds keyed by difficulty level

    /**
     * Initializes user and user variables
     * @param userName User's username
     * @param password User's password
     */
    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.saves = new ArrayList<>();
        this.completionTimes = new HashMap<>();
    }

    /**
     * Creates a user when loaded from Json along with their information
     * @param userName User's username
     * @param password User's password
     * @param saves User's saves
     * @param currSaveId User's current save ID
     * @param completionTimes User's ArrayList of completion times
     */
    public User(String userName, String password, ArrayList<Progress> saves, UUID currSaveId, HashMap<Integer, ArrayList<Integer>> completionTimes) {
        this.userName = userName;
        this.password = password;
        this.saves = saves;
        
        for (Progress save : saves) {
            if (save.getProgressId().equals(currSaveId)) {
                this.currSave = save;
            }
        }

        this.completionTimes = completionTimes;
    }

    /**
     * Turns user into a nice string for debugging
     * @return string representation of user
     */
    public String toString() {
        String info = "User Information:\n";
        info += "Username: " + userName + "\n";
        info += "Password: " + (password != null ? "********" : "None") + "\n\n";

        info += "Current Save:\n";
        if (currSave != null) {
            // indent the progress string by two spaces
            String[] lines = currSave.toString().split("\n");
            for (String line : lines) {
                info += "  " + line + "\n";
            }
        } else {
            info += "  None\n";
        }

        info += "\nAll Saves:\n";
        if (saves != null && !saves.isEmpty()) {
            for (int i = 0; i < saves.size(); i++) {
                info += "  Save " + (i + 1) + ":\n";
                String[] lines = saves.get(i).toString().split("\n");
                for (String line : lines) {
                    info += "    " + line + "\n"; // double-indent saves
                }
            }
        } else {
            info += "  No saves available.\n";
        }

        info += "\nAll Completion Times:\n";
        if (completionTimes.size() == 0) 
            info += "    " + "No Completion times available" + "\n"; 
        else {
            for (Entry<Integer, ArrayList<Integer>> entry : completionTimes.entrySet()) {
                Integer difficulty = entry.getKey();
                ArrayList<Integer> times = entry.getValue();

                info += "    " + "Difficulty " + difficulty + ": " + times + "\n";
            }
        }
        return info;
    }

    /**
     * Gets user's username
     * @return User's username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets user's username
     * @param username User's username
     */
    public void setUserName(String username) {
        this.userName = username;
    }

    /**
     * Gets user's password
     * @return User's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets user's password
     * @param password User's password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Creates a save for the user
     */
    public void createSave() {
        Progress save = new Progress(); 
        addSave(save);
    }

    /**
     * Adds save to save and sets current save to save that's passed in
     * @param save Progress class ties to user
     */
    public void addSave(Progress save) {
        saves.add(save);
        currSave = save;
    }

    /**
     * Gets user's saves
     * @return ArrayList of saves
     */
    public ArrayList<Progress> getSaves() {
        return new ArrayList<>(saves);
    }

    /**
     * Saves users progress to json files
     */
    public void pushSaves() {
        if (saves == null || saves.isEmpty()) {
            System.out.println("No saves to push for " + userName);
            return;
        }

        com.model.Coding.Data.DataManager dataManager = com.model.Coding.Data.DataManager.getInstance();

        for (Progress save : saves) {
            dataManager.saveProgress(this, save);
        }
        System.out.println("Saves pushed for " + userName);
    }

    /**
     * Changes user's current save
     * @param saveIndex Index of save in saves
     */
    public void changeCurrSave(int saveIndex) {
        if (saveIndex >= 0 && saveIndex < saves.size()) {
            currSave = saves.get(saveIndex);
            System.out.println("Current save changed to index " + saveIndex + " for " + userName);

            com.model.Coding.Data.DataManager dataManager = com.model.Coding.Data.DataManager.getInstance();
            dataManager.saveProgress(this, currSave);
        } else {
            System.out.println("Invalid save index: " + saveIndex);
        }
    }

    /**
     * Gets user's current save
     * @return User's save as a Progress class.
     */
    public Progress getCurrSave() {
        return currSave; 
    }

    /**
     * Authenticates user for login process
     * @param username User's username
     * @param password User's password
     * @return True if authenticates, false if not
     */
    public boolean auth(String username, String password) {
        return this.userName.equals(username) && this.password.equals(password);
    }

    /**
     * Adds user's completion time to completionTimes
     * @param difficulty Level of difficulty game was completed on
     * @param seconds Time (in seconds) of completion time
     */
    public void addCompletionTime(int difficulty, int seconds) {
        Integer difficultyObj = Integer.valueOf(difficulty);
        Integer secondsObj = Integer.valueOf(seconds);

        if (completionTimes.get(difficulty) == null) 
            completionTimes.put(difficultyObj, new ArrayList<Integer>()); 
        ArrayList<Integer> timesForDifficulty = completionTimes.get(difficulty);
        timesForDifficulty.add(secondsObj); 
        
    }

    /**
     * Gets ArrayList of completion times
     * @param difficulty Difficulty of game completed on
     * @return ArrayList of completion times
     */
    public ArrayList<Integer> getCompletionTimes(int difficulty) {
        Integer difficultyKey = Integer.valueOf(difficulty);
        return completionTimes.get(difficultyKey);
    }

    /**
     * Full hashmap of completion times mapping difficulty level w/ integer list
     * @return Completion times hashmap
     */
    public HashMap<Integer, ArrayList<Integer>> getCompletionTimesHashmap() {
        return completionTimes;
    }
}
