package com.model.Coding.Progress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import Coding.Gameplay.InteractItems.Inventory;
import Coding.Gameplay.Map.Room;

public class Progress {
    private UUID progressId;
    private Room currentRoom;
    private List<Room> completedRooms;
    private Inventory inventory;
    private ArrayList<Achievement> achievements;
    private int difficulty;
    private int remainingTime;
    private HashMap<String, HashMap<String, Boolean>> completedPuzzles;


public Progress(){

}

public UUID getProgressId(){
    return null;
}

public void setCurrentRoom(Room room){

}

public Room getCurrentRoom(){
    return null;
}

public void markRoomCompleted(Room room){

}

public void getCompletedRooms(){

}

public void markPuzzleCompleted(Puzzle puzzle){

}

public int getCompletedPuzzles(){
    return 0;
}

public void getInventory(){

}

public ArrayList<Achievement> getAchievements(){
    return new ArrayList<>();
}

public void setDifficulty(int leve){

}

public int getDifficulty(){
    return 0;
}

public int setRemainingTime(int time){
    return 0;
}

public int getRemainingTime(){
    return 0;
}

}