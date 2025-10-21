package com.model.Coding.Gameplay.InteractItems;
import java.util.ArrayList;
import com.model.Coding.Gameplay.InteractItems.Puzzle;

public class DandDPuzzle extends Puzzle {
    private ArrayList<Item> requiredItems;
    private ArrayList<Item> placedItems;

    public DandDPuzzle(String answer, String description, String name, ArrayList<Item> requiredItems){
        super(answer, description, name);
        this.requiredItems = requiredItems;
        this.placedItems = new ArrayList<>();
    }

    public boolean insertDNDItem(Item item, Inventory playerInventory) {
        if (item == null || playerInventory == null){
            return false;
        }

        if (!requiredItems.contains(item) || placedItems.contains(item)) {
            return false; 
        }

        boolean hasItem = false;
        for (Item invItem : playerInventory.getItems()){
            if (invItem.getItemId() == item.getItemId()) {
                hasItem = true;
                break;
            }
        }

        if (!hasItem){
            return false;
        }

        placedItems.add(item);
        playerInventory.removeItem(item);

        return true;
    }

    public boolean allItemsPlaced() {
        if (placedItems.size() != requiredItems.size()){
            return false;
        }

        for (Item required : requiredItems) {
            boolean found = false;
            for (Item placed : placedItems) {
                if (placed.getItemId() == required.getItemId()){
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

    public ArrayList<Item> getPlacedItems(){
        return placedItems;
    }

    public ArrayList<Item> getRequiredItems(){
        return requiredItems;
    }
}
