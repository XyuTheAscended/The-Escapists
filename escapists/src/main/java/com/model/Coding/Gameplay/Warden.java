package com.model.Coding.Gameplay;

import com.model.Coding.Gameplay.InteractItems.Item;

public class Warden extends Character {
    private String roomID;
    private boolean hasItem;

    public Warden(String name, Item item, String roomID) {
        super(name, item);
        this.roomID = roomID;
        this.hasItem = false;
    }

    public Item interact(Item item){
        this.item = item;
        hasItem = true;
        return null;
    }

    public Item getItem() {
        return item;
    }

    public boolean getHasItem() {
        return hasItem;
    }

    public String getRoomID() {
        return roomID;
    }
}
