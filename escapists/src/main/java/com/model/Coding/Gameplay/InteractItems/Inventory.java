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
}
