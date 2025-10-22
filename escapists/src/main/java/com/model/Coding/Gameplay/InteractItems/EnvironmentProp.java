package com.model.Coding.Gameplay.InteractItems;

public class EnvironmentProp {
    private String name;
    private Item item;
    private boolean isCollected;

/**
 * Sets variables
 * @param name
 * @param item
*/
public EnvironmentProp(String name, Item item){
    this.name = name;
    this.item = item;
    this.isCollected = false;
}
/**
 * Interacts with the players inventory
 * @param playerInventory
*/
public void interact(Inventory playerInventory){
    if (!isCollected) {
        playerInventory.addItem(item);
        isCollected = true;
    }
}
/**
 * Gets the item
 * @return
*/
public Item getItem(){
    return item;
    
}

}
