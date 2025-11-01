package com.model;

import com.model.Coding.Gameplay.InteractItems.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * tests for the EnvironmentProp class.
 * Ensures correct inventory interaction and one-time item collection behavior.
 * @author Tyler Norman
 */
public class TestEnvProp {

    private EnvironmentProp environmentProp;
    private Inventory inventory;
    private Item testItem;

    @Before
    public void setUp() {
        // Create a simple item and inventory
        testItem = new Item("Gem", 1);
        inventory = new Inventory();

        // Create an environment prop holding that item
        environmentProp = new EnvironmentProp("Treasure Chest", testItem);
    }

    @Test
    public void testGetItemReturnsSameItem() {
        assertEquals("getItem should return the same item passed into the constructor",
                testItem, environmentProp.getItem());
    }

    @Test
    public void testInteractAddsItemToInventory() {
        // Before interacting, inventory should be empty
        assertTrue("Inventory should start empty", inventory.getItems().isEmpty());

        // Interact once
        environmentProp.interact(inventory);

        // After interaction, the item should be added
        assertTrue("Inventory should now contain the item after first interaction",
                inventory.getItems().contains(testItem));
    }

    @Test
    public void testInteractOnlyAddsOnce() {
        // Interact twice
        environmentProp.interact(inventory);
        environmentProp.interact(inventory);

        // The same item should only appear once
        int occurrences = 0;
        for (Item item : inventory.getItems()) {
            if (item.equals(testItem)) occurrences++;
        }

        assertEquals("Item should only be added once even with multiple interactions",
                1, occurrences);
    }

    @Test
    public void testInteractWithNullInventoryDoesNothing() {
        // Should not throw an exception
        try {
            environmentProp.interact(null);
        } catch (Exception e) {
            fail("interact(null) should not throw an exception");
        }
    }
}

