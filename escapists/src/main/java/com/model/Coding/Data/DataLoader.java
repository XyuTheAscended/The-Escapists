package com.model.Coding.Data;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.util.Iterator;

import com.model.Coding.User.User;
import com.model.Coding.Gameplay.InteractItems.DandDPuzzle;
import com.model.Coding.Gameplay.InteractItems.Item;
import com.model.Coding.Gameplay.InteractItems.ItemPuzzle;
import com.model.Coding.Gameplay.InteractItems.Puzzle;
import com.model.Coding.Gameplay.Map.Exit;
import com.model.Coding.Gameplay.Map.Room;
import com.model.Coding.Progress.Progress;
import com.model.DataConstants;

/**
 * Loads user, room, and progress data from JSON files.
 * @author Liam & Jeffen
 */
public class DataLoader {

  private static DataLoader dataLoader;
  private static String getUserFile() {
    return DataConstants.isJUnitTest()
        ? "escapists/src/test/resources/user_test.json"
        : "escapists/src/main/resources/users.json";
  }
  private static final String ROOMS_FILE = "escapists/src/main/resources/rooms.json"; 

    /**
     * Empty constructor for data loader
     */
    private DataLoader() {

    }

    /**
     * Returns instance.
     *
     * @return DataLoader instance
     */
    public static DataLoader getInstance() {
        if (dataLoader == null) {
            dataLoader = new DataLoader();
        }
        return dataLoader;
    }

    /**
     * Retrieves a list of saves from a user entry in the json file
     * @param userJsonObj user json data object
     * @return list of saves/progresses
     */
    private ArrayList<Progress> getProgressSavesFromUser(JSONObject userJsonObj) {
        JSONArray savesArray = (JSONArray) userJsonObj.get("saves");
        ArrayList<Progress> savesList = new ArrayList<Progress>();
        if (savesArray != null) {
            for (Object saveObj : savesArray) {
                JSONObject saveJson = (JSONObject) saveObj;
                String idStr = (String) saveJson.get("progressId"); 

                if (idStr != null) {
                    Progress progress = getProgressFromSaveJson(saveJson, idStr);
                    savesList.add(progress);
                    

                }
            }
        }

        return savesList;
    }

    /**
     * Creates the hashmap user objects use to store their completion times during run time. derived from json files
     * @param userJsonObj User json data we're getting hashmaps from
     * @return the hashmap
     */
    private HashMap<Integer, ArrayList<Integer>> getCompletionTimesFromUser(JSONObject userJsonObj) {
        JSONObject timesTable = (JSONObject) userJsonObj.get("completionTimes");
        HashMap<Integer, ArrayList<Integer>> completionTimes = new HashMap<>();
        if (timesTable != null) {
            for (Object difficulty : timesTable.keySet()) {
                ArrayList<Integer> timesList = new ArrayList<Integer>();
                completionTimes.put(Integer.valueOf((String) difficulty), timesList);
                JSONArray timesArray = (JSONArray) timesTable.get(difficulty);
                if (timesArray != null) {
                    for (Object timeObj : timesArray) {
                        Integer time = ((Number) timeObj).intValue();
                        timesList.add(time);
                    }
                }

            }
        }

        return completionTimes;
    }

