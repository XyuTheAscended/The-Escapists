package com.model.Coding.Gameplay.Map;

import java.util.ArrayList;

public class Map {
    private boolean isOpen = false;
    private Room currentRoom;
    private ArrayList<Room> rooms;

    public Map(){
        rooms = new ArrayList<>();
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

    public void setCurrentRoom(Room room){
        currentRoom = room;
    }

    // temp testing method
    public static void main(String[] args) {
        Map map = new Map();
        map.openMap();
        map.closeMap();
        map.getCurrentRoom();
        map.setCurrentRoom(null);
    }
}