package com.escapists.Controllers;

import com.escapists.App;
import com.model.Coding.Gameplay.GameFacade;
import com.model.Coding.Progress.Progress;
import com.model.Coding.User.User;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class LoadSaveController {

    private final GameFacade gf = GameFacade.getInstance();

    // SLOT CONTAINERS
    @FXML private VBox slot1, slot2, slot3, slot4, slot5, slot6;

    // IMAGES
    @FXML private Rectangle imgSlot1, imgSlot2, imgSlot3, imgSlot4, imgSlot5, imgSlot6;

    // LABELS
    @FXML private Label lblSlot1, lblSlot2, lblSlot3, lblSlot4, lblSlot5, lblSlot6;

    @FXML
    private Button backButton;

    @FXML
    private void initialize() {
        loadSlotVisuals();

        if (backButton != null) {
            backButton.setOnAction(e -> backButtonClicked());
        }
    }

    /** Load preview images + timestamps just like SaveGameController does */
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

            // timestamp text
            String timestamp = p.getSaveTimestamp() == null ? "Empty" : p.getSaveTimestamp();
            lbl.setText("Saved: " + timestamp);

            // image preview
            if (p.getBackgroundImage() != null) {
                img.setStyle("-fx-fill: url('" + p.getBackgroundImage() + "'); -fx-background-size: cover;");
            }
        } else {
            lbl.setText("Empty Slot");
        }
    }

    private static boolean openedFromGame = false;

    public static void openFromGame() {
        openedFromGame = true;
        }

    public static void openFromMainMenu() {
        openedFromGame = false;
        }


    /** Back button → behave based on where the menu was opened from */
   @FXML
    private void backButtonClicked() {
    try {
        if (openedFromGame) {
            App.setRoot("cell");  // return to in-game screen
        } else {
            App.setRoot("mainMenu");  // return to main menu
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    }

}
