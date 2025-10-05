package com.model.Coding.Data;

import com.model.Coding.User.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class DataWriter {
    private static final String USER_FILE = "users.json";
    private static DataWriter dataWriter;

    private DataWriter() { }

    public static DataWriter getInstance() {
        if (dataWriter == null) {
            dataWriter = new DataWriter();
        }
        return dataWriter;
    }

    public void addUser(User user) {
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
            } else {
                // Create new object if necessary
                root = new JSONObject();
                usersArray = new JSONArray();
                root.put("users", usersArray);
            }

            // Create new user object
            JSONObject newUser = new JSONObject();
            newUser.put("userName", user.getUserName());
            newUser.put("password", user.getPassword());
            newUser.put("currSave", null);
            newUser.put("saves", new JSONArray());

            // Add new user
            usersArray.add(newUser);

            // Write back to the file
            FileWriter writer = new FileWriter(file);
            writer.write(root.toJSONString());
            writer.flush();
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
