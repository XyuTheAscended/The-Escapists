package com.model;

import com.model.Coding.Gameplay.InteractItems.Item;
import com.model.Coding.Gameplay.Warden;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

/**
 * Tests for com.model.Coding.Gameplay.Warden (behavioral, excludes simple getters/setters)
 */
public class TestWarden {

    @Test
    public void testConstructorInitializesAndHasNoItem() {
        Item mockItem = Mockito.mock(Item.class);
        Warden w = new Warden("Waldo", mockItem, "room-1");

        // initial hasItem should be false (constructor sets it to false)
        assertFalse("New warden should not have item by default", w.getHasItem());

        // initial item should be the one passed in by constructor (character.item is inherited)
        assertSame("Constructor-stored item should be retrievable", mockItem, w.getItem());

        // roomID stored correctly
        assertEquals("room-1", w.getRoomID());
    }

    @Test
    public void testInteractSetsItemAndHasItemTrue() {
        Item initial = Mockito.mock(Item.class);
        Item handed = Mockito.mock(Item.class);

        Warden w = new Warden("Warden", initial, "R2");

        // precondition
        assertFalse(w.getHasItem());
        assertSame(initial, w.getItem());

        // interact with a new item
        w.interact(handed);

        assertTrue("After interact(item) hasItem should be true", w.getHasItem());
        assertSame("After interact(item) the item should be the handed one", handed, w.getItem());
    }

    @Test
    public void testMultipleInteractsReplaceItem() {
        Item a = Mockito.mock(Item.class);
        Item b = Mockito.mock(Item.class);

        Warden w = new Warden("W", null, "R3");

        // first interact with a
        w.interact(a);
        assertTrue(w.getHasItem());
        assertSame(a, w.getItem());

        // then interact with b -> should replace
        w.interact(b);
        assertTrue(w.getHasItem());
        assertSame(b, w.getItem());
    }

    @Test
    public void testInteractWithNullSetsHasItemTrueAndItemNull() {
        Item original = Mockito.mock(Item.class);
        Warden w = new Warden("Nullie", original, "R4");

        // interact with null
        w.interact(null);

        // Spec: method sets item to given value (null) and sets hasItem true
        assertTrue("interact(null) should still set hasItem to true", w.getHasItem());
        assertNull("interact(null) should set item to null", w.getItem());
    }

    @Test
    public void testConstructorAcceptsNullsWithoutThrowing() {
        // Should not throw even if name, item, or roomID are null
        Warden w = null;
        try {
            w = new Warden(null, null, null);
        } catch (Exception e) {
            fail("Constructor should not throw when passed nulls: " + e.getMessage());
        }

        // state checks
        assertNotNull("Warden instance should be created", w);
        assertNull("roomID should be null when constructed with null", w.getRoomID());
        assertFalse("hasItem defaults to false", w.getHasItem());
        assertNull("item should be null when constructed with null", w.getItem());
    }

    @Test
    public void testSpeakInheritedBehaviorStillWorks() {
        Item mockItem = Mockito.mock(Item.class);
        Warden w = new Warden("Grim", mockItem, "R5");

        String said = w.speak("Stay back");
        assertEquals("Grim: Stay back", said);
    }
}
