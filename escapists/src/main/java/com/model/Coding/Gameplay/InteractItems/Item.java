package com.model.Coding.Gameplay.InteractItems;

import java.util.ArrayList;

public class Item {
    private int itemId;
    private String name;
    private String description;
    private static ArrayList<Item> allItemsEver = new ArrayList<>();

    private Item(int itemId, String name, String description){
        this.itemId = itemId;
        this.name = name;
        this.description = description;
    }

    public static Item cacheItem(String name, String description) {
        if (searchForItem(name) != null) {
            throw new RuntimeException(name + " has already been cached.");
        }

        int nextItemId = allItemsEver.size(); // as long as items never get removed, this will always equal size of the items cache
        Item item = new Item(nextItemId, name, description);

        return item;
    }

    public static Item searchForItem(String name) {
        for (Item item : allItemsEver) {
            if (item.getName().equals(name)) 
                return item;
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

    public String getName(){
        return name;

    }

    public boolean equals(Item otherItem) {
        return this.itemId == otherItem.itemId;
    }

    public String getDescription(){
        return description;
    
    }
}
