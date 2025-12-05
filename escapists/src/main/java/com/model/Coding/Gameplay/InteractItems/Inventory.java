package com.model.Coding.Gameplay.InteractItems;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * The players inventory
 * @author Mason Adams
 */
public class Inventory {
    private ArrayList<Item> items;

    private Consumer<Item> itemAddedCallback;
    private Consumer<Item> itemRemovedCalback;


    /**
     * Initialized ArrayList representing the inventory
     */
    public Inventory(){
        items = new ArrayList<>();
    }

    /**
     * Adds item to the inventory
     * @param item An item object
     */
    public void addItem(Item item){
        items.add(item);
        if (itemAddedCallback != null) itemAddedCallback.accept(item);
    }

    /**
     * Removes item from the inventory
     * @param item An item object
     */
    public void removeItem(Item item){
        items.remove(item);
        if (itemRemovedCalback != null) itemRemovedCalback.accept(item);
    }

    /**
     * Sets callback that runs when an item is added
     * @param cb
     */
    public void setItemAddedCallback(Consumer<Item> cb) {
        this.itemAddedCallback = cb;
    }

    /**
     * Sets callback that runs when an item is removed 
     * @param cb
     */
    public void setItemRemovedCallback(Consumer<Item> cb) {
        this.itemRemovedCalback = cb;
    }

    /**
     * Gets the inventory as an ArrayList
     * @return ArrayList of items
     */
    public ArrayList<Item> getItems(){
        return items;
    }

    /**
     * Retrieves a singular item from the inventory
     * @param index The index of the item being returned
     * @return An item object
     */
    public Item getItem(int index) {
        return items.get(index);
    }

    /**
     * Retrieves item by name
     * @param itemName name of item we want
     * @return item we found
     */
    public Item getItem(String itemName) {
        for (Item item : items) {
            // case is ignored here as well when search for items 
            // (this will break if two items ever have similar names but different cases but that should be impossible)
            if (item.getName().equalsIgnoreCase(itemName)) 
                return item;
        }
        return null;
    }

    /**
     * Checks the inventory for the given item
     * @param item The item object being checked for
     * @return Boolean, true of the item is in the inventory, false otherwise.
     */
    public boolean hasItem(Item item) {
        for (Item invItem : items){
            if (invItem.equals(item)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasItem(String itemName) {
        for (Item invItem : items){
            // Case is ignored when searching for an item because it should be impossible to have two items with same name but diff case loaded
            if (invItem.getName().equalsIgnoreCase(itemName)) { 
                return true;
            }
        }
        return false;
    }

    /**
     * Temporary display of the inventory until UI is implemented
     * @return String containing the names of the items in the inventory
     */
    public String displayInventory() {
        String stringInven = "";
        for (Item item: items) {
            stringInven += (" | " + item.getName() + " | ");
        }
        return ("[" + stringInven + "]");
    }
}
