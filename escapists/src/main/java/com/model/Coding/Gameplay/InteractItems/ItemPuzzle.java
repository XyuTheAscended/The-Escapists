package com.model.Coding.Gameplay.InteractItems;

public class ItemPuzzle extends Puzzle {
    private Item requiredItem;

    public ItemPuzzle(Item requiredItem){
        super("", "", "");
        this.puzzleType = PuzzleType.ITEM;
        this.requiredItem = requiredItem;
    }

    public boolean requiredItem(Item item){
        if (item == null || requiredItem == null)
            return false;
            return item.getItemId() == requiredItem.getItemId();
        
    }

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