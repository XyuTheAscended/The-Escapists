package com.model.Coding.Progress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.model.Coding.Gameplay.InteractItems.Inventory;
import com.model.Coding.Gameplay.InteractItems.Puzzle;
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
        initProgress(UUID.randomUUID(), new HashMap<>());
    }

    public Progress(UUID progressId, HashMap<String, HashMap<String, Boolean>> completedPuzzles) { 
        // use when we're loading progress, cause it already has an id
        initProgress(progressId, completedPuzzles);
    }

    private void initProgress(UUID progressId, HashMap<String, HashMap<String, Boolean>> completedPuzzles) {
        this.progressId = progressId;
        this.completedRooms = new ArrayList<>();
        this.achievements = new ArrayList<>();
        this.completedPuzzles = completedPuzzles;
        this.difficulty = 0;
        this.remainingTime = 0;
        this.inventory = new Inventory(); // will be initialized from json in future
    }

    public String toString() {
        String text = "";
        text += "Progress ID: " + progressId + "\n";
        text += "Difficulty: " + difficulty + "\n";
        text += "Remaining Time: " + remainingTime + " seconds\n";

        text += "Completed Puzzles:\n";
        if (completedPuzzles != null && !completedPuzzles.isEmpty()) {
            for (var categoryEntry : completedPuzzles.entrySet()) {
                text += "  " + categoryEntry.getKey() + ":\n";
                for (var puzzleEntry : categoryEntry.getValue().entrySet()) {
                    text += "    " + puzzleEntry.getKey() + " -> " 
                        + (puzzleEntry.getValue() ? "yes" : "no") + "\n";
                }
            }
        } else {
            text += "  None\n";
        }

        return text;
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
        if (room == null) return;

        if (!completedRooms.contains(room)) {
        completedRooms.add(room);
        }
    }

    public void setPuzzleCompleted(Room room, Puzzle puzzle, boolean bool) {
        String roomName = room.getName();
        String puzzleName = puzzle.getName();
        completedPuzzles.putIfAbsent(roomName, new HashMap<>());
        if (bool) puzzle.setIsCompleted(bool);
        completedPuzzles.get(roomName).put(puzzleName, bool);
        room.updateExits(); // exits updated here in case the completed puzzle is meant to trigger an opening
    }

    public ArrayList<Room> getCompletedRooms() {
        return new ArrayList<>(completedRooms);
    }

    public HashMap<String, HashMap<String, Boolean>> getCompletedPuzzles() {
    HashMap<String, HashMap<String, Boolean>> copy = new HashMap<>();

    for (String roomName : completedPuzzles.keySet()) {
        HashMap<String, Boolean> puzzles = completedPuzzles.get(roomName);
        copy.put(roomName, new HashMap<>(puzzles));
    }

    return copy;
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

    public boolean allPuzzlesCompleted(Room room) {
        HashMap<String, Boolean> puzzles = completedPuzzles.get(room.getName());
        if (puzzles == null || puzzles.isEmpty()) return false;

        for (Boolean completed : puzzles.values()) {
            if (!Boolean.TRUE.equals(completed)) {
                return false;
            }
        }
        return true;
    }

}
