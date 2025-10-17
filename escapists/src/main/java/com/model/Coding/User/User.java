package com.model.Coding.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import com.model.Coding.Progress.Progress;

public class User {
    private String userName;
    private String password;
    private Progress currSave;
    private ArrayList<Progress> saves;
    private HashMap<Integer, ArrayList<Integer>> completionTimes; // array list of times in seconds keyed by difficulty level

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.saves = new ArrayList<>();
        this.completionTimes = new HashMap<>();
    }

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String username) {
        this.userName = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void createSave() {
        Progress save = new Progress(); 
        addSave(save);
    }

    public void addSave(Progress save) {
        saves.add(save);
        currSave = save;
    }

    public List<Progress> getSaves() {
        return new ArrayList<>(saves);
    }

    public void pushSaves() {
    }

    public void changeCurrSave(int saveIndex) {
        if (saveIndex >= 0 && saveIndex < saves.size()) {
            currSave = saves.get(saveIndex);
        }
    }

    public Progress getCurrSave() {
        return currSave; 
    }

    public boolean auth(String username, String password) {
        return this.userName.equals(username) && this.password.equals(password);
    }
}
