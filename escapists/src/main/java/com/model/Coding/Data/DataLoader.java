package com.model.Coding.Data;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.model.Coding.User.User;


import com.model.Coding.Progress.Progress;

public class DataLoader {

  private static DataLoader dataLoader;

  private DataLoader() {

  }

  public static DataLoader getInstance() {
    if (dataLoader == null) {
      dataLoader = new DataLoader();
    }
    return dataLoader;
  }

  public ArrayList<User> getUsers(){
    return new ArrayList<User>();
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
                    Progress progress = new Progress();

                    progress.setDifficulty(((Long) saveJson.get("difficulty")).intValue());
                    progress.setRemainingTime(((Long) saveJson.get("remainingTime")).intValue());

                    JSONArray completedRooms = (JSONArray) saveJson.get("completedRooms");
                    if (completedRooms != null) {
                        for (Object roomName : completedRooms) {
                        }
                    }

                    JSONObject completedPuzzles = (JSONObject) saveJson.get("completedPuzzles");
                    if (completedPuzzles != null) {
                        HashMap<String, HashMap<String, Boolean>> puzzles = new HashMap<>();
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
