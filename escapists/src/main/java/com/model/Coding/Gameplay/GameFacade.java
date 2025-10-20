package com.model.Coding.Gameplay;

import java.util.ArrayList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.model.Coding.Data.DataLoader;
import com.model.Coding.Data.DataManager;
import com.model.Coding.Gameplay.InteractItems.Inventory;
import com.model.Coding.Gameplay.Map.Map;
import com.model.Coding.Gameplay.Map.Room;
import com.model.Coding.Progress.Achievement;
import com.model.Coding.Progress.Leaderboard;
import com.model.Coding.User.User;
import com.model.Coding.User.UserList;
import com.model.Coding.Progress.Progress;

public class GameFacade {
    private static GameFacade gameFacade;

    private ArrayList<Achievement> allAchievements;

    // stuff initialized in constructor
    private String currentState; // WHAT ARE THE StATES?
    private boolean isPaused;
    private Leaderboard leaderboard;

    // stuff initalized when save is loaded
    private User currentUser;
    private Progress activeProgress; 
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

    public void startGame(){
        if (currentUser == null) {
            System.err.println("Cant start the game without a currnent user...");
            return;
        }

        loadCurrSave();
        


    }

    public void pause() {
        if (isPaused) {
            System.out.println("Game is already paused.");
            return;
        }

        isPaused = true;

        if (Timer.getInstance() != null) {
            Timer.getInstance().pause();
        }

        System.out.println("Game paused.");
    }

    public void resume() {
        if (!isPaused) {
            System.out.println("Game is not paused.");
            return;
        }

        isPaused = false;

        if (Timer.getInstance() != null) {
            Timer.getInstance().resume();
        }

        System.out.println("Game resumed.");
    }


    public boolean isPaused() {
        return isPaused;
    }

    public Leaderboard getLeaderboard(){
        return Leaderboard.getInstance();
    }

    public Inventory getInventory(){
        return inventory;
    }

    public void addAchievement(Achievement achievement){
        if (allAchievements == null) return; 
        allAchievements.add(achievement);
    }

    public void openMap(){
        if (map == null) return; 
        map.openMap();
    }

    public void closeMap(){
        if (map == null) return; 
        map.closeMap();
    }

    public void setDifficulty(int level){
        this.difficulty = level;
    }

    /**
     * Logs in a preexisting
     * @param userName
     * @param password
     * @return User data that we have logged in to access 
     */
    public boolean login(String userName, String password) {
        User temp = UserList.getInstance().getUser(userName, password);

        if(temp == null) {
            return false;
        }
        currentUser = temp;
        return true;
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
        if (save == null) currentUser.createSave();;  

        this.activeProgress = save; 
        this.difficulty = save.getDifficulty();
        this.inventory = save.getInventory();
        this.map = new Map();
        this.map.setCurrentRoom("Cell"); // NOTE: hardcoded for now but ill change it later i promise !!
    }

    // wrapper funcs for map's currRoom management methods
    public Room getCurrRoom() {
        return (map != null) ? this.map.getCurrentRoom() : null;
    }

    public void setCurrRoom(Room room) {
        if (map == null) return;
        map.setCurrentRoom(room);
        this.activeProgress.setCurrentRoom(room);
    }

    public ArrayList<Room> getRooms() {
        return (map != null) ? this.map.getRooms() : null;
    }

    public User getCurrUser() {
        return currentUser;
    }

    // temp testing method
     public static void main(String[] args) {
        GameFacade gf = GameFacade.getInstance();
        // gf.addAchievement(null);
        // gf.closeMap();
        // gf.startGame(null);
        // gf.pause();
        // gf.resume();
        // gf.isPaused();
        // gf.getLeaderboard();
        // gf.getInventory();
        // gf.openMap();
        // gf.setDifficulty(0);
        // gf.login(null, null);
        // gf.register(null, null);
        // gf.save();
        // gf.loadCurrSave();
        // gf.logout();
    }
}