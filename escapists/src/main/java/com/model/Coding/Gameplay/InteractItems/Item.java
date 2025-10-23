package com.model.Coding.Gameplay.InteractItems;

/**
 * An Item
 * @author
 */
public class Item {
    private int itemId;
    private String name;
    private String description;

    /**
     * Sets name, description, and ID of items
     * @param itemId ID of the item object
     * @param name Name of the item
     * @param description Description of the item
     */
    public Item(int itemId, String name, String description){
        this.itemId = itemId;
        this.name = name;
        this.description = description;
    }
    /**
     * Gets itemID
     * @return ID of item
     */
    public int getItemId(){
        return itemId;

    }
    /**
     * Gets name of Item
     * @return Name of the item
     */
    public String getName(){
        return name;

    }

    public boolean equals(Item otherItem) {
        return this.itemId == otherItem.itemId;
    }

    /**
     * Gets description of item
     * @return Description of the item
     */
    public String getDescription(){
        return description;
    
    }
}
