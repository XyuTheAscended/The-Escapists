package com.escapists.Controllers;

import com.escapists.App;
import com.model.Coding.Gameplay.GameFacade;
import com.model.Coding.Gameplay.Timer;
import com.model.Coding.Gameplay.InteractItems.Item;
import com.model.Coding.Progress.Progress;
import com.model.Coding.User.User;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
/**
 * @author Tyler Norman
 */import java.util.ArrayList;

public class SaveGameController {

    private final GameFacade gf = GameFacade.getInstance(); 

    @FXML
    private VBox saveDisplay;


    private static Button newSaveButton;
    private DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM/dd/yyyy  HH:mm");

    @FXML
    public void initialize() {
        for (Item item : Item.getAllItemsEver()) {
            System.out.println(item.getName());
        }
        loadSaveVisuals();
    }

    private HBox makeSaveEntry(Progress save, int saveInd) {
        int remainingTime = save.getRemainingTime();
        int difficulty = save.getDifficulty();
        String lastRoom = save.getCurrentRoomName();


        // root HBox
        HBox row = new HBox(12);
        row.getStyleClass().add("save-entry");
        row.setAlignment(Pos.CENTER_LEFT);
        row.setMinHeight(40);

        // Left: save title / index
        Label titleLbl = new Label("Save " + saveInd);
        titleLbl.getStyleClass().add("save-title");

        // Middle: room name
        Label roomLbl = new Label(lastRoom != null ? lastRoom : "Unknown");
        roomLbl.getStyleClass().add("save-room");

        // Middle: remaining time (formatted)
        Label timeLbl = new Label("Time: "+Timer.formatTime(remainingTime));
        timeLbl.getStyleClass().add("save-time");

        // Middle: difficulty
        Label diffLbl = new Label("Diff: "+difficulty);
        diffLbl.getStyleClass().add("save-difficulty");

        // spacer to push buttons to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Right: action buttons
        Button loadBtn = new Button("Load");
        loadBtn.getStyleClass().add("save-btn");
        loadBtn.setOnAction(e -> {
            onSaveLoaded(save);
        });


        row.getChildren().addAll(titleLbl, roomLbl, timeLbl, diffLbl, spacer, loadBtn);
        row.setMaxWidth(Double.MAX_VALUE); 

        return row;
    }


    private void onSaveLoaded(Progress save) {
        User currUser = gf.getCurrUser();
        for (Progress sv : currUser.getSaves()) {
            System.out.println(sv.getCurrentRoomName() + " | " + sv.getProgressId().equals(save.getProgressId()));
        }
        System.out.println("WHYYYYYYYYYYYYYYYYYYYYYYy");
        App.clearFxmlCache();
        if (gf.gameRunning()) {
            gf.endGame();
        }
        gf.loadCurrSave();
        gf.setCurrRoom(save.getCurrentRoom());
        gf.startGame(save.getDifficulty());
        String fxmlToGoTo = mapRoomNameToFXML(save.getCurrentRoomName());
        App.safeSetGameplayRoot(fxmlToGoTo);
    }

    private static int lastSaveIndex = 0;
    private void onSaveAdded() {
        gf.save();
        HBox saveEntry = makeSaveEntry(gf.getCurrUser().getCurrSave(), lastSaveIndex++);
        saveDisplay.getChildren().add(saveDisplay.getChildren().size()-1, saveEntry);
    }


    /** Loads all six save slot previews */
    private void loadSaveVisuals() {
        User user = gf.getCurrUser();
        ArrayList<Progress> saves = user.getSaves();
        saveDisplay.setSpacing(10);
        int saveIndex = 1;
        for (Progress save : saves) {
            if (save.getCurrentRoomName() == null) continue;
            int saveInd = saveIndex++;
            HBox saveEntry = makeSaveEntry(save, saveInd);
            saveDisplay.getChildren().add(saveEntry);
        }
        lastSaveIndex = saveIndex;

        if (App.getLastPageName() != null && !App.getLastPageName().equals("mainMenu")) {
            newSaveButton = new Button("Add new save");
            newSaveButton.getStyleClass().add("save-entry");
            newSaveButton.getStyleClass().add("add-save-button");
            newSaveButton.setMaxWidth(Double.MAX_VALUE);
            newSaveButton.setOnAction(e -> onSaveAdded());
            saveDisplay.getChildren().add(newSaveButton);

        }
    }

    public static void toggleNewSaveButton(boolean toggled) {
        newSaveButton.setVisible(toggled);
    }


    private String mapRoomNameToFXML(String roomName) {
        if (roomName == null) return "cell";

        switch (roomName) {

            case "Cell":
                return "cell";

            case "Hallway":
                return "hallway";

            case "StorageRoom":
                return "storageRoom";

            case "SurveillanceRoom":
                return "surveillanceRoom";

            case "Vent":
                return "ventRoom"; // CONFIRMED by you

            default:
                System.out.println("Unknown room: " + roomName + " — defaulting to Cell.");
                return "cell";
        }
    }

    /** Handles saving into a slot when clicked */
    @FXML
    private void handleSlotClick(javafx.scene.input.MouseEvent e) {

        int slot = Integer.parseInt(((VBox)e.getSource()).getId().substring(4)); // slot1 → 1

        User user = gf.getCurrUser();
        Progress progress = user.getCurrSave();

        // Timestamp
        progress.setSaveTimestamp(LocalDateTime.now().format(fmt));

        // Slot number
        progress.setSaveSlot(slot);

        // Save the room the player is currently in
        if (gf.getCurrRoom() != null) {
            progress.setCurrentRoom(gf.getCurrRoom());
        }

        // Thumbnail image for UI (always prison2.png)
        progress.setBackgroundImage("prison2.png");

        // Save to user JSON
        // user.saveToSlot(slot, progress);
        // gf.save();

        System.out.println("Saved to slot: " + slot);

        // Refresh screen
    }

    @FXML
    private void backButtonClicked() {
        try {
            App.setRootToPrev();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
