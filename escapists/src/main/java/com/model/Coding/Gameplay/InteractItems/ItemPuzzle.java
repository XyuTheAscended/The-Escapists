package com.model.Coding.Gameplay.InteractItems;

public class ItemPuzzle extends Puzzle {
    private Item requiredItem;
/**
 * gets required item
 * @param requiredItem
 */
    public ItemPuzzle(Item requiredItem){
        super("", "", "");
        this.requiredItem = requiredItem;
    }
/**
 * Boolean method to make sure user has a required item for the puzzle
 * @param item
 * @return
 */
    public boolean requiredItem(Item item){
        if (item == null || requiredItem == null)
            return false;
            return item.getItemId() == requiredItem.getItemId();
        
    }
/**
 * Boolean to check if the user can use the item in their inventory
 * @param inventory
 * @return
 */
    public boolean useItem(Inventory inventory){
        if (inventory == null || requiredItem == null)
            return false;
            
        for (Item item : inventory.getItems()){
            if (item.getItemId() == requiredItem.getItemId()){
                return true;
            }
        }  
    return false;
    }

}