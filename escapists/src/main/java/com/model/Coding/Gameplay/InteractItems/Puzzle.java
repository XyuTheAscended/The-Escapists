package com.model.Coding.Gameplay.InteractItems;

// just for testing delete later
import java.util.Scanner;

public class Puzzle {
    private boolean isCompleted;
    private String answer;
    private String userGuess;
    private String description;
    private String name;


    public Puzzle(String answer, String description, String name){
        this.answer = answer;
        this.description = description;
        this.name = name;
        this.isCompleted = false;
    }

    public String userAnswer(String guess){
        userGuess = guess;
        return userGuess.trim().toLowerCase().replaceAll("\\s+", "");
    }

    public boolean checkAnswer(String guess){
        if(guess.equals(answer.trim().toLowerCase().replaceAll("\\s+", ""))) {
            isCompleted = true;
            // delete later
            System.out.println("Correct");
            return true;
        }
        // delete later
        System.out.println("Wrong");
        return false;
    }

    // check to see if we need to add getters and setters to SCRUM board
    public String getAnswer() {
        return answer;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public boolean getIsCompleted() {
        return isCompleted;
    }

    // temp testing method
    public static void main(String[] args) {
        Puzzle puzzle = new Puzzle("1234", "Enter the correct number", "Keypad");
        System.out.println(puzzle.getName());
        System.out.println(puzzle.getDescription());
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter correct passcode: ");
        puzzle.checkAnswer(puzzle.userAnswer(scanner.nextLine()));
        System.out.println();

        Puzzle riddle = new Puzzle("A cold", "What can you catch, but cannot throw?", "Riddle");
        System.out.println(riddle.getName());
        System.out.println(riddle.getDescription());
        System.out.println("Enter answer: ");
        riddle.checkAnswer(riddle.userAnswer(scanner.nextLine()));
    }

    public boolean contains(Puzzle puzzle) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'contains'");
    }
}
