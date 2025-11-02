package com.model;

import com.model.Coding.Gameplay.InteractItems.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

/**
 * Tests for DandDPuzzle class.
 * Tests drag-and-drop puzzle logic for correct insertion and completion behavior. 
 * Tests insertion logic, placement checks, and completion detection.
 * Each test isolates one behavior only.
 * @author Tyler Norman
 */
public class TestDandDPuzzle {

    private DandDPuzzle dndPuzzle;
    private Inventory inventory;
    private Item item1;
    private Item item2;
    private Item extraItem;
    private ArrayList<Item> requiredItems;

    @Before
    public void setUp() {
        // Setup items
        item1 = new Item("Gem", 1);
        item2 = new Item("Key", 2);
        extraItem = new Item("Rock", 3);

        // Required items list
        requiredItems = new ArrayList<>();
        requiredItems.add(item1);
        requiredItems.add(item2);

        // Setup inventory and add all items
        inventory = new Inventory();
        inventory.addItem(item1);
        inventory.addItem(item2);
        inventory.addItem(extraItem);

        // Setup puzzle
        dndPuzzle = new DandDPuzzle("answer", "Place the items correctly", "Treasure Puzzle", requiredItems);
    }

    @Test
    public void testInsertValidItem() {
        boolean result = dndPuzzle.insertDNDItem(item1, inventory);
        assertTrue("Should successfully insert a valid required item", result);
        assertTrue("Inserted item should now be in placedItems", dndPuzzle.getPlacedItems().contains(item1));
        assertFalse("Item should be removed from inventory", inventory.getItems().contains(item1));
    }

    @Test
    public void testInsertItemNotRequired() {
        boolean result = dndPuzzle.insertDNDItem(extraItem, inventory);
        assertFalse("Should return false for non-required item", result);
        assertFalse("Extra item should not be added to placedItems", dndPuzzle.getPlacedItems().contains(extraItem));
    }

    @Test
    public void testInsertItemAlreadyPlaced() {
        dndPuzzle.insertDNDItem(item1, inventory);
        boolean result = dndPuzzle.insertDNDItem(item1, inventory);
        assertFalse("Should not allow inserting the same item twice", result);
    }

    @Test
    public void testInsertItemNotInInventory() {
        Inventory emptyInv = new Inventory();
        boolean result = dndPuzzle.insertDNDItem(item1, emptyInv);
        assertFalse("Should return false if the item is not in the inventory", result);
    }

    @Test
    public void testInsertNullValues() {
        assertFalse("Should return false when item is null", dndPuzzle.insertDNDItem(null, inventory));
        assertFalse("Should return false when inventory is null", dndPuzzle.insertDNDItem(item1, null));
    }

    @Test
    public void testAllItemsPlacedFalseInitially() {
        assertFalse("Initially allItemsPlaced should be false", dndPuzzle.allItemsPlaced());
    }

    @Test
    public void testAllItemsPlacedTrueAfterAllInserted() {
        dndPuzzle.insertDNDItem(item1, inventory);
        dndPuzzle.insertDNDItem(item2, inventory);
        assertTrue("Should return true once all required items are placed", dndPuzzle.allItemsPlaced());
    }

    @Test
    public void testGettersReturnCorrectLists() {
        assertEquals("getRequiredItems should match list passed to constructor", requiredItems, dndPuzzle.getRequiredItems());
        assertNotNull("getPlacedItems should not be null", dndPuzzle.getPlacedItems());
    }
}
