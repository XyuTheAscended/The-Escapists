package com.model.Coding.Gameplay.InteractItems;

import java.util.ArrayList;

import java.util.ArrayList;

/**
 * An Item class
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
     */    
    private Item(int itemId, String name, String description){
        this.itemId = itemId;
        this.name = name;
        this.description = description;
    }

    public Item(String string, int i) {
        //TODO Auto-generated constructor stub
    }

    /**
     * Wrapper function for creating items ONLY if they
     * have not been created before ever
     * If an item with same name already exists, no new
     * item is made
     * @param name
     * @param description
     * @return Item that was either created just now or had been created
     */
    public static Item cacheItem(String name, String description) {
        Item preexistingItem = searchForItem(name);
        if (preexistingItem != null) { // this search call is case sensitive (so for ex. these two cant exit together: pIsToL, PISTOL)
            return preexistingItem;
        }

        int nextItemId = allItemsEver.size(); // as long as items never get removed, this will always equal size of the items cache
        Item item = new Item(nextItemId, name, description);
        allItemsEver.add(item);
        
        return item;
    }

    /**
     * Searches if item was created before by checking item cache
     * @param name searchee's name
     * @return thing tha twas found
     */
    public static Item searchForItem(String name) {
        return searchForItem(name, false);
    }

    /**
     * Same function as above for searching items but has a case sensitive functionality
     * @param name searchee's name
     * @param caseSensitive whether we wanna search by case sensitivity or not
     * @return item found 
     */
    public static Item searchForItem(String name, boolean caseSensitive) {
        return searchForItemInList(allItemsEver, name, caseSensitive);
    }

    /**
     * Searches for item in a given list of items
     * @param list list in question
     * @param name name of item we're searching for
     * @param caseSensitive whether we wanna search with case sensitivity
     * @return item found 
     */
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

    /**
     * search for an item and gets it BUT returns the Id of the item instead of the item itself
     * @param name name of searchee
     * @return item id of the found item
     */
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

    /**
     * Compares two items together based on item id
     * @param otherItem item we're comparing with the curr item
     * @return equivalency boolean
     */
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
