package com.escapists.Controllers;

import com.escapists.App;
import com.model.Coding.Gameplay.GameFacade;
import com.model.Coding.Gameplay.InteractItems.Item;
import com.model.Coding.UiHelp.Coolui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * Controller for the sink screen
 * @author Mason, Jeffen
 */
public class CellSinkController {
    private static boolean keyTaken = false;


    @FXML
    AnchorPane mainAp;

    @FXML
    ImageView bgImgView; 

    @FXML
    private Button btnKey;

    /**
     * Initialize method to initialize required things for the room.
     */
    @FXML
    public void initialize() {
        Coolui.layerPage(mainAp); // this function call adds the Hud
        btnKey.setVisible(false);

        Coolui.setupItemPickup(btnKey, "Key");

        if (keyTaken) {
            String openSinkImgLink = getClass().getResource("/lootedSink.png").toExternalForm();
            bgImgView.setImage(new Image(openSinkImgLink));

        }
    }

    @FXML
    private Button btnBack;

    @FXML
    private Button btnDrain;

    /**
     * Goes to previous screen, the cell
     */
    @FXML
    void btnBackClicked(ActionEvent event) throws IOException {
        App.setRootToPrev();
    }

    /**
     * "Opens" the drain when clicked and displays the key
     */
    @FXML
    void btnDrainClicked(ActionEvent event) throws IOException {
        if (keyTaken) return;
        String openSinkImgLink = getClass().getResource("/lootedSink.png").toExternalForm();
        bgImgView.setImage(new Image(openSinkImgLink));
        btnDrain.setVisible(false);
        btnKey.setVisible(true);
    }
}
