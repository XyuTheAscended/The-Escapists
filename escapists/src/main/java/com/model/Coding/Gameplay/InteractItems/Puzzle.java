package com.model.Coding.Gameplay.InteractItems;

/**
 * A puzzle
 * @author Mason Adams
 */
public class Puzzle {
    private boolean isCompleted;
    private String answer;
    private String userGuess;
    private String description;
    private String name;

    /**
     * sets answer, description, and name
     * @param answer Answer to the puzzles
     * @param description Description of the puzzle
     * @param name Name of the puzzle
     */
    public Puzzle(String answer, String description, String name){
        this.answer = answer;
        this.description = description;
        this.name = name;
        this.isCompleted = false;
    }

    /**
     * Gets the answer from user
     * @param guess The players guess
     * @return Stripped and lowercase string of the players guess
     */
    public String userAnswer(String guess){
        userGuess = guess;
        return userGuess.trim().toLowerCase().replaceAll("\\s+", "");
    }

    /**
     * check if the answer is correct
     * @param guess The guess being checked
     * @return Boolean, true if correct, false otherwise
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
     * @return Answer to the puzzle
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * Gets description
     * @return Description of the puzzle
    */
    public String getDescription() {
        return description;
    }

    /**
     * Gets name
     * @return Name of the puzzle
    */
    public String getName() {
        return name;
    }

    /**
     * sets if puzzle if completed
     * @param completionVal Boolean stating if the puzzle is completed or not
    */
    public void setIsCompleted(boolean completionVal) {
        this.isCompleted = completionVal;
    }

    /**
     * Retrieves if the puzzle is completed
     * @return Boolean, true if the puzzle is completed, false otherwise
    */
    public boolean getIsCompleted() {
        return isCompleted;
    }
}
