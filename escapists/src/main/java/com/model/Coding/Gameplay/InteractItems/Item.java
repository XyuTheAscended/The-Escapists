package com.model.Coding.Gameplay.InteractItems;

import java.util.ArrayList;

import java.util.ArrayList;

/**
 * An Item
 * @author
 */
public class Item {
    private int itemId;
    private String name;
    private String description;
    public static ArrayList<Item> allItemsEver = new ArrayList<>();

    /**
     * Sets name, description, and ID of items
     * @param itemId ID of the item object
     * @param name Name of the item
     * @param description Description of the item
     */    private Item(int itemId, String name, String description){
        this.itemId = itemId;
        this.name = name;
        this.description = description;
    }

    public static Item cacheItem(String name, String description) {
        if (searchForItem(name) != null) { // this search call is case sensitive (so for ex. these two cant exit together: pIsToL, PISTOL)
            throw new RuntimeException(name + " has already been cached.");
        }

        int nextItemId = allItemsEver.size(); // as long as items never get removed, this will always equal size of the items cache
        Item item = new Item(nextItemId, name, description);
        allItemsEver.add(item);
        
        return item;
    }

    public static Item searchForItem(String name) {
        return searchForItem(name, false);
    }

    public static Item searchForItem(String name, boolean caseSensitive) {
        return searchForItemInList(allItemsEver, name, caseSensitive);
    }

    public static Item searchForItemInList(ArrayList<Item> list, String name, boolean caseSensitive) {
        for (Item item : list) {
            if (caseSensitive) { 
                if (item.getName().equals(name)) 
                    return item;
            } else {
                if (item.getName().equalsIgnoreCase(name)) {
                    return item; 
                }
            }
        }

        return null;
    }

    public static int searchForItemId(String name) {
        Item item = searchForItem(name);
        return item != null ? item.getItemId() : null;
    }

    

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
