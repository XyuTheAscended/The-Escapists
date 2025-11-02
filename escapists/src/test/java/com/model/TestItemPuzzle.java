package com.model;
import com.model.Coding.Gameplay.InteractItems.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
/**
 * Test for ItemPuzzle Class
 * Tests item-checking and inventory-based puzzle logic only.
 * @author Tyler Norman
 */
public class TestItemPuzzle {
 private Item requiredItem;
    private Item differentItem;
    private Inventory inventory;
    private ItemPuzzle itemPuzzle;

    @Before
    public void setUp() {
        // Setup items
        requiredItem = new Item("Key", 1);
        differentItem = new Item("Keycard",2);

        // Setup inventory
        inventory = new Inventory();
        inventory.addItem(requiredItem);

        // Setup puzzle
        itemPuzzle = new ItemPuzzle("open", "A locked chest", "Chest Puzzle", requiredItem);
    }

    @Test
    public void testRequiredItemMatches() {
        assertTrue("Should return true when item matches the required item",
                itemPuzzle.requiredItem(requiredItem));
    }

    @Test
    public void testRequiredItemDoesNotMatch() {
        assertFalse("Should return false when item does not match the required item",
                itemPuzzle.requiredItem(differentItem));
    }

    @Test
    public void testRequiredItemNullCases() {
        assertFalse("Should return false when given item is null",
                itemPuzzle.requiredItem(null));

        ItemPuzzle nullReqPuzzle = new ItemPuzzle("answer", "desc", "name", null);
        assertFalse("Should return false when puzzle's required item is null",
                nullReqPuzzle.requiredItem(requiredItem));
    }

    @Test
    public void testUseItemFoundInInventory() {
        assertTrue("Should return true when required item is in inventory",
                itemPuzzle.useItem(inventory));
    }

    @Test
    public void testUseItemNotInInventory() {
        Inventory emptyInventory = new Inventory();
        assertFalse("Should return false when required item not in inventory",
                itemPuzzle.useItem(emptyInventory));
    }

    @Test
    public void testUseItemNullInventoryOrItem() {
        assertFalse("Should return false when inventory is null",
                itemPuzzle.useItem(null));

        ItemPuzzle nullReqPuzzle = new ItemPuzzle("answer", "desc", "name", null);
        assertFalse("Should return false when required item is null",
                nullReqPuzzle.useItem(inventory));
    }

    @Test
    public void testGetRequiredItem() {
        assertEquals("getRequiredItem should return the same item from constructor",
                requiredItem, itemPuzzle.getRequiredItem());
    }
}

