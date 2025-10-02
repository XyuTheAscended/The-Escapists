package com.model.Coding.Gameplay;

import java.util.HashMap;
import java.util.UUID;

import com.model.Coding.Gameplay.InteractItems.Inventory;
import com.model.Coding.Progress.Achievement;
import com.model.Coding.Progress.Leaderboard;
import com.model.Coding.User.User;

public class GameFacade {
    private static GameFacade gameFacade;
    private User currentUser;
    private String currentState;
    private boolean isPaused;
    private Leaderboard leaderboard;
    private HashMap<Integer, String> allAchievements;
    private int difficulty;
    private Inventory inventory;


    public GameFacade(){

    }

    public GameFacade(UUID progressId, User user){

    }

    public static GameFacade getInstance(){
        return gameFacade;
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
        return leaderboard;
    }

    public Inventory getInventory(){
        return inventory;
    }

    public void addAchievement(Achievement achievement){

    }

    public void openMap(){

    }

    public void closeMap(){

    }

    public void setDifficulty(int level){

    }

    public User login(String userName, String password){
        return currentUser;
    }

    public User register(String userName, String password){
        return currentUser;
    }

    public void logout(){

    }

    public void save(){

    }

    public void loadSave(int saveIndex){
    
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
        gf.logout();
        gf.save();
        gf.loadSave(0);
    }
}