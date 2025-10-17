package com.model.Coding.Gameplay;

import com.model.Coding.Gameplay.InteractItems.Inventory;
import com.model.Coding.Gameplay.InteractItems.Item;
import com.model.Coding.Gameplay.InteractItems.Puzzle;
import com.model.Coding.Progress.Leaderboard;
import com.model.Coding.User.User;
import com.model.Coding.User.UserList;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

// this is just a console based test UI for the game. this is where we test our program, and we can hard code
// certain scenarios to test.
public class GameUI {

    private Puzzle puzzle;
    private Scanner scan = new Scanner(System.in);

    GameUI() {
        puzzle = new Puzzle(null, null, null);
    }

    public void scenario1() {
        if (puzzle.checkAnswer(null)) {
            System.out.println("Answer is right");
        }
        System.out.println("Answer is not right");
    }

    public void scenario2() {
        if(Objects.equals(puzzle.userAnswer("yes"), puzzle.getAnswer())) {
            System.out.println("Answer is correct");
            return;
        }
    }

    // player solves a puzzle, character gives item to inventory, inventory gives item to warden.
    // maybe put all of this in one room
    public void scenario3() {
        Item key = new Item(123, "Blue Key", "Open the door.");
        Inventory inven = new Inventory();
        Character character = new Character("Character", key);
        Warden warden = new Warden("Warden", null, null);
        Puzzle riddle = new Puzzle("A cold", "What can you catch, but cannot throw?", "Riddle");

        do {
            System.out.println("\n" + riddle.getName());
            System.out.println(riddle.getDescription());
            System.out.println("Enter answer: ");
        } while (!riddle.checkAnswer(riddle.userAnswer(scan.nextLine())));

        inven.addItem(character.interact());
        System.out.println(character.speak("Here is the blue key."));
        System.out.println("Inventory Updated: " + inven.displayInventory());

        do {
            System.out.println("\n" + "Do you want to give the blue key to the warden?");
            String answer = scan.nextLine();

            if (answer.equalsIgnoreCase("y")) {
                warden.interact(inven.getItem(0));
            } else {
                System.out.println(warden.speak("I need the key"));
            }
        } while(!warden.getHasItem());

        inven.removeItem(key);
        System.out.println("Inventory Updated: " + inven.displayInventory());
        System.out.println(warden.speak("You may proceed"));
    }

    public void successfulLogin() {
        GameFacade gf = GameFacade.getInstance();

        if(gf.login("John", "passworD123")){
            System.out.println(gf.getCurrUser().toString());
        }
        else {
            System.out.println("Login Failed");
        }
    }

    public void unsuccessfulLogin() {
        GameFacade gf = GameFacade.getInstance();

        if(gf.login("dsa;lijfidsajf", "dsakfa")){
            System.out.println(gf.getCurrUser().toString());
        }
        else {
            System.out.println("Login Failed");
        }
    }

    public void displayLeaderboard() {
        User user = GameFacade.getInstance().getCurrUser();
        System.out.println(Leaderboard.getInstance().getFormattedOrderedTimes(user, 1));
    }

    public static void main(String[] args) {
        GameUI gameUI = new GameUI();
        //gameUI.scenario1();
        //gameUI.scenario2();
        //gameUI.scenario3();
        gameUI.successfulLogin();
        gameUI.displayLeaderboard(); 
        // gameUI.unsuccessfulLogin();
    }
}
