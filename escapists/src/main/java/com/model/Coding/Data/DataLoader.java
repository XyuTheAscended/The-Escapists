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
import com.model.Coding.Gameplay.InteractItems.ItemPuzzle;
import com.model.Coding.Gameplay.InteractItems.Puzzle;
import com.model.Coding.Gameplay.Map.Exit;
import com.model.Coding.Gameplay.Map.Room;
import com.model.Coding.Progress.Progress;

public class DataLoader {

  private static DataLoader dataLoader;
  private static final String USER_FILE = "escapists/src/main/java/com/model/Coding/json/users.json";
  private static final String ROOMS_FILE = "escapists/src/main/java/com/model/Coding/json/rooms.json";

  private DataLoader() {

  }

  public static DataLoader getInstance() {
    if (dataLoader == null) {
      dataLoader = new DataLoader();
    }
    return dataLoader;
  }

  // pretty much exact code from load progress method. may wanna be reused there
  private ArrayList<Progress> getProgressSavesFromUser(JSONObject userJsonObj) {
    JSONArray savesArray = (JSONArray) userJsonObj.get("saves");
    ArrayList<Progress> savesList = new ArrayList<Progress>();
    if (savesArray != null) {
      for (Object saveObj : savesArray) {
        JSONObject saveJson = (JSONObject) saveObj;
        String idStr = (String) saveJson.get("progressId"); // THIS doesnt get saved in progress object?? why??

        if (idStr != null) {

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
          

          // Inventory - need more work before full implementation
          JSONObject inventoryJson = (JSONObject) saveJson.get("inventory");
          if (inventoryJson != null) {
          }

          // Achievements - need more work before full implementation
          JSONArray achievementsJson = (JSONArray) saveJson.get("achievements");
          if (achievementsJson != null) {
          }

          Progress progress = new Progress(UUID.fromString(idStr), puzzles);
          progress.setDifficulty(((Long) saveJson.get("difficulty")).intValue());
          progress.setRemainingTime(((Long) saveJson.get("remainingTime")).intValue());
          savesList.add(progress); 

        }
      }
    } 


    return savesList;
  }


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



  public ArrayList<User> getUsers(){
    ArrayList<User> users = new ArrayList<User>(); 
    try {
      File file = new File(USER_FILE);
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
          if (userName == null) continue; 
          String pass = (String) obj.get("password");
          if (pass == null) continue; 

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

  public ArrayList<Room> loadRooms(){
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
            // String reqItemName = (String) puzzleJSObj.get("requiredItem");

            Puzzle puzzle;
            // if (reqItemObj != null) {
              // more item implementation required to finish this
              // Item reqItem = ???
              // puzzle = new ItemPuzzle(reqItem);
            // } else {
            //  ...
            // }

            puzzle = new Puzzle(answer, desc, name);
            newRoom.addPuzzle(puzzle);
          }

          rooms.put(newRoom.getName(), newRoom);
        }

        // rooms and their puzzles must be loaded before we loop a second time to add their exits
        for (Object roomNameObj : roomsTable.keySet()) {
          JSONObject roomJSObj = (JSONObject) roomsTable.get(roomNameObj); 
          
          Room room = rooms.get((String) roomNameObj); 
          if (room == null) System.err.println("Impossible situation: Room was not found for " + (String) roomNameObj);
          
          JSONObject exitsTable = (JSONObject) roomJSObj.get("exits");
          if (exitsTable != null) {
            Exit[] exits = new Exit[exitsTable.size()];
            int exitInd = 0;
            for (Object exitRoomNameObj : exitsTable.keySet()) {
              String exitRoomName = (String) exitRoomNameObj;
              Room exitRoom = rooms.get(exitRoomName);
              if (exitRoom == null) System.err.print("Impossible situation: Room was not found for " + (String) roomNameObj);

              JSONArray exitPrereqs = (JSONArray) exitsTable.get(exitRoomNameObj);
              Puzzle[] prereqPuzzles = new Puzzle[exitPrereqs.size()];
              int prereqInd = 0;
              for (Object prereqNameObj : exitPrereqs) {
                String prereqName = (String) prereqNameObj;
                Puzzle puzzle = room.getPuzzle(prereqName);
                if (puzzle == null) System.err.print("Prereq Puzzle not found in " + (String) roomNameObj);
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

  @SuppressWarnings("unchecked")
  public Progress loadProgress(UUID progressId) {
      JSONParser parser = new JSONParser();
      try (FileReader reader = new FileReader("escapists/src/main/java/com/model/Coding/json/users.json")) {
          JSONObject root = (JSONObject) parser.parse(reader);
          JSONArray usersArray = (JSONArray) root.get("users");
          if (usersArray == null) return null;

          for (Object userObj : usersArray) {
              JSONObject userJson = (JSONObject) userObj;
              JSONArray savesArray = (JSONArray) userJson.get("saves");
              if (savesArray == null) continue;

              for (Object saveObj : savesArray) {
                  JSONObject saveJson = (JSONObject) saveObj;
                  String idStr = (String) saveJson.get("progressId");
                  if (idStr != null && idStr.equals(progressId.toString())) {

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

                      // Inventory - need more work before full implementation
                      JSONObject inventoryJson = (JSONObject) saveJson.get("inventory");
                      if (inventoryJson != null) {
                      }

                      // Achievements - need more work before full implementation
                      JSONArray achievementsJson = (JSONArray) saveJson.get("achievements");
                      if (achievementsJson != null) {
                      }

                      Progress progress = new Progress(UUID.fromString(idStr), puzzles);
                      progress.setDifficulty(((Long) saveJson.get("difficulty")).intValue());
                      progress.setRemainingTime(((Long) saveJson.get("remainingTime")).intValue());
                      return progress;
                  }
              }
          }

      } catch (Exception e) {
          e.printStackTrace();
      }

      return null;
  }

}
