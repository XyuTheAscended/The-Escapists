package com.model.Coding.Gameplay.InteractItems;
import java.util.ArrayList;

/**
 * The players inventory
 * @author Mason Adams
 */
public class Inventory {
    private ArrayList<Item> items;

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
    }

    /**
     * Removes item from the inventory
     * @param item An item object
     */
    public void removeItem(Item item){
        items.remove(item);
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
