package com.model;

import org.junit.Test;

import com.model.Coding.Gameplay.InteractItems.Inventory;
import com.model.Coding.Gameplay.InteractItems.Item;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;

// These tests will only work if we can assume Item class works
public class InventoryTests {
  Inventory inv; 

  @Before
  public void setup() {
    inv = new Inventory(); 
    for (int i = 1; i <= 10; i++) {
      inv.addItem( Item.cacheItem("Item"+i, null) );
    }
  }

  @Test
  public void testInventoryHasFirstElement() {
    assertTrue(inv.hasItem("Item1"));
  }

  @Test
  public void testInventoryHasLastElement() {
    assertTrue(inv.hasItem("Item10"));
  }

  @Test
  public void testInventoryDoesntHaveFakeElement() {
    assertFalse(inv.hasItem("Item69"));
  }

  @Test
  public void testItemChecksOnEmptyInventory() {
    Inventory emptyInv = new Inventory();
    assertFalse(emptyInv.hasItem("Sword"));
  }

  @Test
  // this method checks if hasItem works with an item instance parameter instead of just the item name
  public void testItemCheckingByInstanceFirstItem() {
    Item item = Item.searchForItem("Item1");
    assertTrue(inv.hasItem(item));
  }

  @Test
  // this method checks if hasItem works with an item instance parameter instead of just the item name
  public void testItemCheckingByInstanceLastItem() {
    Item item = Item.searchForItem("Item10");
    assertTrue(inv.hasItem(item));
  }

  @Test
  public void testItemRemovalFirstItem() {
    Item item = Item.searchForItem("Item1");
    inv.removeItem(item); // probably should overload this method to be able to take itemName as a param
    assertFalse(inv.hasItem(item));
  }

  @Test
  public void testItemRemovalLastItem() {
    Item item = Item.searchForItem("Item10");
    inv.removeItem(item); // probably should overload this method to be able to take itemName as a param
    assertFalse(inv.hasItem(item));
  }

  @Test
  public void testItemRemovalMiddleItem() {
    Item item = Item.searchForItem("Item5");
    inv.removeItem(item); // probably should overload this method to be able to take itemName as a param
    assertFalse(inv.hasItem(item));
  }

  @Test
  public void testFailedItemRemoval() {
    Item itemWeDonthave = Item.cacheItem("Taser", null);
    int ogInvSize = inv.getItems().size();
    inv.removeItem(itemWeDonthave);
    assertFalse(inv.hasItem(itemWeDonthave));
    int newInvSize = inv.getItems().size();
    assertTrue(newInvSize == ogInvSize);
  }

  @Test
  public void testCaseInsensitiveLookup() {
    assertTrue(inv.hasItem("item1")); // lowercase
    assertTrue(inv.hasItem("ITEM1")); // lowercase
  }


 @Test
  public void testDuplicateItemsRemovalRemovesSingleInstance() {
    Item dup = Item.cacheItem("DUP", null);
    inv.addItem(dup);
    inv.addItem(dup); // same instance added twice
    // should contain item twice (backing list)
    int before = inv.getItems().size();
    inv.removeItem(dup); // removes first occurrence
    int after = inv.getItems().size();
    assertEquals(before - 1, after);
    // still has one remaining
    assertTrue(inv.hasItem(dup));
  }

  @Test
  // this test gives null pointer error cause i dont check for nulls when adding items to inventory
  public void testAddingNullLeadsToNpeInHasItem() {
      // current Inventory.addItem allows null; hasItem(Item) will NPE when it iterates and sees a null element
      inv.addItem(null);
      // this line will throw NPE with your current hasItem(Item) implementation:
      inv.hasItem(Item.cacheItem("Whatever", null));
  }


}
