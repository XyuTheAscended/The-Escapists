package com.escapists.Controllers;

import com.escapists.App;
import com.model.Coding.Gameplay.GameFacade;
import com.model.Coding.Progress.Progress;
import com.model.Coding.User.User;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SaveGameController {

    private final GameFacade gf = GameFacade.getInstance();

    @FXML private VBox slot1, slot2, slot3, slot4, slot5, slot6;
    @FXML private Rectangle imgSlot1, imgSlot2, imgSlot3, imgSlot4, imgSlot5, imgSlot6;
    @FXML private Label lblSlot1, lblSlot2, lblSlot3, lblSlot4, lblSlot5, lblSlot6;

    private DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM/dd/yyyy  HH:mm");

    @FXML
    public void initialize() {
        loadVisuals();
    }

    /** Loads all six save slot previews */
    private void loadVisuals() {
        User user = gf.getCurrUser();

        updateSlot(user, 1, imgSlot1, lblSlot1);
        updateSlot(user, 2, imgSlot2, lblSlot2);
        updateSlot(user, 3, imgSlot3, lblSlot3);
        updateSlot(user, 4, imgSlot4, lblSlot4);
        updateSlot(user, 5, imgSlot5, lblSlot5);
        updateSlot(user, 6, imgSlot6, lblSlot6);
    }

    /** Update each slot’s thumbnail + timestamp */
    private void updateSlot(User user, int slot, Rectangle img, Label lbl) {
        if (user.slotExists(slot)) {
            Progress p = user.loadFromSlot(slot);

            lbl.setText("Saved: " + (p.getSaveTimestamp() == null ? "Unknown" : p.getSaveTimestamp()));

            // Always use prison2.png as thumbnail
            img.setStyle("-fx-fill: url('/escapists/images/prison2.png'); -fx-background-size: cover;");
        } else {
            lbl.setText("Empty Slot");
            img.setStyle("-fx-fill: black;");
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
        user.saveToSlot(slot, progress);
        gf.save();

        System.out.println("Saved to slot: " + slot);

        // Refresh screen
        loadVisuals();
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
