package com.model.Coding.Gameplay.InteractItems;

// just for testing delete later
import java.util.Scanner;

public class Puzzle {
    private boolean isCompleted;
    private String answer;
    private String userGuess;
    private String description;
    private String name;
/**
 * sets answer, description, and name
 * @param answer
 * @param description
 * @param name
 */
    public Puzzle(String answer, String description, String name){
        this.answer = answer;
        this.description = description;
        this.name = name;
        this.isCompleted = false;
    }
/**
 * Gets the answer from user
 * @param guess
 * @return
 */
    public String userAnswer(String guess){
        userGuess = guess;
        return userGuess.trim().toLowerCase().replaceAll("\\s+", "");
    }
/**
 * check if the answer is correct
 * @param guess
 * @return
 */
    public boolean checkAnswer(String guess){
        if(guess.equals(answer.trim().toLowerCase().replaceAll("\\s+", ""))) {
            isCompleted = true;
            return true;
        }
        return false;
    }
/**
 * gets the answer
 * @return
 */
    public String getAnswer() {
        return answer;
    }
/**
 * Gets description
 * @return
*/
    public String getDescription() {
        return description;
    }
/**
 * Gets name
 * @return
*/
    public String getName() {
        return name;
    }
/**
 * sets if puzzle if completed
 * @param completionVal
*/
    public void setIsCompleted(boolean completionVal) {
        this.isCompleted = completionVal;
    }
/**
 * Determines if puzzle completion is true
 * @return
*/
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
}
