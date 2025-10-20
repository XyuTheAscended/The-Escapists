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

    public ArrayList<Puzzle> getPuzzles() {
        return puzzles;
    }

    public Puzzle getPuzzle(String puzzleName) {
        for (Puzzle puzzle : puzzles) {
            if (puzzle.getName().equals(puzzleName)) return puzzle;
        }
        return null; 
    } 

    public String getName() {
        return name;
    }

    public Exit[] getExits() {
        return exits; 
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

    public boolean canBeEntered(Room from) {
        Exit[] fromsExits = from.getExits();
        for (Exit fromExit : fromsExits) {
            boolean exitMatchesThisRoom = fromExit.getNextRoom().getName().equals(this.name);
            if (exitMatchesThisRoom) {
               return fromExit.isOpen();
            }
        }
        return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ROOM: ").append(name).append("\n");

        sb.append("PUZZLES:\n");
        if (puzzles == null || puzzles.isEmpty()) {
            sb.append("  NONE\n");
        } else {
            for (Puzzle p : puzzles) {
                sb.append("  - ").append(p.getName()+": "+p.getIsCompleted()).append("\n");
            }
        }

        sb.append("EXITS:\n");
        if (exits == null || exits.length == 0) {
            sb.append("  NONE\n");
        } else {
            for (Exit e : exits) {
                sb.append("    To: ")
                .append(e.getNextRoom() != null ? e.getNextRoom().getName() : "NOWHERE")
                .append("\n");

                sb.append("    Prerequisite Puzzles: ");
                if (e.getPrereqPuzzles() == null || e.getPrereqPuzzles().length == 0) {
                    sb.append("NONE");
                } else {
                    for (Puzzle p : e.getPrereqPuzzles()) {
                        sb.append(p.getName()).append(", ");
                    }
                }
                sb.append("\n    Open: ").append(e.isOpen()).append("\n");
            }
        }

        return sb.toString();
    }

    // temp testing method
    public static void main(String[] args) {
        Room room = new Room("Cell");
        room.canInteract(null);
        room.addPuzzle(null);
    }
}