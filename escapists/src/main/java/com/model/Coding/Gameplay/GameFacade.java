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

/**
 * Game facade to handle interaction between UI and backend
 * @author
 */
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
    private int difficulty = 0;
    private Inventory inventory;
    private Map map;

    /**
     * GameFacade constructor. Initializes currentState, isPaused, leaderboard, and gameFacade
     */
    private GameFacade() {
        currentState = "Inactive"; 
        isPaused = false;
        leaderboard = Leaderboard.getInstance(); 

        gameFacade = this;
    }

    /**
     * Gets instance of gameFacade
     * @return GameFacade
     */
    public static GameFacade getInstance(){
        return gameFacade != null ? gameFacade : new GameFacade();
    }

    /**
     * Starts the game
     */
    public void startGame(){
        if (currentUser == null) {
            System.err.println("Cant start the game without a currnent user...");
            return;
        }
        loadCurrSave();
        if (difficulty == 0) {
            System.out.println("Defaulting difficulty to 1");
            this.setDifficulty(1);
        }
        Timer.getInstance().start(1800 / difficulty);;
    }

    public int getTimePassed() {
        return Timer.getInstance().getTimePassed();
    }

    /**
     * Pauses the game
     */
    public void pause() {
        if (isPaused) {
            System.out.println("Game is already paused.");
            return;
        }

        isPaused = true;

        if (Timer.getInstance() != null) {
            Timer.getInstance().pause();
        }

        // System.out.println("Game paused.");
    }

    /**
     * Resume the game
     */
    public void resume() {
        if (!isPaused) {
            // System.out.println("Game is not paused.");
            return;
        }

        isPaused = false;

        if (Timer.getInstance() != null) {
            Timer.getInstance().resume();
        }
        // System.out.println("Game resumed.");
    }

    /**
     * Gets isPaused
     * @return Boolean, true if it's paused, false otherwise
     */
    public boolean isPaused() {
        return isPaused;
    }

    /**
     * Gets the leaderboard
     * @return Leaderboard
     */
    public Leaderboard getLeaderboard(){
        return Leaderboard.getInstance();
    }

    /**
     * Gets the inventory
     * @return Inventory
     */
    public Inventory getInventory(){
        return inventory;
    }

    /**
     * Adds achievement to allAchievements
     * @param achievement The achievement object being added
     */
    public void addAchievement(Achievement achievement){
        if (allAchievements == null) return; 
        allAchievements.add(achievement);
    }

    /**
     * Opens the map
     */
    public void openMap(){
        if (map == null) return; 
        map.openMap();
    }

    /**
     * Closes the map
     */
    public void closeMap(){
        if (map == null) return; 
        map.closeMap();
    }

    /**
     * Sets game difficulty
     * @param level Leve of difficulty
     */
    public void setDifficulty(int level){
        this.difficulty = level;
    }

    /**
     * Logs in a preexisting user
     * @param userName User's username
     * @param password User's password
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

    /**
     * Registers a new user
     * @param userName User's username
     * @param password User's password
     * @return User object
     */
    public User register(String userName, String password){
        UserList userList = UserList.getInstance(); 
        if (!userList.checkAvailability(userName)) { 
            return null; 
        }

        return userList.createUser(userName, password);
    }

    /**
     * Logs the current user out
     */
    public void logout(){
        if (currentUser == null) return;
        currentUser = null; 
    }
    // idk
    public void save(){

    }

    /**
     * Loads current save under current user
     */
    public void loadCurrSave(){
        if (currentUser == null) return;
        if (currentUser.getCurrSave() == null) currentUser.createSave(); // creating save automatically sets the curr save to the newly created one
        Progress save = currentUser.getCurrSave();

        this.activeProgress = save; 
        this.difficulty = save.getDifficulty();
        this.inventory = save.getInventory();
        this.map = new Map();
        this.map.setCurrentRoom("Cell"); // NOTE: hardcoded for now but ill change it later i promise !!
    }

    /**
     * Wrapper funcs for map's currRoom management methods
     * @return Current room
     */
    public Room getCurrRoom() {
        return (map != null) ? this.map.getCurrentRoom() : null;
    }

    /**
     * Sets the current room
     * @param room Room to be set to
     */
    public void setCurrRoom(Room room) {
        if (map == null) return;
        map.setCurrentRoom(room);
        this.activeProgress.setCurrentRoom(room);
    }

    /**
     * Gets the ArrayList of rooms
     * @return ArrayList of rooms
     */
    public ArrayList<Room> getRooms() {
        return (map != null) ? this.map.getRooms() : null;
    }

    /**
     * Gets the current user
     * @return Object of the current user
     */
    public User getCurrUser() {
        return currentUser;
    }
}