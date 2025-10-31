package com.model;

import org.junit.Test;

import com.model.Coding.Gameplay.InteractItems.Item;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;


public class ItemTests {
  ArrayList<Item> tems = new ArrayList<>(); 

  @Before
  public void setup() {
    for (int i = 1; i <= 10; i++) {
      Item.cacheItem("Item"+i, "This is item number "+i);
    }

    for (int i = 1; i <= 4; ++i) {
      Item coolItem = Item.cacheItem("CoolItem"+i, "Epic");
      tems.add(coolItem);
    }
  }

  @Test
  public void testFirstItemCached() {
    assertTrue( Item.searchForItem("Item1") != null ); 
  }

  @Test
  public void testLastItemCached() {
    assertTrue( Item.searchForItem("Item10") != null ); 
  }

  @Test
  public void testMiddleItemCached() {
    assertTrue( Item.searchForItem("Item5") != null ); 
  }

  @Test
  public void testCaseInsensitivitySearch() {
    assertTrue( Item.searchForItem("ITEM5", false) != null ); 
  }

  @Test
  public void testCaseSensitivitySearch() {
    assertFalse( Item.searchForItem("ITEM5", true) != null ); 
  }

  @Test
  public void testNonexistentItemCached() {
    assertFalse( Item.searchForItem("Item69") != null ); 
  }

  @Test
  public void testCacheingNullItem() {
    int ogSize = Item.getAllItemsEver().size();
    Item.cacheItem(null, null); // ideally this should do nothing and thus leave item cache size unchanged
    int newSize = Item.getAllItemsEver().size();
    assertTrue(ogSize == newSize);
  }

  @Test
  public void testItemEqualityOverride() {
    Item item1 = Item.searchForItem("Item1");
    Item item2 = Item.searchForItem("Item2");

    assertTrue(item1.equals(item1));
    assertFalse(item1.equals(item2));
  }

  @Test 
  public void testItemEqualityForSameDescItems() {
    Item item1 = Item.cacheItem("Item1", "Poop");
    Item item2 = Item.cacheItem("Item2", "Poop");
    assertFalse(item1.equals(item2));
  }

  @Test
  public void testDuplicateCacheing() {
    int ogSize = Item.getAllItemsEver().size();
    Item ogItem1 = Item.searchForItem("Item1");
    Item.cacheItem("Item1", "A duplicate of item 1"); // ideally this should do nothing and thus leave item cache size unchanged
    Item postRecacheItem1 = Item.searchForItem("Item1"); 
    assertTrue(ogItem1 == postRecacheItem1); // these vars should be the same reference to the same object located in the cache
    assertTrue(ogItem1.equals(postRecacheItem1));
    assertTrue(ogItem1.getDescription().equals(postRecacheItem1.getDescription()));
    int newSize = Item.getAllItemsEver().size();
    assertTrue(ogSize == newSize);
  }

  @Test
  // A new item1 should not be created even if it is made in all caps
  public void testDuplicateCacheingCaseSensitivity() {
    int ogSize = Item.getAllItemsEver().size();
    Item ogItem1 = Item.searchForItem("Item1");
    Item.cacheItem("ITEM1", "A duplicate of item 1"); // ideally this should do nothing and thus leave item cache size unchanged
    int newSize = Item.getAllItemsEver().size();
    Item postRecacheItem1 = Item.searchForItem("Item1"); 
    assertTrue(ogItem1 == postRecacheItem1); // these vars should be the same reference to the same object located in the cache
    assertTrue(ogItem1.equals(postRecacheItem1));
    assertTrue(ogItem1.getDescription().equals(postRecacheItem1.getDescription()));
    assertTrue(ogSize == newSize);
  }

  @Test
  public void testSearchCapabilitiesOnGenericItemListFirstItem() {
    assertTrue(Item.searchForItemInList(tems, "CoolItem1", false) != null);
  }

  @Test
  public void testSearchCapabilitiesOnGenericItemListLastItem() {
    assertTrue(Item.searchForItemInList(tems, "CoolItem4", false) != null);
  }

  @Test
  public void testSearchCapabilitiesOnGenericItemListMiddleItems() {
    assertTrue(Item.searchForItemInList(tems, "CoolItem2", false) != null);
    assertTrue(Item.searchForItemInList(tems, "CoolItem3", false) != null);
  }
  

  @Test
  public void testSearchCapabilitiesOnGenericItemListNonexistentItem() {
    assertFalse(Item.searchForItemInList(tems, "CoolItem420", false) != null);
  }

}
