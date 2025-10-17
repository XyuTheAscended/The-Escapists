package com.model.Coding.Gameplay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.model.Coding.Gameplay.InteractItems.Inventory;
import com.model.Coding.Gameplay.Map.Map;
import com.model.Coding.Progress.Achievement;
import com.model.Coding.Progress.Leaderboard;
import com.model.Coding.User.User;
import com.model.Coding.User.UserList;
import com.model.Coding.Progress.Progress;

public class GameFacade {
    private static GameFacade gameFacade;

    private ArrayList<Achievement> allAchievements;
    private String currentState; // WHAT ARE THE StATES?
    private boolean isPaused;
    private Leaderboard leaderboard;

    private User currentUser;
    private int difficulty;
    private Inventory inventory;
    private Map map; 


    private GameFacade() {
        currentState = "Inactive"; 
        isPaused = false;
        leaderboard = Leaderboard.getInstance(); 

        gameFacade = this;
    }

    public static GameFacade getInstance(){
        return gameFacade != null ? gameFacade : new GameFacade();
    }

    public void startGame(User user){

    }

    public void pause(){

    }

    public void resume(){

    }

    public boolean isPaused(){
        return true;
    }

    public Leaderboard getLeaderboard(){
        return Leaderboard.getInstance();
    }

    public Inventory getInventory(){
        return new Inventory();
    }

    public void addAchievement(Achievement achievement){

    }

    public void openMap(){

    }

    public void closeMap(){

    }

    public void setDifficulty(int level){

    }

    /**
     * Logs in a preexisting
     * @param userName
     * @param password
     * @return User data that we have logged in to access 
     */
    public boolean login(String userName, String password) {
        UserList userList = UserList.getInstance(); 
        User user = userList.getUser(userName);
        if (user == null || !user.auth(userName, password)) { // looking at implementation here im realizing checking username in auth method is redundant
            return false; 
        } else {
            currentUser = user; 
            return true; 
        }
    }

    public User register(String userName, String password){
        UserList userList = UserList.getInstance(); 
        if (!userList.checkAvailability(userName)) { 
            return null; 
        }

        return userList.createUser(userName, password);
    }

    public void logout(){
        if (currentUser == null) return;

        currentUser = null; 
    }

    public void save(){

    }

    // Loads current save under current user
    public void loadCurrSave(){
        if (currentUser == null) return;
        Progress save = currentUser.getCurrSave();
        if (save == null) return;  

        this.difficulty = save.getDifficulty();
        this.inventory = save.getInventory();
        this.map = new Map(); // PLACEhOLDER BECAUSE IDK WHAT WE'RE DOING WITH IT
    }

    // temp testing method
    public static void main(String[] args) {
        GameFacade gf = new GameFacade();
        gf.addAchievement(null);
        gf.closeMap();
        gf.startGame(null);
        gf.pause();
        gf.resume();
        gf.isPaused();
        gf.getLeaderboard();
        gf.getInventory();
        gf.openMap();
        gf.setDifficulty(0);
        gf.login(null, null);
        gf.register(null, null);
        gf.save();
        gf.loadCurrSave();
        gf.logout();
    }
}