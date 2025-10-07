package com.model.Coding.User;

import java.util.ArrayList;
import java.util.List;
import com.model.Coding.Progress.Progress;

public class User {
    private String userName;
    private String password;
    private Progress currSave;
    private ArrayList<Progress> saves;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.saves = new ArrayList<>();
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

    public boolean auth(String username, String password) {
        return this.userName.equals(username) && this.password.equals(password);
    }
}
