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

    /** Load saved slot previews */
    private void loadVisuals() {
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
            lbl.setText("Saved: " + (p.getSaveTimestamp() == null ? "Unknown" : p.getSaveTimestamp()));
            if (p.getBackgroundImage() != null) {
                img.setStyle("-fx-fill: url('" + p.getBackgroundImage() + "'); -fx-background-size: cover;");
            }
        }
    }

    /** Handles clicking any save slot */
    @FXML
    private void handleSlotClick(javafx.scene.input.MouseEvent e) {

        int slot = Integer.parseInt(((VBox)e.getSource()).getId().substring(4)); // slot1 → 1

        Progress progress = gf.getCurrUser().getCurrSave();
        User user = gf.getCurrUser();

        // store details
        progress.setSaveTimestamp(LocalDateTime.now().format(fmt));
        progress.setSaveSlot(slot);

        // background image is stored by room name → perfect!
        progress.setBackgroundImage(progress.getCurrentRoomName() + ".png");

        // backend save
        user.saveToSlot(slot, progress);
        gf.save();

        System.out.println("Saved to slot: " + slot);

        // refresh labels + images
        loadVisuals();
    }

    @FXML
    private void backButtonClicked() {
        try {
            App.safeSetRoot("cell"); // Go back into the game pause context
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
