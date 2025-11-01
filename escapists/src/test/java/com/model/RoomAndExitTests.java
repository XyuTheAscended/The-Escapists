package com.model;

import org.junit.Test;

import com.model.Coding.Gameplay.InteractItems.Item;
import com.model.Coding.Gameplay.InteractItems.ItemPuzzle;
import com.model.Coding.Gameplay.InteractItems.Puzzle;
import com.model.Coding.Gameplay.Map.Exit;
import com.model.Coding.Gameplay.Map.Room;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;

/**
 * Made room and exit tests in one script b/c they are so intertwined
 */
public class RoomAndExitTests {
  Room startRm = new Room("Start");
  Room endRm = new Room("End");
  Room secretRm = new Room("Secret");
  Puzzle[] puzs = new Puzzle[4];
  @Before
  public void setup() {
    for (int i = 0; i < 4; ++i) {
      Puzzle puz = new Puzzle("Answer"+i, "Puzzle number "+i, "Puzzle"+i);
      startRm.addPuzzle(puz);
    }

    Puzzle[] reqs4End = { puzs[0], puzs[1] };
    Exit exit2End = new Exit(endRm, reqs4End);

    Puzzle[] reqs4Scrt = { puzs[2], puzs[3] };
    Exit exit2Secret = new Exit(secretRm, reqs4Scrt);

    Exit[] exitsAtStart = { exit2End, exit2Secret };
    startRm.setExits(exitsAtStart);
  }

  @Test 
  public void testFirstPuzzleAdded() {
    assertTrue( startRm.getPuzzle("Puzzle0") != null );
  }

  @Test 
  public void testLastPuzzleAdded() {
    assertTrue( startRm.getPuzzle("Puzzle3") != null );
  }

  @Test 
  public void testMiddlePuzzlesAdded() {
    assertTrue( startRm.getPuzzle("Puzzle1") != null );
    assertTrue( startRm.getPuzzle("Puzzle2") != null );
  }

  @Test 
  public void testAddingDuplicatePuzzles() {
    int ogPuzCount = startRm.getPuzzles().size();
    startRm.addPuzzle(puzs[0]);
    assertTrue(ogPuzCount == startRm.getPuzzles().size());
  }

  @Test 
  public void testAddingNullAsPuzzle() {
    int ogPuzCount = startRm.getPuzzles().size();
    startRm.addPuzzle(null); // ideally this should do nothing
    assertTrue(ogPuzCount == startRm.getPuzzles().size());
  }

  @Test
  public void testItemTracking() { // rooms are meant to track what items their puzzles use if they use any at all
    assertTrue(startRm.getItemsForThisRoom().size() == 0);
    Item katanaItem = Item.cacheItem("Katana", "Cool samurai sword");
    ItemPuzzle temPuz = new ItemPuzzle(null, "I use an item", "Tem Puzzle", katanaItem);
    startRm.addPuzzle(temPuz);
    assertTrue(startRm.getItemsForThisRoom().size() > 0);
  }

  @Test
  public void testSuccessfulExitOpening() {
    puzs[0].setIsCompleted(true);
    puzs[1].setIsCompleted(true);
    startRm.updateExits();

    Exit exitOpened = null;
    for (Exit exit : startRm.getExits()) {
      if (exit.isOpen()) {
        exitOpened = exit;
        break;
      }
    }

    assertTrue(exitOpened != null);
    assertTrue(exitOpened.getNextRoom() == endRm);
  }

  @Test
  public void testSuccessfulOtherExitOpening() {
    puzs[2].setIsCompleted(true);
    puzs[3].setIsCompleted(true);
    startRm.updateExits();

    Exit exitOpened = null;
    for (Exit exit : startRm.getExits()) {
      if (exit.isOpen()) {
        exitOpened = exit;
        break;
      }
    }

    assertTrue(exitOpened != null);
    assertTrue(exitOpened.getNextRoom() == secretRm);
  }

  @Test
  /**
   * checks if only appropriate amount of exits become open when certain
   * puzzles are completed as opposed to having them all open
   */
  public void testOnlySomeExitsOpen() { 

    puzs[0].setIsCompleted(true);
    puzs[1].setIsCompleted(true);
    startRm.updateExits();

    int exitsOpened = 0;
    for (Exit exit : startRm.getExits()) {
      if (exit.isOpen()) {
        ++exitsOpened;
      }
    }

    assertTrue(exitsOpened == 1);
  }

  @Test
  /**
   * tests if all exits do not open when not enough puzzles are completed for any of them
   */
  public void testUnsuccessfulExitOpen() { 

    puzs[0].setIsCompleted(true);
    startRm.updateExits();

    int exitsOpened = 0;
    for (Exit exit : startRm.getExits()) {
      if (exit.isOpen()) {
        ++exitsOpened;
      }
    }

    assertTrue(exitsOpened == 0);
  }


  @Test
  public void testRequirementlessExitUpdating() {
    Room rm = new Room("Room of rooms");
    Puzzle puz = new Puzzle("Bad", "Fill the blank: This room is _", "Bad puzzle");
    Puzzle[] reqs = { puz };
    Exit goodExit = new Exit(startRm, reqs);
    Exit lazyExit = new Exit(endRm, null); // exit w/o puzzle requirements
    Exit[] exits = { goodExit, lazyExit };
    rm.setExits(exits);
    rm.updateExits();

    int exitsOpened = 0;
    for (Exit exit : rm.getExits()) {
      if (exit.isOpen()) {
        ++exitsOpened;
      }
    }

    assertTrue(exitsOpened == 1);
    assertTrue(lazyExit.isOpen());
    
  }

  @Test
  public void testNullExitArrHandling() {
    Room rm = new Room("Room of rooms");
    Exit goodExit = new Exit(startRm, null);
    rm.setExits(null);
    rm.updateExits();
  }


  @Test
  public void testSingleNullExitHandling() {
    Room rm = new Room("Room of rooms");
    Puzzle puz = new Puzzle("Bad", "Fill the blank: This room is _", "Bad puzzle");
    Puzzle[] reqs = { puz };
    Exit goodExit = new Exit(startRm, reqs);
    Exit[] exits = { goodExit, null };
    rm.setExits(exits);
    rm.updateExits();
  }

}
