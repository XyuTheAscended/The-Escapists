package com.model.Coding.Gameplay.InteractItems;
import java.util.ArrayList;
import com.model.Coding.Gameplay.InteractItems.Puzzle;

public class DandDPuzzle extends Puzzle {
    private ArrayList<Item> requiredItems;
    private ArrayList<Item> placedItems;
    /**
     * Sets the variables in constructor
     * @param answer
     * @param description
     * @param name
     * @param requiredItems
    */
    public DandDPuzzle(String answer, String description, String name, ArrayList<Item> requiredItems){
        super(answer, description, name);
        this.requiredItems = requiredItems;
        this.placedItems = new ArrayList<>();
    }

    /**
     * Insertion for the DNDItem
     * @param item
     * @param playerInventory
     * @return
    */
    public boolean insertDNDItem(Item item, Inventory playerInventory) {
        if (item == null || playerInventory == null){
            return false;
        }

        if (!requiredItems.contains(item) || placedItems.contains(item)) {
            return false; 
        }

        boolean hasItem = playerInventory.hasItem(item);

        if (!hasItem){
            return false;
        }

        placedItems.add(item);
        playerInventory.removeItem(item);

        return true;
    }

/**
 * Checks if all the items are placed
 * @return
*/
    public boolean allItemsPlaced() {
        if (placedItems.size() != requiredItems.size()){
            return false;
        }

        for (Item required : requiredItems) {
            boolean found = false;
            for (Item placed : placedItems) {
                if (placed.equals(required)){
                    found = true;
                    break;
                }
            }

            if (!found){
                return false;
            }
        }

        return true;
    }

    /**
     * gets placed items
     * @return
    */
    public ArrayList<Item> getPlacedItems(){
        return placedItems;
    }
/**
 * gets required items
 * @return
*/
    public ArrayList<Item> getRequiredItems(){
        return requiredItems;
    }
}
