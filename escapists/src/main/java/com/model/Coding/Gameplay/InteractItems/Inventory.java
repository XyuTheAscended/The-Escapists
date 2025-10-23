package com.model.Coding.Gameplay.InteractItems;
import java.util.ArrayList;

public class Inventory {
    private ArrayList<Item> items;

    public Inventory(){
        items = new ArrayList<>();
    }

    public void addItem(Item item){
        items.add(item);
    }

    public void removeItem(Item item){
        items.remove(item);
    }

    public ArrayList<Item> getItems(){
        return items;
    }

    // gets a certain item from the inventory
    public Item getItem(int index) {
        return items.get(index);
    }

    public Item getItem(String itemName) {
        for (Item item : items) {
            // case is ignored here as well when search for items 
            // (this will break if two items ever have similar names but different cases but that should be impossible)
            if (item.getName().equalsIgnoreCase(itemName)) 
                return item;
        }
        return null;
    }

    public boolean hasItem(Item item) {
        for (Item invItem : items){
            if (invItem.equals(item)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasItem(String itemName) {
        for (Item invItem : items){
            // Case is ignored when searching for an item because it should be impossible to have two items with same name but diff case loaded
            if (invItem.getName().equalsIgnoreCase(itemName)) { 
                return true;
            }
        }
        return false;
    }

    public String displayInventory() {
        String stringInven = "";
        for (Item item: items) {
            stringInven += (" | " + item.getName() + " | ");
        }
        return ("[" + stringInven + "]");
    }
}
