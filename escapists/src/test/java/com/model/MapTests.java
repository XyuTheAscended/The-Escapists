package com.model;

import org.junit.Test;

import com.model.Coding.Gameplay.InteractItems.Puzzle;
import com.model.Coding.Gameplay.Map.Map;
import com.model.Coding.Gameplay.Map.Room;
import com.model.Coding.Progress.Progress;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;


public class MapTests {
  Map map;
  Progress save;

  @Before
  public void setup() {
    ArrayList<Room> rms = new ArrayList<>();

    save = new Progress();
    HashMap<String, HashMap<String, Boolean>> completionMap = save.getCompletedPuzzles();
    for (int i = 0; i < 3; ++i) {
      Room rm = new Room("Room"+i);
      rms.add(rm);

      HashMap<String, Boolean> rmPuzComps = new HashMap<>();
      completionMap.put(rm.getName(), rmPuzComps);
      for (int j = 0; j < 3; ++j) {
        Puzzle puz = new Puzzle("Answer", "A cool puzzle", "Puzzle"+i+""+j);
        rm.addPuzzle(puz);
        rmPuzComps.put(puz.getName(), puz.getIsCompleted());
      }
    }

    map = new Map(rms); 
  }

  @Test
  public void testFirstRoomInited() {
    assertTrue( map.getRoomByName("Room0") != null );
  }

  @Test
  public void testLastRoomInited() {
    assertTrue( map.getRoomByName("Room2") != null );
  }

  @Test
  public void testMiddleRoomInited() {
    assertTrue( map.getRoomByName("Room1") != null );
  }

  @Test
  public void testAddingRoom() {
    map.addRoomToMap( new Room("Room5") );
    assertTrue( map.getRoomByName("Room5") != null );

  }

  @Test
  public void testAddingNullRoomFails() {
    int oldAmountOfRms = map.getRooms().size();
    map.addRoomToMap(null);
    int newAmount = map.getRooms().size();
    assertTrue(newAmount == oldAmountOfRms);
  }

  @Test
  public void testSearchingForNonexistentRoomFails() {
    assertTrue(map.getRoomByName("Room100000") == null);
  }

  @Test
  public void testSearchingForNullRoomFails() {
    assertTrue(map.getRoomByName(null) == null);
  }


  private void saveAndMapSanityCheck(HashMap<String, HashMap<String, Boolean>> completionMap) {
    ArrayList<Room> rms = map.getRooms();
    for (Room rm : rms) {
      HashMap<String, Boolean> rmPuzComps = completionMap.get(rm.getName());
      for (Puzzle puz : rm.getPuzzles()) {
        boolean saveCompVal = rmPuzComps.get(puz.getName());
        assertTrue(puz.getIsCompleted() == saveCompVal);
      }
    }
  }

  @Test
  public void testProgressLoadingWithAllComplete() {
    HashMap<String, HashMap<String, Boolean>> completionMap = save.getCompletedPuzzles();
    for (HashMap<String, Boolean> innerMap : completionMap.values()) {
      for (String key : innerMap.keySet()) {
        innerMap.put(key, true);
      }
    }

    map.loadFromSave(save);
    saveAndMapSanityCheck(completionMap);
    
  }

  @Test
  public void testProgressLoadingWithAllIncomplete() {
    HashMap<String, HashMap<String, Boolean>> completionMap = save.getCompletedPuzzles();
    for (HashMap<String, Boolean> innerMap : completionMap.values()) {
      for (String key : innerMap.keySet()) {
        innerMap.put(key, false);
      }
    }

    map.loadFromSave(save);
    saveAndMapSanityCheck(completionMap);
  }

  @Test
  public void testProgressLoadingWithSomeComplete() {
    HashMap<String, HashMap<String, Boolean>> completionMap = save.getCompletedPuzzles();
    for (HashMap<String, Boolean> innerMap : completionMap.values()) {
      for (String key : innerMap.keySet()) {
        boolean randomBool = Math.random() < 0.5 ? true : false;
        innerMap.put(key, randomBool);
      }
    }

    map.loadFromSave(save);
    saveAndMapSanityCheck(completionMap); 
  }
}
