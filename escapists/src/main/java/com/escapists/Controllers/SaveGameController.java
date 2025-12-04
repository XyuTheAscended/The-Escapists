package com.escapists.Controllers;

import com.escapists.App;
import com.model.Coding.Gameplay.GameFacade;
import com.model.Coding.Progress.Progress;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SaveGameController {

    private GameFacade gf = GameFacade.getInstance();

    @FXML
    private ImageView slot1Image;

    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        Progress save = gf.getCurrUser().getCurrSave();

        if (save != null && save.getBackgroundImage() != null) {
            slot1Image.setImage(new Image(save.getBackgroundImage()));
        }
    }

    @FXML
    public void saveSlot1() {
        Progress save = gf.getCurrUser().getCurrSave();
        if (save == null) return;

        // Apply background image to save slot
        String bg = save.getBackgroundImage();
        slot1Image.setImage(new Image(bg));

        gf.save(); // persist JSON save

        System.out.println("Saved Slot 1 with background: " + bg);
    }

    @FXML
    public void back() throws Exception {
        App.setRoot("mainMenu");
    }
}
