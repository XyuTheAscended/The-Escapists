package com.model.Coding.Gameplay.InteractItems;
import java.util.ArrayList;
public class DandDPuzzle {
    private ArrayList<Item> requiredItems;
    private ArrayList<Item> placedItems;

    public DandDPuzzle(String answer, String description, String name, ArrayList<Item> requiredItems){
        super();
        this.requiredItems = requiredItems;
        this.placedItems = new ArrayList<>();
    }

    public boolean DandDItem(Item item, Inventory playerInventory) {
        if (item == null || playerInventory == null){
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

        if (!placedItems.contains(item)) {
            placedItems.add(item);
            playerInventory.removeItem(item);
        }

        checkCompletion();
        return true;
    }

    private void checkCompletion() {
        if (placedItems.size() != requiredItems.size()){
            return;
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
                return;
            }
        }
    }

    public ArrayList<Item> getPlacedItems(){
        return placedItems;
    }

    public ArrayList<Item> getRequiredItems(){
        return requiredItems;
    }
}
