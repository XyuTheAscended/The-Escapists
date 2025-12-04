package com.escapists.Controllers;

import com.escapists.App;
import com.model.Coding.Gameplay.GameFacade;
import com.model.Coding.UiHelp.Coolui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class OpenCellDoorController {

    GameFacade gf = GameFacade.getInstance();
    @FXML
    AnchorPane mainAp;

    @FXML
    public void initialize() {
        Coolui.layerPage(mainAp); // this function call adds the Hud

    }

    @FXML
    private Button btnExit;

    @FXML
    void btnExitClicked(ActionEvent event) throws IOException {
        App.setRoot("hallway");
        gf.getCurrUser().getCurrSave().markRoomCompleted(gf.getCurrRoom());
        gf.setCurrRoom(gf.getCurrRoom().getExitByNextRoomName("Hallway").getNextRoom());
        System.out.println(gf.getCurrRoom());
    }
}
