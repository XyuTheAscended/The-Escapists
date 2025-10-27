package com.model.Coding.Gameplay.Map;

import java.util.ArrayList;

import com.model.Coding.Data.DataManager;
import com.model.Coding.Gameplay.InteractItems.Puzzle;
import com.model.Coding.Progress.Progress;

/**
 * Map
 * @author
 */
public class Map {
    private boolean isOpen = false;
    private Room currentRoom;
    private ArrayList<Room> rooms;

    public Map(){
        rooms = DataManager.getInstance().loadRooms();
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

    public void setCurrentRoom(String roomName){
        for (Room room : rooms) {
            if (room.getName().equals(roomName)) {
                currentRoom = room;
                return;
            }
        }
        System.err.println(roomName + " not a room! cannot beset as current room");
    }

    private Room getRoomByName(String roomName) {
        for (Room room : rooms) { 
            if (room.getName().equals(roomName)) 
                return room;
        }
        return null;
    }

    public void loadFromSave(Progress save) {
        String currRoomName = save.getCurrentRoomName();
        if (currRoomName == null) {
            currRoomName = "Cell";
            System.out.println("Setting default room to " + currRoomName);
        }
        Room currRoom = getRoomByName(currRoomName);
        save.setCurrentRoom(currRoom);
        setCurrentRoom(currRoom.getName());

        for (Room room : rooms) {
            for (Puzzle puzzle : room.getPuzzles()) {
                save.setPuzzleCompleted(room, puzzle);
            }
        }
    }
}