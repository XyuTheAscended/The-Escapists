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
 * Controller for the open cell door screen
 */
public class OpenCellDoorController {

    GameFacade gf = GameFacade.getInstance();
    @FXML
    AnchorPane mainAp;

    /**
     *Initialize method to initialize required things for the room
     */
    @FXML
    public void initialize() {
        Coolui.layerPage(mainAp); // this function call adds the Hud

    }

    @FXML
    private Button btnExit;

    /**
     * Handles room exit logic
     */
    @FXML
    void btnExitClicked() throws IOException {
        App.safeSetGameplayRoot("hallway");
        gf.getCurrUser().getCurrSave().markRoomCompleted(gf.getCurrRoom());
        gf.setCurrRoom(gf.getCurrRoom().getExitByNextRoomName("Hallway").getNextRoom());
        System.out.println(gf.getCurrRoom());
    }
}
