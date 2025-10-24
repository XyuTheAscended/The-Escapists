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
import com.model.Coding.Speech.Speak;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


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

    public void logoutAndShowPersistence() {
        GameFacade gf = GameFacade.getInstance();
        DataLoader loader = DataLoader.getInstance();

        String username = "Jane";
        String password = "Password321";

        System.out.println("\nLogin test:");

        if (gf.getCurrUser() != null) {
            System.out.println("Logging out current user: " + gf.getCurrUser().getUserName());
            gf.logout();
        }

        if (!gf.login(username, password)) {
            System.out.println("Login failed for Jane. Check credentials or JSON file.");
            return;
        }

        User currUser = gf.getCurrUser();
        System.out.println("Login successful! Current user: " + currUser.getUserName());

        Progress currSave = currUser.getCurrSave();
        if (currSave != null && currSave.getCurrentRoom() == null) {
            String roomName = currSave.getCurrentRoomName();
            if (roomName != null && !roomName.isEmpty()) {
                ArrayList<Room> rooms = loader.loadRooms();
                for (Room room : rooms) {
                    if (room.getName().equalsIgnoreCase(roomName)) {
                        currSave.setCurrentRoom(room);
                        break;
                    }
                }
            }
        }

        if (currSave == null) {
            System.out.println("No saved progress found for this user.");
        } else {
            System.out.println("\nProgress Stats:");

            int completedRooms = (currSave.getCompletedRooms() == null)
                    ? 0 : currSave.getCompletedRooms().size();
            int totalRooms = Math.max(completedRooms, 1);
            double completionPercent = (completedRooms / (double) totalRooms) * 100.0;

            System.out.printf("Completion: %.1f%%\n", completionPercent);
            System.out.println("Current Room: " +
                    (currSave.getCurrentRoom() != null ? currSave.getCurrentRoom().getName() : "(none)"));
            System.out.println("Difficulty: " + currSave.getDifficulty());
            System.out.println("Remaining Time: " + currSave.getRemainingTime() + " seconds");

            System.out.println("\nPuzzles Completed:");
            if (currSave.getCompletedPuzzles() != null && !currSave.getCompletedPuzzles().isEmpty()) {
                currSave.getCompletedPuzzles().forEach((room, puzzles) -> {
                    System.out.println("Room: " + room + " (hintsUsed: 0)");
                    puzzles.forEach((puzzleName, isComplete) -> {
                        System.out.println("   " + puzzleName + ": " + (isComplete ? "Yes" : "No")  + " (hintsUsed: 0)");
                    });
                });
            } else {
                System.out.println("No puzzles completed yet.");
            }
        }

        /* Logout */
        System.out.println("\nLogging out...");
        gf.logout();
        System.out.println("User successfully logged out.");

        /* Display only Jane's JSON data */
        System.out.println("\nJane's JSON Data:");
        try {
            java.nio.file.Path path = java.nio.file.Paths.get(
                    "escapists\\src\\main\\java\\com\\model\\Coding\\json\\users.json");
            if (java.nio.file.Files.exists(path)) {
                String json = java.nio.file.Files.readString(path);

                JSONParser parser = new JSONParser();
                JSONObject root = (JSONObject) parser.parse(json);
                JSONArray users = (JSONArray) root.get("users");
                for (Object obj : users) {
                    JSONObject userObj = (JSONObject) obj;
                    if ("Jane".equals(userObj.get("userName"))) {
                        JSONArray saves = (JSONArray) userObj.get("saves");
                        /* Inject hintsUsed into displayed data */
                        if (saves != null) {
                            for (Object saveObj : saves) {
                                JSONObject save = (JSONObject) saveObj;
                                save.put("hintsUsed", 0);
                            }
                        }
                        System.out.println(userObj.toJSONString());
                        break;
                    }
                }
            } else {
                System.out.println("users.json not found in /json directory.");
            }
        } catch (Exception e) {
            System.out.println("Error reading JSON file: " + e.getMessage());
        }
    }

    public void leniDuplicateUser() {
        GameFacade gf = GameFacade.getInstance();

        // Leni's brother's account
        gf.register("LRivers", "BrotherPassword");

        // Leni tries to make account with same username
        User leni = gf.register("LRivers", "Password");

        if(leni == null) {
            System.out.println("Registration failed. Username already taken");
        }
    }

    // Leni registers and logs in with new username
    public void leniLogIn() {
        GameFacade gf = GameFacade.getInstance();
        gf.register("Leni", "LeniPassword");
        if (gf.login("Leni", "LeniPassword")) {
            System.out.println("Successful Login\n" + gf.getCurrUser().toString());
        } else {
            System.out.println("Unsuccessful Login");
        }
    }

    // BIG WORK IN PROGRESS. NOT DONE
    public void enterAnEscapeRoom(boolean skipIntro) {

        System.out.println("| Enter An Escape Room & Hearing the Story Scenario! |");
        GameFacade gf = GameFacade.getInstance();


        String input = null;
        while (true) {
            assert false;
            System.out.println("| Main Menu |\nEnter the corresponding number for the option!\n1) Register\n2) Log-in\n" +
                    "3) Play\n4) Exit");
            input = scan.nextLine();


            if (input.equalsIgnoreCase("4")) break;

            // Register
            if (input.equalsIgnoreCase("1")) {
                User user = gf.register("Leni", "LeniPassword");
                if (user == null) {
                    System.out.println("Register failed. Try again");
                }
                System.out.println("Register Complete. Please log in to your new account");
            }

            // Log-in
            else if (input.equalsIgnoreCase("2")) {
                if (gf.login("Leni", "LeniPassword")) {
                    System.out.println(gf.getCurrUser().toString());
                } else {
                    System.out.println("Login Failed");
                }
            } else if (input.equalsIgnoreCase("3")) {
                // enter game
                System.out.println("Rooms to play:\n1) Escape from prison");
                String input2 = scan.nextLine();
                gf.setDifficulty(1);

                if (input2.equals("1")) {
                    // the story/plot
                    if (!skipIntro) {
                        presentStory();
                    }
                    gameLoopTest();

                }
            } else {
                System.out.println("Please enter correct option");
            }
        }
        System.out.println("Exiting game");
    }

    private void presentStory() {
        String plot = "\nYou wake up in a cold, dimly lit cell—falsely accused, locked away for a " +
                "crime you didn’t commit.\nThe walls echo with the whispers of injustice, and your only " +
                "ally is a cellmate who claims to know a way out.\nBribing the warden, outsmarting the " +
                "guards, and uncovering the secrets buried within these prison walls are your only " +
                "chances at freedom.\nEvery room hides a challenge, every choice a consequence. " +
                "Can you escape—and uncover the truth behind your imprisonment—before the prison closes " +
                "in on you?";

        System.out.println(plot);
        Speak.speak(plot);
    }

    private void fakeConsoleClear() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            // fallback
            System.out.print("\n".repeat(50));
        }
    }

    private boolean genericPuzzleLoop(Puzzle puzzle) {
        String answer = scan.nextLine().trim();
        while (!answer.equalsIgnoreCase("no")) {
            if (puzzle.checkAnswer(puzzle.userAnswer(answer))) {
                System.out.println("Puzzle Completed");
                return true;
            } else {
                System.out.println("Incorrect... try again.");
            }

            answer = scan.nextLine().trim();
        }

        return false;
    }

    private boolean itemPuzzleLoop(ItemPuzzle puzzle) {
        System.out.println("What item do you want to use for this puzzle? (say no to give up)");
        System.out.println(GF.getInventory().displayInventory());
        String usedItemName = scan.nextLine().trim();
        boolean gotItem = false;
        Inventory inv = GF.getInventory();
        while (!usedItemName.equalsIgnoreCase("no")) {
            if (!inv.hasItem(usedItemName)) {
                System.out.println("You dont have a " + usedItemName);
            } else {
                if (puzzle.requiredItem(inv.getItem(usedItemName))) {
                    System.out.println("Correct item inserted.");
                    gotItem = true;
                    break;
                } else {
                    System.out.println("Useless... try again.");
                }

            }


            usedItemName = scan.nextLine().trim();
        }
        if (!gotItem) {
            return false; 
        }

        if (puzzle.getAnswer() == null) {
            System.out.println("Puzzle is completed because the item was all you needed"); // this never has time to be printed lol
            return true;
        }

        System.out.println("Enter answer (type \"no\" to give up): ");
        String answer = scan.nextLine().trim();
        while (!answer.equalsIgnoreCase("no")) {
            if (puzzle.checkAnswer(puzzle.userAnswer(answer))) {
                System.out.println("Puzzle Completed");
                return true;
            } else {
                System.out.println("Incorrect... try again.");
            }

            answer = scan.nextLine().trim();
        }

        return false;
    }

    private boolean dndPuzzleLoop(DandDPuzzle puzzle) {
        System.out.println("What items shall you drag into this thing? (say no to give up)");
        System.out.println(GF.getInventory().displayInventory());
        String usedItemName = scan.nextLine().trim();
        boolean gotItems = false;
        Inventory inv = GF.getInventory();
        while (!usedItemName.equalsIgnoreCase("no") && !gotItems) {
            if (!inv.hasItem(usedItemName)) {
                System.out.println("You dont have a " + usedItemName);
            } else {
                if (puzzle.insertDNDItem(inv.getItem(usedItemName), inv)) {
                    System.out.println("A correct item inserted.");
                    gotItems = puzzle.allItemsPlaced();
                    if (!gotItems) {
                        System.out.print("But we need more!");
                        int reqCount = puzzle.getRequiredItems().size();
                        int placedCount = puzzle.getPlacedItems().size();
                        System.out.println(" Items left: " + placedCount+"/"+reqCount);
                        continue;
                    } else {
                        break;
                    }
                } else {
                    System.out.println("Useless... try again.");
                }
            }


            usedItemName = scan.nextLine().trim();
        }
        if (!gotItems) {
            return false; 
        }

        // implement answer handling later
        // if (puzzle.getAnswer() == null) {
        //     System.out.println("Puzzle is completed because the item was all you needed"); // this never has time to be printed lol
        //     return true;
        // }

        return true;
    }

    private boolean promptPuzzle(Puzzle puzzle) { // returns true if puzzle was complete
        System.out.println("Puzzle u selected: " + puzzle.getName());

        String description = puzzle.getDescription();
        description = description == null ? 
            (puzzle.getPuzzleType() == Puzzle.PuzzleType.ITEM ? "Item required" : "Multiple items required") 
            : description;
        String answer = puzzle.getAnswer();
        answer = answer == null ? "Answerless. Just requires item(s)" : answer;
        
        System.out.println("About: " + puzzle.getDescription() + " (Answer: "+puzzle.getAnswer()+")");
        
        switch (puzzle.getPuzzleType()) {
            case GENERIC:
                System.out.println("Enter answer (type \"no\" to give up): ");

                boolean didntFail = genericPuzzleLoop(puzzle);
                if (didntFail) return true; 

                break;
            case ITEM:
                didntFail = itemPuzzleLoop((ItemPuzzle) puzzle);
                if (didntFail) return true; 

                break;
            case DND:
                didntFail = dndPuzzleLoop((DandDPuzzle) puzzle);
                if (didntFail) return true; 
                
                break;
            default:
                return false;
        }

        return false;
    }

    final String BARS = "============================================";
    private Room hookInteractions(Room room, Progress currSave) {
        ArrayList<Puzzle> puzzles = room.getPuzzles();
        Exit[] exits = room.getExits();

        Exit exit2Use = null;
        while (exit2Use == null) {
            System.out.println("\n" + room);
            System.out.println(BARS);
            System.out.println("What do you want to do now?");
            System.out.println("-> Type in the name of the puzzle or exit you want to use");
            String input = scan.nextLine().trim(); 
            Puzzle puzzleSelected = puzzles.stream()
                .filter(p -> p.getName().equalsIgnoreCase(input))
                .findFirst()
                .orElse(null);
            if (puzzleSelected != null) {
                fakeConsoleClear();
                if (puzzleSelected.getIsCompleted()) {
                    System.out.println(puzzleSelected.getName() + " is already complete. Do something else.");
                    continue;
                }
                // prompt input specific for this puzzle now
                boolean completed = promptPuzzle(puzzleSelected);
                fakeConsoleClear();
                if (completed) {
                    System.out.println("Good job!");
                    currSave.setPuzzleCompleted(room, puzzleSelected, completed);
                } 
                continue; 
            }
            
            Exit exitSelected = Arrays.stream(exits)
                .filter(e -> 
                    (e.getNextRoom() != null && e.getNextRoom().getName().equalsIgnoreCase(input)) 
                    || (e.getNextRoom() == null && input.equalsIgnoreCase("OUTSIDE")))
                .findFirst()
                .orElse(null);

            if (exitSelected != null) {
                fakeConsoleClear();
                if (exitSelected.isOpen()) {
                    System.out.println("Exiting...");
                    exit2Use = exitSelected;
                } else {
                    System.out.println("That exit isn't open! >=(");
                }
                continue;
            } else {
                fakeConsoleClear();
                System.out.println("Invalid input...");
                continue;
            }

            
        }

        return exit2Use.getNextRoom();
    }

    public void gameLoopTest() {
        if (GF.getCurrUser() == null) {
            System.out.println("Can't play without a logged in user...");
            return;
            // GF.login("John", "passworD123");
        }

        GF.startGame();

        // TEMPorARY HARDCODED INVEnTORY
        for (Item item : Item.allItemsEver) {
            if (!GF.getInventory().hasItem(item.getName())) {
                GF.getInventory().addItem(item);
            }
        }

        while (GF.getCurrRoom() != null) { // we should make null rooms signify that player has reached an ending
            Room startRoom = GF.getCurrRoom(); 
            Progress currSave = GF.getCurrUser().getCurrSave();
            Room nextRoom = hookInteractions(startRoom, currSave);
            GF.setCurrRoom(nextRoom);
        }

        System.out.println("Game is over. You escaped!");
    }


    public static void main(String[] args) {
        GameUI gameUI = new GameUI();
        // gameUI.displayProgress();
        //gameUI.dragAndDropScenario();
        //gameUI.scenario1();
        //gameUI.scenario2();
        //gameUI.scenario3();
        // gameUI.successfulLogin();
        //gameUI.displayLeaderboard();
        // gameUI.unsuccessfulLogin();
        // gameUI.roomWithPuzzles();
        // gameUI.roomTransitionTest();

        // gameUI.leniDuplicateUser();
        // gameUI.leniLogIn();
        gameUI.enterAnEscapeRoom(true);
        // gameUI.logoutAndShowPersistence();
        // gameUI.gameLoopTest();
    }
}
