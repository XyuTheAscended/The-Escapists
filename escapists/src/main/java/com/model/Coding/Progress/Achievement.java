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

    public void addAchievement(String name, String description) {
        for (Achievement ach : userAchievements) {
            if (ach.getName().equalsIgnoreCase(name)) {
                System.out.println("Achievement already earned: " + name);
                return;
            }
        }

        Achievement newAch = new Achievement(this.user);
        newAch.setName(name);
        newAch.setDescription(description);

        userAchievements.add(newAch);

        System.out.println("Achievement added for " + user.getUserName() + ": " + name);
    }
    
    public String getName() {
    return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Achievement> getUserAchievements() {
        return new ArrayList<>(userAchievements);
    }


}