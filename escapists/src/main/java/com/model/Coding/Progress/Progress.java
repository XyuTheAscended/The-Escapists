package com.model.Coding.Progress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.model.Coding.Gameplay.InteractItems.Inventory;
import com.model.Coding.Gameplay.Map.Room;

public class Progress {
    private UUID progressId;
    private Room currentRoom;
    private ArrayList<Room> completedRooms;
    private Inventory inventory;
    private ArrayList<Achievement> achievements;
    private int difficulty;
    private int remainingTime;
    private HashMap<String, HashMap<String, Boolean>> completedPuzzles;

    public Progress() {
        this.progressId = UUID.randomUUID();
        this.completedRooms = new ArrayList<>();
        this.achievements = new ArrayList<>();
        this.completedPuzzles = new HashMap<>();
        this.difficulty = 0;
        this.remainingTime = 0;
    }

    public UUID getProgressId() {
        return progressId;
    }

    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void markRoomCompleted(Room room) {
    }

    public ArrayList<Room> getCompletedRooms() {
        return new ArrayList<>(completedRooms);
    }

    public int getCompletedPuzzlesCount() {
        return completedPuzzles.size();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public ArrayList<Achievement> getAchievements() {
        return new ArrayList<>(achievements);
    }

    public void setDifficulty(int level) {
        this.difficulty = level;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setRemainingTime(int time) {
        this.remainingTime = time;
    }

    public int getRemainingTime() {
        return remainingTime;
    }
}
