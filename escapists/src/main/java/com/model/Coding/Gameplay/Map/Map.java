package com.model.Coding.Gameplay.Map;

import java.util.ArrayList;

public class Map {
    private boolean isOpen;
    private Room currentRoom;
    private ArrayList<Room> rooms;

    public Map(){

    }

    public void openMap(){

    }

    public void closeMap(){

    }

    public void toggleMap(){

    }

    public Room getCurrentRoom(){
        return currentRoom;
    }

    public void setCurrentRoom(Room room){
    
    }

    // temp testing method
    public static void main(String[] args) {
        Map map = new Map();
        map.openMap();
        map.closeMap();
        map.toggleMap();
        map.getCurrentRoom();
        map.setCurrentRoom(null);
    }
}