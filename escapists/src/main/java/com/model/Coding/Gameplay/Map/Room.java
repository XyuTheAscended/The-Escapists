package com.model.Coding.Gameplay.Map;
import com.model.Coding.Gameplay.InteractItems.EnvironmentProp;
import com.model.Coding.Gameplay.InteractItems.Puzzle;

import java.util.ArrayList;

public class Room {
    private ArrayList<Puzzle> puzzles;


    public Room(){
        puzzles = new ArrayList<>();
    }

    public boolean canInteract(EnvironmentProp environmentProp){
        if (environmentProp == null){
            return false;
        }
                return true;
    }

    public void addPuzzle(Puzzle puzzle){
        if (puzzle != null && !puzzles.contains(puzzle)) {
            puzzles.add(puzzle);
        }
    }

    // temp testing method
    public static void main(String[] args) {
        Room room = new Room();
        room.canInteract(null);
        room.addPuzzle(null);
    }
}