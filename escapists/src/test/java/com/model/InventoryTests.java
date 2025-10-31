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
    assertTrue(!inv.hasItem("Item69"));
  }

  @Test
  public void testItemChecksOnEmptyInventory() {
    Inventory emptyInv = new Inventory();
    assertTrue(!emptyInv.hasItem("Sword"));
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
    assert(!inv.hasItem(item));
  }

  @Test
  public void testItemRemovalLastItem() {
    Item item = Item.searchForItem("Item10");
    inv.removeItem(item); // probably should overload this method to be able to take itemName as a param
    assert(!inv.hasItem(item));
  }

  @Test
  public void testItemRemovalMiddleItem() {
    Item item = Item.searchForItem("Item5");
    inv.removeItem(item); // probably should overload this method to be able to take itemName as a param
    assert(!inv.hasItem(item));
  }

  @Test
  public void testFailedItemRemoval() {
    Item itemWeDonthave = Item.cacheItem("Taser", null);
    inv.removeItem(itemWeDonthave);
    assert(!inv.hasItem(itemWeDonthave));
  }




}
