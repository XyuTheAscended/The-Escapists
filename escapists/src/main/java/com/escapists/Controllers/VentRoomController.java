package com.escapists.Controllers;

import com.model.Coding.Gameplay.GameFacade;
import com.model.Coding.UiHelp.Coolui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

/**
 * Controller for the vent room (inside the vent)
 * @author Mason
 */
public class VentRoomController {

    GameFacade gf = GameFacade.getInstance();

    /**
     *Initialize method to initialize required things for the room
     */
    @FXML
    public void initialize() {
        Coolui.layerPage(mainAp); // this function call adds the Hud

    }

    @FXML
    private Button btnOut;

    @FXML
    public AnchorPane mainAp;

    /**
     * Saves and ends the game. Player has escaped
     */
    @FXML
    void btnOutClicked() {

        gf.getCurrUser().getCurrSave().setCurrentRoomName("OUTSIDE");
        gf.save();
        gf.endGame();

    }
}
