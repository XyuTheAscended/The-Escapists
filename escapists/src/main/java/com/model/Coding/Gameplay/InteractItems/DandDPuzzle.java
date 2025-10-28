package com.model.Coding.Gameplay.InteractItems;
import java.util.ArrayList;
import com.model.Coding.Gameplay.InteractItems.Puzzle;

/**
 * A drag and drop puzzle
 * @author Tyler Norman
 */
public class DandDPuzzle extends Puzzle {
    private ArrayList<Item> requiredItems;
    private ArrayList<Item> placedItems;

    /**
     * Sets the variables in constructor
     * @param answer Answer to the puzzle
     * @param description Description of the puzzle
     * @param name Name of the puzzle
     * @param requiredItems Required items
    */
    public DandDPuzzle(String answer, String description, String name, ArrayList<Item> requiredItems){
        super(answer, description, name);
        this.puzzleType = PuzzleType.DND;
        this.requiredItems = requiredItems;
        this.placedItems = new ArrayList<>();
    }

    /**
     * Insertion for the DNDItem
     * @param item Item being inserted
     * @param playerInventory The players inventory
     * @return Boolean, true of item is inserted, false otherwise
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
     * @return Boolean, true if all the items are places, false otherwise
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
     * @return ArrayList of placed items
    */
    public ArrayList<Item> getPlacedItems(){
        return placedItems;
    }

    /**
     * gets required items
     * @return ArrayList of required items
    */
    public ArrayList<Item> getRequiredItems(){
        return requiredItems;
    }
}
