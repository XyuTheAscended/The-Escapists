package com.model.Coding.Gameplay.Map;
import com.model.Coding.Gameplay.InteractItems.Puzzle;
import com.model.Coding.Gameplay.Map.Room;

public class Exit {
  private Room nextRoom; 
  private Puzzle[] prereqPuzzles;
  private boolean open; 

  public Exit(Room nextRoom, Puzzle[] prereqPuzzles) {
    this.nextRoom = nextRoom;
    this.prereqPuzzles = prereqPuzzles;

    // added this part to make sure exit gets opened by default if there are no puzzles in it
    boolean allPuzzlesComplete = true;
    for (Puzzle puzzle : prereqPuzzles) {
      if (!puzzle.getIsCompleted()) {
        allPuzzlesComplete = false;
        break;
      }
    }

    this.open = allPuzzlesComplete;
  }

  public void setOpen(boolean open) {
    this.open = open; 
  }

  public Puzzle[] getPrereqPuzzles() {
    return prereqPuzzles; 
  }

  public boolean isOpen() {
    return open;
  }

  public Room getNextRoom() {
    return nextRoom;
  }

  public String toString() {
    // null room means exit leads to outside of prison (so end state)
    return "To: " + (nextRoom != null ? nextRoom.getName() : "OUTSIDE") + " |" + (open ? "O" : "X") + "|";  
  }
}
