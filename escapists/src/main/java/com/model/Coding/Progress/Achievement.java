package com.model.Coding.Progress;

import java.util.ArrayList;
import java.util.HashMap;
import com.model.Coding.User.User;

public class Achievement {
    private String name;
    private String description;
    private ArrayList<Achievement> userAchievements;
    private HashMap<Integer, String> allAchievements;
    private User user;

    public Achievement(User user) {
        this.user = user;
        this.userAchievements = new ArrayList<>();
        this.allAchievements = new HashMap<>();
    }

    public void addAchievement() {
    }
}
