package com.model.Coding.Gameplay.InteractItems;

/**
 * An item puzzle
 * @author
 */
public class ItemPuzzle extends Puzzle {
    private Item requiredItem;

    /**
     * ItemPuzzle constructor. Calls super and initializes requiredItem
     * @param requiredItem The item required for the puzzle
     */
    public ItemPuzzle(String answer, String description, String name, Item requiredItem){
        super(answer, description, name);
        this.puzzleType = PuzzleType.ITEM;
        this.requiredItem = requiredItem;
    }

    /**
     * Boolean method to make sure user has a required item for the puzzle
     * @param item Item being checked
     * @return
     */
    public boolean requiredItem(Item item){
        if (item == null || requiredItem == null) 
            return false;
        return item.equals(requiredItem);
        
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

    public Item getRequiredItem() {
        return requiredItem;
    }

}