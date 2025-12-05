package com.escapists.Controllers;

import com.model.Coding.Gameplay.GameFacade;
import com.model.Coding.UiHelp.Coolui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class VentRoomController {


    GameFacade gf = GameFacade.getInstance();

    @FXML
    public void initialize() {
        Coolui.layerPage(mainAp); // this function call adds the Hud

    }

    @FXML
    private Button btnOut;

    @FXML
    public AnchorPane mainAp;

    @FXML
    void btnOutClicked(ActionEvent event) {

        gf.getCurrUser().getCurrSave().setCurrentRoomName("OUTSIDE");
        // commented this out so saves aren't created during testing
        //gf.save();
        gf.endGame();

    }
}