    /**
     * Loads all users from JSON file.
     *
     * @return list of users
     */
    public ArrayList<User> getUsers() {
        ArrayList<User> users = new ArrayList<User>();
        try {
            File file = new File(getUserFile());
            JSONParser parser = new JSONParser();
            JSONObject root;
            JSONArray usersArray;

            // Read file if exists
            if (file.exists() && file.length() > 0) {
                FileReader reader = new FileReader(file);
                root = (JSONObject) parser.parse(reader);
                reader.close();

                Object usersObj = root.get("users");
                if (usersObj instanceof JSONArray) {
                    usersArray = (JSONArray) usersObj;
                } else {
                    usersArray = new JSONArray();
                    root.put("users", usersArray);
                }

                Iterator<Object> it = usersArray.iterator();
                while (it.hasNext()) {
                    JSONObject obj = (JSONObject) it.next();
                    String userName = (String) obj.get("userName");
                    if (userName == null) {
                        continue;
                    }
                    String pass = (String) obj.get("password");
                    if (pass == null) {
                        continue;
                    }

                    ArrayList<Progress> saves = getProgressSavesFromUser(obj);
                    HashMap<Integer, ArrayList<Integer>> completionTimes = getCompletionTimesFromUser(obj);

                    if (saves.size() > 0) {
                        UUID currSaveId = UUID.fromString((String) obj.get("currSave"));
                        users.add(new User(userName, pass, saves, currSaveId, completionTimes));
                    } else {
                        users.add(new User(userName, pass));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;

    }

    /**
     * Loads all rooms and constructs map structure.
     *
     * @return list of rooms
     */
    public ArrayList<Room> loadRooms() {
        HashMap<String, Room> rooms = new HashMap<>();

        try {
            File file = new File(ROOMS_FILE);
            JSONParser parser = new JSONParser();
            JSONObject root;
            JSONObject roomsTable;

            // Read file if exists
            if (file.exists() && file.length() > 0) {
                FileReader reader = new FileReader(file);
                root = (JSONObject) parser.parse(reader);
                reader.close();

                roomsTable = (JSONObject) root.get("rooms");

                // first loop over roomsTable initializes room objects
                for (Object roomNameObj : roomsTable.keySet()) {
                    JSONObject roomJSObj = (JSONObject) roomsTable.get(roomNameObj);

                    JSONArray puzzlesArray = (JSONArray) roomJSObj.get("puzzles");
                    Room newRoom = new Room((String) roomNameObj);

                    for (Object puzzleEntry : puzzlesArray) {
                        JSONObject puzzleJSObj = (JSONObject) puzzleEntry;

                        String name = (String) puzzleJSObj.get("name");
                        String desc = (String) puzzleJSObj.get("description");
                    
                        String answer = (String) puzzleJSObj.get("answer");
                        JSONArray itemReqs = (JSONArray) puzzleJSObj.get("requiredItems");
                        int itemReqsSize = itemReqs != null ? itemReqs.size() : 0;

                        if (answer == null && (itemReqs == null || itemReqs.get(0) == null)) {
                            throw new RuntimeException("Puzzle " + name + " problem: Answerless puzzles at least need one required item");
                        }

                        Puzzle puzzle = null;
                        if (itemReqs != null && itemReqsSize > 0) {
                        if (itemReqsSize == 1) { // standard item puzzle case 
                            // in future, json should store more comprehensive item data than just the name
                            String itemName = (String) itemReqs.get(0);
                            Item itemReq = Item.cacheItem(itemName, desc);

                            // note that answer and desc may be null for item puzzles
                            puzzle = new ItemPuzzle(answer, desc, name, itemReq);
                            
                        } else { // dnd puzzle case since there are multiple item reqs
                            ArrayList<Item> dndItemsList = new ArrayList<>();
                            for (int i = 0; i < itemReqs.size(); i++) {
                                String itemName = (String) itemReqs.get(i);
                                Item itemReq = Item.cacheItem(itemName, desc);
                                dndItemsList.add(itemReq);
                            }

                            // note that answer and desc may ALSO be null for DnD puzzles
                            puzzle = new DandDPuzzle(answer, desc, name, dndItemsList);
                            
                        }
                        } else {
                            puzzle = new Puzzle(answer, desc, name);
                        }

                        newRoom.addPuzzle(puzzle);
                    }

                    rooms.put(newRoom.getName(), newRoom);
                }

        // rooms and their puzzles must be loaded before we loop a second time to add their exits
        for (Object roomNameObj : roomsTable.keySet()) {
            JSONObject roomJSObj = (JSONObject) roomsTable.get(roomNameObj); 
          
            Room room = rooms.get((String) roomNameObj); 
            if (room == null) throw new RuntimeException("Impossible situation: Room was not found for " + (String) roomNameObj);
          
            JSONObject exitsTable = (JSONObject) roomJSObj.get("exits");
            if (exitsTable != null) {
                Exit[] exits = new Exit[exitsTable.size()];
                int exitInd = 0;
                for (Object exitRoomNameObj : exitsTable.keySet()) {
                    String exitRoomName = (String) exitRoomNameObj;
                    Room exitRoom = rooms.get(exitRoomName);
                    if (exitRoom == null && !exitRoomName.equals("OUTSIDE")) throw new RuntimeException("Impossible situation: Room was not found for " + (String) roomNameObj);

                    JSONArray exitPrereqs = (JSONArray) exitsTable.get(exitRoomNameObj);
                    Puzzle[] prereqPuzzles = new Puzzle[exitPrereqs.size()];
                    int prereqInd = 0;
                    for (Object prereqNameObj : exitPrereqs) {
                        String prereqName = (String) prereqNameObj;
                        Puzzle puzzle = room.getPuzzle(prereqName);
                        if (puzzle == null) throw new RuntimeException("Prereq Puzzle not found in " + (String) roomNameObj);
                            prereqPuzzles[prereqInd++] = puzzle;
                    }

                    Exit exit = new Exit(exitRoom, prereqPuzzles);
                    exits[exitInd++] = exit;
                }

                room.setExits(exits);
            }
        }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>(rooms.values());

    }

    /**
     * Helper method to retrieve Progress instances when other methods need to do so
     * @param saveJson the progress json data, which is supposed to be stored under user data entries in Json
     * @param progIdStr the progress UUID also derived from the json data
     * @return the progress instance that we process out of the json data stuff
     */
    private Progress getProgressFromSaveJson(JSONObject saveJson, String progIdStr) {
        JSONArray completedRooms = (JSONArray) saveJson.get("completedRooms");
        if (completedRooms != null) {
            for (Object roomName : completedRooms) {
            }
        }

        JSONObject completedPuzzles = (JSONObject) saveJson.get("completedPuzzles");
        HashMap<String, HashMap<String, Boolean>> puzzles = new HashMap<>();
        if (completedPuzzles != null) {
            for (Object roomKey : completedPuzzles.keySet()) {
                JSONObject puzzlesJson = (JSONObject) completedPuzzles.get(roomKey);
                HashMap<String, Boolean> puzzleMap = new HashMap<>();
                for (Object puzzleKey : puzzlesJson.keySet()) {
                    puzzleMap.put((String) puzzleKey, (Boolean) puzzlesJson.get(puzzleKey));
                }
                puzzles.put((String) roomKey, puzzleMap);
            }
        }

        Progress progress = new Progress(UUID.fromString(progIdStr), puzzles);
        Long diffData = (Long) saveJson.get("difficulty");
        if (diffData != null)
            progress.setDifficulty((diffData).intValue());
        Long remTimeData = (Long) saveJson.get("remainingTime");
        if (remTimeData != null)
            progress.setRemainingTime((remTimeData).intValue());
        Long hintsData = (Long) saveJson.get("hintsUsed");
        if (hintsData != null)
            progress.setHintsUsed((hintsData).intValue());    

        /* Inventory */
        JSONObject inventoryJson = (JSONObject) saveJson.get("inventory");
        if (inventoryJson != null) {
            JSONArray itemsJsonList = (JSONArray) inventoryJson.get("items");
            for (Object itemObj : itemsJsonList) {
                JSONObject itemJsobj = (JSONObject) itemObj; 
                String name = (String) itemJsobj.get("name");
                // String itemId = (String) itemJsobj.get("name");
                String description = (String) itemJsobj.get("description");
                Item item = Item.cacheItem(name, description);
                progress.getInventory().addItem(item);
            }
        }

        /* Achievements */
        JSONArray achievementsJson = (JSONArray) saveJson.get("achievements");
        if (achievementsJson != null) {
        }

        String currRoomName = (String) saveJson.get("currentRoom");
        if (currRoomName != null) {
            progress.setCurrentRoomName(currRoomName);
        }

        

        return progress;
    }

    /**
     * Loads progress by ID.
     *
     * @param progressId progress identifier
     * @return progress object or null if not found
     */
    @SuppressWarnings("unchecked")
    public Progress loadProgress(UUID progressId) {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(getUserFile())) {
            JSONObject root = (JSONObject) parser.parse(reader);
            JSONArray usersArray = (JSONArray) root.get("users");
            if (usersArray == null) {
                return null;
            }

            for (Object userObj : usersArray) {
                JSONObject userJson = (JSONObject) userObj;
                JSONArray savesArray = (JSONArray) userJson.get("saves");
                if (savesArray == null) {
                    continue;
                }

                for (Object saveObj : savesArray) {
                    JSONObject saveJson = (JSONObject) saveObj;
                    String idStr = (String) saveJson.get("progressId");
                    if (idStr != null && idStr.equals(progressId.toString())) {

                        return getProgressFromSaveJson(saveJson, idStr);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
