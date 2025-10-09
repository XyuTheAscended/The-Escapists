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
import com.model.Coding.Progress.Progress;

public class DataLoader {

  private static DataLoader dataLoader;
  private static final String USER_FILE = "escapists/src/main/java/com/model/Coding/json/users.json";

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
          if (saves.size() > 0) {
            UUID currSaveId = UUID.fromString((String) obj.get("currSave")); 
            users.add(new User(userName, pass, saves, currSaveId));
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
