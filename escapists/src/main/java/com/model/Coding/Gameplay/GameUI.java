package com.model.Coding.Gameplay;

import com.model.Coding.Data.DataLoader;
import com.model.Coding.Data.DataManager;
import com.model.Coding.Data.DataWriter;
import com.model.Coding.Gameplay.InteractItems.DandDPuzzle;
import com.model.Coding.Gameplay.InteractItems.Inventory;
import com.model.Coding.Gameplay.InteractItems.Item;
import com.model.Coding.Gameplay.InteractItems.ItemPuzzle;
import com.model.Coding.Gameplay.InteractItems.Puzzle;
import com.model.Coding.Gameplay.InteractItems.Puzzle.PuzzleType;
import com.model.Coding.Gameplay.Map.Exit;
import com.model.Coding.Gameplay.Map.Room;
import com.model.Coding.Progress.Leaderboard;
import com.model.Coding.Progress.Progress;
import com.model.Coding.User.User;
import com.model.Coding.User.UserList;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.UUID;


// this is just a console based test UI for the game. this is where we test our program, and we can hard code
// certain scenarios to test.
public class GameUI {
    private static GameFacade GF = GameFacade.getInstance();
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
        Item key = Item.cacheItem("Blue Key", "Open the door.");
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

    public void displayProgress() {
        DataManager manager = DataManager.getInstance();
        DataLoader loader = DataLoader.getInstance();
        DataWriter writer = DataWriter.getInstance();

        System.out.println("=== TEST START ===");

        User testUser = new User("Tester", "password123");
        manager.addUser(testUser);
        System.out.println("User added: " + testUser.getUserName());

        Progress progress = new Progress();
        progress.setDifficulty(2);
        progress.setRemainingTime(300);
        UUID progressId = progress.getProgressId();
        System.out.println("Generated UUID: " + progressId);

        testUser.addSave(progress);
        System.out.println("Save added to user " + testUser.getUserName());

        manager.saveProgress(testUser, progress);
        System.out.println("GameFacade save method simulated..");

        System.out.println("\nLoaded progressId from JSON.");
        Progress loadedProgress = loader.loadProgress(progressId);

        if (loadedProgress != null) {
            System.out.println("Progress loaded.");
            System.out.println("Difficulty: " + loadedProgress.getDifficulty());
            System.out.println("Remaining Time: " + loadedProgress.getRemainingTime());
            System.out.println("Current Room: " + loadedProgress.getCurrentRoom());
            System.out.println("Achievements: " + loadedProgress.getAchievements());
            System.out.println("Puzzles Completed: " + loadedProgress.getCompletedPuzzlesCount());
        } else {
            System.out.println("Failed to load progress");
        }

        ArrayList<User> allUsers = manager.getUsers();
        System.out.println("\nUsers Loaded:");
        for (User u : allUsers) {
            System.out.println("- " + u.getUserName() + " | Saves: " + u.getSaves().size());
        }
    }
    
    public void dragAndDropScenario() {
        Item key1 = Item.cacheItem("Blue Key", "Open the door.");
        Item key2 = Item.cacheItem("Red Key", "Open the door.");
        Item key3 = Item.cacheItem("Green Key", "Open the door.");
        ArrayList<Item> itemReqs = new ArrayList<>();
        itemReqs.add(key1);
        itemReqs.add(key2);
        itemReqs.add(key3);

        Inventory inven = new Inventory();
        inven.addItem(key1);
        inven.addItem(key2);
        // inven.addItem(key3);

        DandDPuzzle dndPuzzle = new DandDPuzzle(null, null, null, itemReqs);
        dndPuzzle.insertDNDItem(key1, inven);
        dndPuzzle.insertDNDItem(key2, inven);
        dndPuzzle.insertDNDItem(key3, inven);
        System.out.println(dndPuzzle.allItemsPlaced() ? "Complete!" : "Not complete");
        
    }

    public void successfulLogin() {

        if(GF.login("John", "passworD123")){
            System.out.println(GF.getCurrUser().toString());
        }
        else {
            System.out.println("Login Failed");
        }
    }

    public void unsuccessfulLogin() {

        if(GF.login("dsa;lijfidsajf", "dsakfa")){
            System.out.println(GF.getCurrUser().toString());
        }
        else {
            System.out.println("Login Failed");
        }
    }

    public void displayLeaderboard() {
        User user = GameFacade.getInstance().getCurrUser();
        int diff = 1;
        System.out.println("User's top times for D"+diff+": " + Leaderboard.getInstance().getFormattedOrderedTimes(user, diff));
    }

