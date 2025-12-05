package com.escapists.Controllers;

import com.escapists.App;
import com.model.Coding.Gameplay.GameFacade;
import com.model.Coding.UiHelp.Coolui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * Controller for the open drawer screen
 * @author Mason, Jeffen
 */
public class OpenDrawerController {

    /**
     * Initialize method to initialize required things for the room
     */
    @FXML
    public void initialize() {
        Coolui.layerPage(mainAp); // this function call adds the Hud
        Coolui.setupItemPickup(btnKeycard, "KeyCard");
    }


    @FXML
    private Button btnBack;

    @FXML
    private Button btnKeycard;

    @FXML
    AnchorPane mainAp;

    /**
     * Takes the user to the previous page, the surveillance room
     */
    @FXML
    void btnBackClicked() throws IOException {
        App.safeSetGameplayRoot("surveillanceRoom");
    }

    // UNUSED METHOD
    @FXML
    void btnKeycardClicked(ActionEvent event) {
        // Item keycard = Item.loadItem("KeyCard");
        // gf.getInventory().addItem(keycard);
        // btnKeycard.setDisable(true);
        // // temp
        // System.out.println(gf.getInventory().displayInventory());
    }
}
