package Coding.Gameplay;

import java.util.HashMap;

import Coding.Gameplay.InteractItems.Inventory;
import Coding.Progress.Achievement;

public class GameFacade {
    private static GameFacade gameFacade;
    private User currentUser;
    private String currentState;
    private boolean isPaused;
    private Leaderboard leaderboard;
    private HashMap<int, String> allAchievements;
    private int difficulty;


public GameFacade(){

}

public GameFacade(UUID progressId, User user){

}

public static GameFacade getInstance(){

}

public void startGame(User user){

}

public void pause(){

}

public void resume(){

}

public boolean isPaused(){

}

public Leaderboard getLeaderboard(){

}

public Inventory getInventory(){

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

}

public User register(String userName, String password){

}

public void logout(){

}

public void save(){

}

public void loadSave(int saveIndex){
    
}

}