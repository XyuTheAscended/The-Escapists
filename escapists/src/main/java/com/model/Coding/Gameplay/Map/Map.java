package com.model.Coding.Gameplay.Map;

import java.util.ArrayList;

import com.model.Coding.Data.DataManager;

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

    public void closeMap(){
        isOpen = false;
    }

    public void addRoomToMap(Room room){
        rooms.add(room);
    }

    public Room getCurrentRoom(){
        return currentRoom;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

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

    // temp testing method
    // public static void main(String[] args) {
    //     Map map = new Map();
    //     map.openMap();
    //     map.closeMap();
    //     map.getCurrentRoom();
    //     map.setCurrentRoom(null);
    // }
}