package com.model.Coding.Gameplay.InteractItems;

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
    }

    public String userAnswer(String guess){
        userGuess = guess;
        return "";
    }

    public boolean checkAnswer(String guess){
        return true;
    }

    public boolean checkItem(Item item){
        return true;
    }

    public String getAnswer() {
        return answer;
    }

    // temp testing method
    public static void main(String[] args) {
        Puzzle puzzle = new Puzzle("1234", "Enter the correct number", "Keypad");
        puzzle.checkAnswer("1234");
        puzzle.userAnswer("");
        puzzle.checkItem(null);
    }
}
