package com.model.Coding.Gameplay;
import com.model.Coding.Gameplay.InteractItems.Item;

/**
 * A NPC Character
 * @author Tyler Norman
 */
public class Character {
    protected String characterName;
    protected Item item;

    /**
     * Initializes characterName and item
     * @param name Name of the character
     * @param item Item the character has
     */
    public Character(String name, Item item){
        this.characterName = name;
        this.item = item;
    }

    // expand this for each character
    /**
     * Gets a string and just slaps the character's name at the beginning
     * of that string, turning that into a new string
     * @param dialogue Thing the character is supposed to say
     * @return The dialogue with the character's name prepended
     */
    public String speak(String dialogue ){
        return (characterName + ": " + dialogue);
    }
    /**
     * Character can interact with items
     * @return Item object
    */
    public Item interact(){
        return item;
    }
}
