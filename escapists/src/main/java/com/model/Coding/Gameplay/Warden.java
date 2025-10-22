package com.model.Coding.Gameplay;

import com.model.Coding.Gameplay.InteractItems.Item;

public class Warden extends Character {
    private String roomID;
    private boolean hasItem;
/**
 * Sets constructor for warden
 * @param name
 * @param item
 * @param roomID
*/
    public Warden(String name, Item item, String roomID) {
        super(name, item);
        this.roomID = roomID;
        this.hasItem = false;
    }
/**
 * Warden can interact with the item
 * @param item
 * @return
*/
    public Item interact(Item item){
        this.item = item;
        hasItem = true;
        return null;
    }
/**
 * Gets the item for the warden
 * @return
*/
    public Item getItem() {
        return item;
    }
/**
 * Checks if the warden has an item
 * @return
*/
    public boolean getHasItem() {
        return hasItem;
    }
/**
 * Gets room ID of the warden
 * @return
*/
    public String getRoomID() {
        return roomID;
    }
}
