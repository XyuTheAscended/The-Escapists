package com.escapists.Controllers;

import com.model.Coding.Gameplay.GameFacade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class OpenCellDoorController {

    GameFacade gf = GameFacade.getInstance();

    @FXML
    private Button btnExit;

    @FXML
    void btnExitClicked(ActionEvent event) {
        // App.setRoot("next room here");
        gf.getCurrUser().getCurrSave().markRoomCompleted(gf.getCurrRoom());
        gf.setCurrRoom(gf.getCurrRoom().getExitByNextRoomName("Hallway").getNextRoom());
    }
}
