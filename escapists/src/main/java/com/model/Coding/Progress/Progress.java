package com.model.Coding.Progress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.model.Coding.Gameplay.InteractItems.Inventory;
import com.model.Coding.Gameplay.InteractItems.Puzzle;
import com.model.Coding.Gameplay.Map.Room;

/**
 * Tracks the progress of the user throughout the game
 * @author
 */
public class Progress {
    private UUID progressId;
    private Room currentRoom;
    private ArrayList<Room> completedRooms;
    private Inventory inventory;
    private ArrayList<Achievement> achievements;
    private int difficulty;
    private int remainingTime;
    private HashMap<String, HashMap<String, Boolean>> completedPuzzles;
    private String currentRoomName;

    /**
     * Initializes progress
     */
    public Progress() {
        initProgress(UUID.randomUUID(), new HashMap<>());
    }

    /**
     * Initializes progress when loading a game
     * @param progressId ID of the progress
     * @param completedPuzzles HashMap of completed puzzles
     */
    public Progress(UUID progressId, HashMap<String, HashMap<String, Boolean>> completedPuzzles) { 
        // use when we're loading progress, cause it already has an id
        initProgress(progressId, completedPuzzles);
    }

    /**
     * Initializes progress variables
     * @param progressId ID of the progress
     * @param completedPuzzles Hashmap of completed puzzles
     */
    private void initProgress(UUID progressId, HashMap<String, HashMap<String, Boolean>> completedPuzzles) {
        this.progressId = progressId;
        this.completedRooms = new ArrayList<>();
        this.achievements = new ArrayList<>();
        this.completedPuzzles = completedPuzzles;
        this.difficulty = 0;
        this.remainingTime = 0;
        
        this.inventory = new Inventory(); // will be initialized from json in future
    }

    /**
     * List the stats/progress of the player
     * @return String of the stats
     */
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

    /**
     * Retrieves the progressID
     * @return UUID for progress
     */
    public UUID getProgressId() {
        return progressId;
    }

    /**
     * Sets the current room
     * @param room Room to set currentRoom to
     */
    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }

    /**
     * Retrieves the current room
     * @return The current room the player is in
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * Marks the room complete
     * @param room Room being marked completed
     */
    public void markRoomCompleted(Room room) {
        if (room == null) return;

        if (!completedRooms.contains(room)) {
        completedRooms.add(room);
        }
    }

    /**
     * Set's the boolean value if puzzle is completed or not
     * @param room Room the puzzle is in
     * @param puzzle Puzzle being marked complete or not
     * @param bool Boolean if it is completed or not
     */
    public void setPuzzleCompleted(Room room, Puzzle puzzle, boolean bool) {
        String roomName = room.getName();
        String puzzleName = puzzle.getName();
        completedPuzzles.putIfAbsent(roomName, new HashMap<>());
        if (bool) puzzle.setIsCompleted(bool);
        completedPuzzles.get(roomName).put(puzzleName, bool);
        room.updateExits(); // exits updated here in case the completed puzzle is meant to trigger an opening
    }

    /**
     * Retries ArrayList of completed rooms
     * @return ArrayList of completed rooms
     */
    public ArrayList<Room> getCompletedRooms() {
        return new ArrayList<>(completedRooms);
    }

    /**
     * Retrieves HashMap of completed puzzles
     * @return HashMap of completed puzzles
     */
    public HashMap<String, HashMap<String, Boolean>> getCompletedPuzzles() {
    HashMap<String, HashMap<String, Boolean>> copy = new HashMap<>();

        for (String roomName : completedPuzzles.keySet()) {
            HashMap<String, Boolean> puzzles = completedPuzzles.get(roomName);
            copy.put(roomName, new HashMap<>(puzzles));
        }
        return copy;
    }

    /**
     * Retrieves number of completed puzzles
     * @return Number of completed puzzles
     */
    public int getCompletedPuzzlesCount() {
        return completedPuzzles.size();
    }

    /**
     * Retrieves the inventory
     * @return Inventory
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Retrieves ArrayList of achievements
     * @return ArrayList of achievements
     */
    public ArrayList<Achievement> getAchievements() {
        return new ArrayList<>(achievements);
    }

    /**
     * Sets difficultly level
     * @param level Level of difficulty
     */
    public void setDifficulty(int level) {
        this.difficulty = level;
    }

    /**
     * Gets difficulty
     * @return Level of difficulty
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Sets remaining amount of time
     * @param time Reaming time (in seconds)
     */
    public void setRemainingTime(int time) {
        this.remainingTime = time;
    }

    /**
     * Gets remaining time
     * @return Remaining time (in seconds)
     */
    public int getRemainingTime() {
        return remainingTime;
    }

    /**
     * Checks if all the puzzles in a room are completed
     * @param room Room the puzzles are in
     * @return True if all are complete, false otherwise
     */
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

    public String getCurrentRoomName() {
    return currentRoomName;
    }

    public void setCurrentRoomName(String roomName) {
        this.currentRoomName = roomName;
    }
}
