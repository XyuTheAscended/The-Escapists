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
    this.open = false;
  }

  public void setOpen(boolean open) {
    this.open = open; 
  }

  public Puzzle[] getPrereqPuzzles() {
    return prereqPuzzles; 
  }
}
