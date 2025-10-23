package com.model.Coding.Gameplay.InteractItems;

public class ItemPuzzle extends Puzzle {
    private Item requiredItem;

    public ItemPuzzle(String answer, String description, String name, Item requiredItem){
        super(answer, description, name);
        this.puzzleType = PuzzleType.ITEM;
        this.requiredItem = requiredItem;
    }

    public boolean requiredItem(Item item){
        if (item == null || requiredItem == null)
            return false;
        return item.equals(requiredItem);
        
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

    public Item getRequiredItem() {
        return requiredItem;
    }

}