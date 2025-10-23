package com.model.Coding.Gameplay.InteractItems;

/**
 * An environmental prop
 * @author
 */
public class EnvironmentProp {
    private String name;
    private Item item;
    private boolean isCollected;

    /**
     * Sets variables
     * @param name Name of the prop
     * @param item Name of the item
    */
    public EnvironmentProp(String name, Item item){
        this.name = name;
        this.item = item;
        this.isCollected = false;
    }

    /**
     * Interacts with the players inventory
     * @param playerInventory The players inventory
    */
    public void interact(Inventory playerInventory){
        if (!isCollected) {
            playerInventory.addItem(item);
            isCollected = true;
        }
    }

    /**
     * Gets the item
     * @return Item object
    */
    public Item getItem(){
        return item;

    }
}
