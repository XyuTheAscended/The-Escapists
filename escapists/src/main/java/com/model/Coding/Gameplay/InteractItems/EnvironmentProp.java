package com.model.Coding.Gameplay.InteractItems;

public class EnvironmentProp {
    private String name;
    private Item item;
    private boolean isCollected;


public EnvironmentProp(String name, Item item){
    this.name = name;
    this.item = item;
    this.isCollected = false;
}

public void interact(Inventory playerInventory){
    if (!isCollected) {
        playerInventory.addItem(item);
        isCollected = true;
    }
}

public Item getItem(){
    return item;
    
}

}
