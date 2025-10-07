package com.model.Coding.Gameplay.InteractItems;

public class Item {
    private int itemId;
    private String name;
    private String description;


    public Item(int itemId, String name, String description){
        this.itemId = itemId;
        this.name = name;
        this.description = description;
    }

    public int getItemId(){
        return itemId;

    }

    public String getName(){
        return name;

    }

    public String getDescription(){
        return description;
    
    }
}
