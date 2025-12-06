package com.escapists.Controllers;

import com.escapists.App;
import com.model.Coding.Gameplay.GameFacade;
import com.model.Coding.Progress.Progress;
import com.model.Coding.User.User;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

/**
 * THIS CLASS IS NOT USED
 */
public class LoadSaveController {

    private final GameFacade gf = GameFacade.getInstance();

    // SLOT CONTAINERS
    @FXML private VBox slot1, slot2, slot3, slot4, slot5, slot6;

    // IMAGES
    @FXML private Rectangle imgSlot1, imgSlot2, imgSlot3, imgSlot4, imgSlot5, imgSlot6;

    // LABELS
    @FXML private Label lblSlot1, lblSlot2, lblSlot3, lblSlot4, lblSlot5, lblSlot6;

    @FXML
    private void initialize() {
        loadSlotVisuals();
        addClickHandlers();
    }

    /** Assign clicking to each save slot */
    private void addClickHandlers() {
        slot1.setOnMouseClicked(e -> loadSlot(1));
        slot2.setOnMouseClicked(e -> loadSlot(2));
        slot3.setOnMouseClicked(e -> loadSlot(3));
        slot4.setOnMouseClicked(e -> loadSlot(4));
        slot5.setOnMouseClicked(e -> loadSlot(5));
        slot6.setOnMouseClicked(e -> loadSlot(6));
    }

    /** Load preview images + timestamps */
    private void loadSlotVisuals() {
        User user = gf.getCurrUser();

        updateSlot(user, 1, imgSlot1, lblSlot1);
        updateSlot(user, 2, imgSlot2, lblSlot2);
        updateSlot(user, 3, imgSlot3, lblSlot3);
        updateSlot(user, 4, imgSlot4, lblSlot4);
        updateSlot(user, 5, imgSlot5, lblSlot5);
        updateSlot(user, 6, imgSlot6, lblSlot6);
    }

    private void updateSlot(User user, int slot, Rectangle img, Label lbl) {
        if (user.slotExists(slot)) {
            Progress p = user.loadFromSlot(slot);

            String timestamp = (p.getSaveTimestamp() == null ? "Empty" : p.getSaveTimestamp());
            lbl.setText("Saved: " + timestamp);

            img.setStyle("-fx-fill: url('/escapists/images/prison2.png'); -fx-background-size: cover;");
        } else {
            lbl.setText("Empty Slot");
            img.setStyle("-fx-fill: black;");
        }
    }

    /** Load the save from a specific slot */
    private void loadSlot(int slot) {
        User user = gf.getCurrUser();

        if (!user.slotExists(slot)) {
            System.out.println("No save exists in slot " + slot);
            return;
        }

        // Load the Progress object from that slot
        Progress p = user.loadFromSlot(slot);

        // Set this save as the active save
        user.changeCurrSave(slot - 1);

        // Load map, inventory, puzzles, current room, etc.
        gf.loadCurrSave();

        // Determine correct room FXML
        String roomName = p.getCurrentRoomName();
        String fxmlToLoad = mapRoomNameToFXML(roomName);

        System.out.println("Loading room from save: " + roomName + " → FXML: " + fxmlToLoad);

        try {
            App.setRoot(fxmlToLoad);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load FXML for: " + fxmlToLoad);
        }
    }

    /**
     * Converts backend room names (from JSON & Progress) to the correct FXML file names.
     */
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

    @FXML
    private void backButtonClicked() {
        App.setRootToPrev();
    }
}
