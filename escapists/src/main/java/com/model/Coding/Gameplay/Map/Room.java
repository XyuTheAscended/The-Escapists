package com.model.Coding.Gameplay.Map;
import com.model.Coding.Gameplay.InteractItems.EnvironmentProp;
import com.model.Coding.Gameplay.InteractItems.Puzzle;

import java.util.ArrayList;

public class Room {
    private ArrayList<Puzzle> puzzles;
    private String name;
    private Exit[] exits; 

    public Room(String name){
        puzzles = new ArrayList<>();
        this.name = name;
    }

    public boolean canInteract(EnvironmentProp environmentProp){ // probably should remove this method cause idk what wed ever use it for
        // if (environmentProp == null){
        //     return false;
        // }
        //     return true;

        return environmentProp != null;
    }

    public void addPuzzle(Puzzle puzzle){
        if (puzzle != null && !puzzles.contains(puzzle)) {
            puzzles.add(puzzle);
        }
    }

    public String getName() {
        return name;
    }

    // method assumes that ALL prereq puzzles have already been added to the current room; wont work otherwise
    public void setExits(Exit[] exits) {
        // verify all puzzles needed to open exits are puzzles that actually exist in current room
        for (int i = 0; i < exits.length; i++) {
            Exit exit = exits[i];
            Puzzle[] prereqs = exit.getPrereqPuzzles();
            for (int j = 0; j < prereqs.length; j++) {
                if (!puzzles.contains(prereqs[j])) {
                    System.err.println(prereqs[j].getName() + " is not a puzzle for " + this.name);
                    return; 
                }
            }
        }

        this.exits = exits;
    }


    public void updateExits() {
        for (int i = 0; i < exits.length; i++) {
            Exit exit = exits[i];
            Puzzle[] prereqs = exit.getPrereqPuzzles();
            boolean exitShouldBeOpen = true;
            for (int j = 0; j < prereqs.length; j++) {
                Puzzle prereq = prereqs[j];
                if (!prereq.getIsCompleted()) {
                    exitShouldBeOpen = false; 
                    break;
                }
            }
            exit.setOpen(exitShouldBeOpen);
        } 
    }

    // temp testing method
    public static void main(String[] args) {
        Room room = new Room("Cell");
        room.canInteract(null);
        room.addPuzzle(null);
    }
}