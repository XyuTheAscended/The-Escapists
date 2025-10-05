package com.model.Coding.Gameplay;

import com.model.Coding.Gameplay.InteractItems.Puzzle;

import java.util.Objects;

// this is just a console based test UI for the game. this is where we test our program, and we can hard code
// certain scenarios to test.
public class GameUI {

    private Puzzle puzzle;

    GameUI() {
        puzzle = new Puzzle(null, null, null);
    }

    public void scenario1() {
        if(puzzle.checkAnswer(null)) {
            System.out.println("Answer is right");
        }
        System.out.println("Answer is not right");

        if(puzzle.checkItem(null)) {
            System.out.println("Correct item");
        }
        System.out.println("Incorrect item");
    }

    public void scenario2() {
        if(Objects.equals(puzzle.userAnswer("yes"), puzzle.getAnswer())) {
            System.out.println("Answer is correct");
            return;
        }
    }

    public static void main(String[] args) {
        GameUI gameUI = new GameUI();
        gameUI.scenario1();
        gameUI.scenario2();
    }
}
