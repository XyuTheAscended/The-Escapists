package com.model.Coding.Gameplay.Map;
import com.model.Coding.Gameplay.InteractItems.Puzzle;

import java.util.ArrayList;

/**
 * A room
 * @author Mason Adams
 */
public class Room {
    private ArrayList<Puzzle> puzzles;
    private String name;
    private Exit[] exits;

    /**
     * Initializes puzzles ArrayList and name.
     * @param name Name of the room
     */
    public Room(String name){
        puzzles = new ArrayList<>();
        this.name = name;
    }

    /**
     * Adds a puzzle to the room via the puzzles ArrayList
     * @param puzzle Puzzle object
     */
    public void addPuzzle(Puzzle puzzle){
        if (puzzle != null && !puzzles.contains(puzzle)) {
            puzzles.add(puzzle);
        }
    }

    /**
     * Returns the list of puzzles in the room
     * @return ArrayList of puzzles
     */
    public ArrayList<Puzzle> getPuzzles() {
        return puzzles;
    }

    /**
     * Retrieves a puzzle from the puzzles ArrayList
     * @param puzzleName String of the name of the puzzle
     * @return Puzzle object
     */
    public Puzzle getPuzzle(String puzzleName) {
        for (Puzzle puzzle : puzzles) {
            if (puzzle.getName().equals(puzzleName)) return puzzle;
        }
        return null; 
    }

    /**
     * Retrieves the name of the room
     * @return String containing the name of the room
     */
    public String getName() {
        return name;
    }

    // ion know the rest lol
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
        if (exits == null) return; 
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

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ROOM: ").append(name).append("\n");

        sb.append("PUZZLES:\n");
        if (puzzles == null || puzzles.isEmpty()) {
            sb.append("  NONE\n");
        } else {
            for (Puzzle p : puzzles) {
                sb.append("  - ").append(p).append("\n");
            }
        }

        sb.append("EXITS:\n");
        if (exits == null || exits.length == 0) {
            sb.append("  NONE\n");
        } else {
            for (Exit e : exits) {
                sb.append(e)
                .append("\n");

                sb.append("    Prerequisite Puzzles: ");
                if (e.getPrereqPuzzles() == null || e.getPrereqPuzzles().length == 0) {
                    sb.append("NONE");
                } else {
                    for (Puzzle p : e.getPrereqPuzzles()) {
                        sb.append(p.getName()).append(", ");
                    }
                }
            }
        }

        return sb.toString();
    }
}