package com.model.Coding.Progress;

import java.util.ArrayList;
import java.util.HashMap;
import com.model.Coding.User.User;

/**
 * An in game achievement
 * @author
 */
public class Achievement {
    private String name;
    private String description;
    private ArrayList<Achievement> userAchievements;
    private HashMap<Integer, String> allAchievements;
    private User user;

    /**
     * Initializes user, userAchievements, and allAchievements
     * @param user Current user
     */
    public Achievement(User user) {
        this.user = user;
        this.userAchievements = new ArrayList<>();
        this.allAchievements = new HashMap<>();
    }

    /**
     * Adds achievement
     * @param name Name of the achievement
     * @param description Description of the achievement
     */
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

    /**
     * Gets name of the achievement
     * @return Name of the achievement
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the achievement
     * @param name Name for the achievement
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of the achievement
     * @return Description of the achievement
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description of the achievement
     * @param description Description of the achievement
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets ArrayList of the user's achievements
     * @return ArrayList of user's achievements
     */
    public ArrayList<Achievement> getUserAchievements() {
        return new ArrayList<>(userAchievements);
    }
}