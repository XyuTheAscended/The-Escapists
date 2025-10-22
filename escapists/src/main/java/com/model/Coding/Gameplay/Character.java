package com.model.Coding.Gameplay;
import com.model.Coding.Gameplay.InteractItems.Item;

// characters speak to you and maybe give you an item
public class Character {
    protected String characterName;
    protected Item item;

    public Character(String name, Item item){
        this.characterName = name;
        this.item = item;
    }

    // expand this for each character
    public String speak(String dialogue ){
        return (characterName + ": " + dialogue);
    }
/**
 * Character can interact with items
 * @return
*/
    public Item interact(){
        return item;
    }
}
