package com.model.Coding.Gameplay.Map;

import java.util.ArrayList;

import org.junit.Test;

import com.model.Coding.Data.DataManager;
import com.model.Coding.Gameplay.InteractItems.Puzzle;
import com.model.Coding.Progress.Progress;

/**
 * Map
 * @author Mason Adams
 */
public class Map {
    private boolean isOpen = false;
    private Room currentRoom;
    private ArrayList<Room> rooms;

    /**
     * COnstructor for making map
     */
    public Map(ArrayList<Room> rooms){
        this.rooms = rooms;
        // setCurrentRoom(rooms.get(0)); // PLACEHOLDER
    }

    // temp solutions until we implement UI
    public void openMap(){
        isOpen = true;
    }

    /**
     * Closes the map
    */
    public void closeMap(){
        isOpen = false;
    }

    /**
     * Adds the rooms to the map
     * @param room
    */
    public void addRoomToMap(Room room){
        rooms.add(room);
    }

    /**
     * Gets the current room user is in
     * @return
    */
    public Room getCurrentRoom(){
        return currentRoom;
    }

    /**
     * Gets all rooms
     * @return
    */
    public ArrayList<Room> getRooms() {
        return rooms;
    }

    /**
     * Sets the room the user is in
     * @param room
    */
    public void setCurrentRoom(Room room){
        currentRoom = room;
    }

    /**
     * Sets current room player is in
     * @param roomName room we wanna be in
     */
    public void setCurrentRoom(String roomName){
        for (Room room : rooms) {
            if (room.getName().equals(roomName)) {
                currentRoom = room;
                return;
            }
        }
        System.err.println(roomName + " not a room! cannot beset as current room");
    }

    /**
     * Retrieves a room by its name
     * @param roomName searchee's name
     * @return room we found
     */
    public Room getRoomByName(String roomName) {
        for (Room room : rooms) { 
            if (room.getName().equals(roomName)) 
                return room;
        }
        return null;
    }

    /**
     * Sets progress state conditions on room and puzzles
     * @param save save we're basing the tracker changes on
     */
    public void loadFromSave(Progress save) {
        String currRoomName = save.getCurrentRoomName();
        if (currRoomName == null) {
            currRoomName = "Cell";
            System.out.println("Setting default room to " + currRoomName);
        }
        Room currRoom = getRoomByName(currRoomName);
        if (currRoom == null) {
            System.out.println("WARNING AHHHHHHHHH!!!!!! currRoom not found from save we're loading into Map class :(. Defaulting to first room");
            currRoom = rooms.get(0);
        } 
        save.setCurrentRoom(currRoom);
        setCurrentRoom(currRoom.getName());

        for (Room room : rooms) {
            for (Puzzle puzzle : room.getPuzzles()) {
                save.setPuzzleCompleted(room, puzzle);
            }
        }
    }
}