    public void roomWithPuzzles() {
        Puzzle riddle = new Puzzle("A cold", "What can you catch, but cannot throw?", "Riddle");
        Puzzle keypad = new Puzzle("1234", "Enter the correct code: ", "Keypad");
        Room cell = new Room("Cell");
        Progress prog = new Progress();
        Item key = Item.cacheItem("Blue Key", "Open the door.");
        Inventory inven = new Inventory();
        Character cellMate = new Character("Cell Mate", key);
        Warden warden = new Warden("Warden", null, null);
        Timer timer  = Timer.getInstance(1800);

        cell.addPuzzle(riddle);
        cell.addPuzzle(keypad);
        prog.setCurrentRoom(cell);

        prog.setPuzzleCompleted(cell, riddle, false);
        prog.setPuzzleCompleted(cell, keypad, false);

        System.out.println("You are in the " + prog.getCurrentRoom().getName() +
                ".\nPuzzles to complete: Riddle & Keypad. \nType their name to enter the puzzle.");
        while (!prog.getCompletedRooms().contains(cell)) {
            String input = scan.nextLine();
            timer.start();
            if (input.equalsIgnoreCase("riddle") && !riddle.getIsCompleted()) {
                while (true) {
                    System.out.println("\n" + riddle.getName());
                    System.out.println(riddle.getDescription());
                    System.out.println("Enter answer (type exit to exit the puzzle): ");
                    String answer = scan.nextLine();

                    if (answer.equalsIgnoreCase("exit")) {
                        break;
                    }

                    if (riddle.checkAnswer(riddle.userAnswer(answer))) {
                        System.out.println("Riddle Completed");
                        inven.addItem(cellMate.interact());
                        System.out.println("\n" + cellMate.speak("Here is the blue key."));
                        System.out.println("Inventory Updated: " + inven.displayInventory());
                        prog.setPuzzleCompleted(cell, riddle, true);
                        break;
                    } else {
                        System.out.println("Incorrect — try again.");
                    }
                }
            }

            else if (input.equalsIgnoreCase("keypad") && !keypad.getIsCompleted()) {
                while (true) {
                    System.out.println("\n" + keypad.getName());
                    System.out.println(keypad.getDescription());
                    System.out.println("Enter answer (type exit to exit the puzzle): ");
                    String answer = scan.nextLine();

                    if (answer.equalsIgnoreCase("exit")) {
                        break;
                    }

                    if (keypad.checkAnswer(keypad.userAnswer(answer))) {
                        System.out.println("Keypad Completed");
                        prog.setPuzzleCompleted(cell, keypad, true);
                        break;
                    } else {
                        System.out.println("Incorrect — try again.");
                    }
                }
            }

            if(prog.allPuzzlesCompleted(cell)) {
                do {
                    System.out.println("\nYou have completed all the puzzles. Give the Blue Key to the warden to proceed. (y/n).");
                    String answer = scan.nextLine();
                    if (answer.equalsIgnoreCase("y")) {
                        warden.interact(inven.getItem(0));
                        inven.removeItem(key);
                        System.out.println("\nInventory Updated: " + inven.displayInventory());
                        prog.markRoomCompleted(cell);
                        System.out.println("Room Completed");
                    } else {
                        System.out.println(warden.speak("I need the key."));
                    }
                } while (!warden.getHasItem());
            } else {
                System.out.println("\nEnter a puzzle (not previously completed puzzle(s)):");
            }
        }
        timer.pause();
        int minutes = timer.getRemainingTime() / 60;
        int seconds = timer.getRemainingTime() % 60;
        System.out.printf("Congratulations! Your time was: %02d:%02d%n", minutes, seconds);
    }

    private void roomTransitionTest() {
        GF.login("John", "passworD123");
        GF.startGame();

        Progress currSave = GF.getCurrUser().getCurrSave();
        Room startRoom = GF.getCurrRoom(); 
        ArrayList<Puzzle> puzzles = startRoom.getPuzzles();
        System.out.println("BEFORE--------------------\n" + startRoom);
        currSave.setPuzzleCompleted(startRoom, puzzles.get(0), true);
        // currSave.setPuzzleCompleted(startRoom, puzzles.get(1), true); // uncomment this to be able to proceed in next room (this will make it so enough puzzle have been complete)
        System.out.println("AFTER--------------------\n" + startRoom);
        Exit firstExit = startRoom.getExits()[0];
        if (firstExit != null && firstExit.isOpen()) {
            Room nextRoom = firstExit.getNextRoom();
            System.out.println("...Exiting to " + nextRoom.getName());
            GF.setCurrRoom(nextRoom);
            System.out.println("NEXT ROOM UNLOCKED---------------\n" + nextRoom);
        } else {
            System.out.println("Cannot proceed.");
        }
    }

    public static void main(String[] args) {
        GameUI gameUI = new GameUI();
        gameUI.dragAndDropScenario();
        //gameUI.scenario1();
        //gameUI.scenario2();
        //gameUI.scenario3();
        // gameUI.successfulLogin();
        //gameUI.displayLeaderboard();
        // gameUI.unsuccessfulLogin();
        // gameUI.roomWithPuzzles();
        // gameUI.roomTransitionTest();
        gameUI.gameLoopTest();
    }
}
