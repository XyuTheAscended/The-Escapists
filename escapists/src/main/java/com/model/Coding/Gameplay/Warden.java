package com.model.Coding.Gameplay;
import com.model.Coding.Gameplay.InteractItems.Item;

/**
 * A warden character
 * @author Mason Adams
 */
public class Warden extends Character {
    private String roomID;
    private boolean hasItem;

    /**
     * Sets constructor for warden
     * @param name Name of the warden
     * @param item Item the warden has
     * @param roomID ID of the room the warden is tied to
    */
    public Warden(String name, Item item, String roomID) {
        super(name, item);
        this.roomID = roomID;
        this.hasItem = false;
    }

    /**
     * Gives the warden an item
     * @param item An item object
    */
    public void interact(Item item){
        this.item = item;
        hasItem = true;
    }

    /**
     * Gets the item for the warden
     * @return Item object
    */
    public Item getItem() {
        return item;
    }

    /**
     * Checks if the warden has an item
     * @return Boolean, true if the warden has an item, false otherwise
    */
    public boolean getHasItem() {
        return hasItem;
    }

    /**
     * Gets room ID
     * @return String containing the room ID
    */
    public String getRoomID() {
        return roomID;
    }
}
