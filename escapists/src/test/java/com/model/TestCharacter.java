package com.model;

import com.model.Coding.Gameplay.Character;
import com.model.Coding.Gameplay.InteractItems.Item;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

/**
 * Tests for com.model.Coding.Gameplay.Character (non-getter logic).
 */
public class TestCharacter {

    private Object getFieldValue(Object target, String fieldName) throws Exception {
        java.lang.reflect.Field f = target.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        return f.get(target);
    }

    @Test
    public void testConstructorStoresFields() throws Exception {
        Item mockItem = Mockito.mock(Item.class);
        Character c = new Character("Bob", mockItem);

        String nameValue = (String) getFieldValue(c, "characterName");
        assertEquals("Character name should be stored", "Bob", nameValue);

        Item itemValue = (Item) getFieldValue(c, "item");
        assertSame("Item reference should be stored", mockItem, itemValue);

    }

    @Test
    public void testSpeakPrependsCharacterName() {
        Item dummyItem = Mockito.mock(Item.class);
        Character c = new Character("Alice", dummyItem);

        String line = c.speak("Hello there!");
        assertEquals("Alice: Hello there!", line);
    }

    @Test
    public void testSpeakHandlesEmptyDialogue() {
        Item dummyItem = Mockito.mock(Item.class);
        Character c = new Character("Charlie", dummyItem);

        String line = c.speak("");
        assertEquals("Charlie: ", line);
    }

    @Test
    public void testSpeakHandlesNullDialogueGracefully() {
        Item dummyItem = Mockito.mock(Item.class);
        Character c = new Character("Dana", dummyItem);

        // Current implementation will concatenate "null" string if dialogue is null.
        String line = c.speak(null);
        assertEquals("Dana: null", line);
    }

    @Test
    public void testInteractReturnsSameItem() {
        Item mockItem = Mockito.mock(Item.class);
        Character c = new Character("Eve", mockItem);

        assertSame("interact() should return the same Item reference", mockItem, c.interact());
    }

    @Test
    public void testInteractWhenItemIsNull() {
        Character c = new Character("Ghost", null);
        assertNull("interact() should return null if character has no item", c.interact());
    }

    @Test
    public void testSpeakWhenNameIsNull() {
        Item dummyItem = Mockito.mock(Item.class);
        Character c = new Character(null, dummyItem);

        // Java will stringify null to "null"
        String line = c.speak("Hi");
        assertEquals("null: Hi", line);
    }
}
