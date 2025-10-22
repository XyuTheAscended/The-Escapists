package com.model.Coding.Gameplay.InteractItems;

public class Item {
    private int itemId;
    private String name;
    private String description;

/**
 * Sets name, description, and ID of items
 * @param itemId
 * @param name
 * @param description
 */
    public Item(int itemId, String name, String description){
        this.itemId = itemId;
        this.name = name;
        this.description = description;
    }
/**
 * Gets itemID
 * @return
 */
    public int getItemId(){
        return itemId;

    }
/**
 * Gets name of Item
 * @return
 */
    public String getName(){
        return name;

    }

    public boolean equals(Item otherItem) {
        return this.itemId == otherItem.itemId;
    }
/**
 * Gets decription of item
 * @return
 */
    public String getDescription(){
        return description;
    
    }
}
