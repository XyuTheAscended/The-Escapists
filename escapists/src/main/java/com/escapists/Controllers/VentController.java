package com.escapists.Controllers;

import com.escapists.App;
import com.model.Coding.Gameplay.GameFacade;
import com.model.Coding.Gameplay.InteractItems.Inventory;
import com.model.Coding.Gameplay.Map.Room;
import com.model.Coding.UiHelp.Coolui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class VentController {

    GameFacade gf = GameFacade.getInstance();

    @FXML
    public void initialize() {
        Coolui.layerPage(mainAp); // this function call adds the Hud

    }


    @FXML
    public AnchorPane mainAp;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnVent;

    @FXML
    void btnBackClicked(ActionEvent event) throws IOException {
        App.safeSetGameplayRoot("hallway");
    }

    @FXML
    void btnVentClicked(ActionEvent event) throws IOException {

        Inventory inven = gf.getInventory();
        Room currRoom = gf.getCurrRoom();

        gf.getInventory().removeItem(inven.getItem("Screwdriver"));
        gf.getInventory().removeItem(inven.getItem("KeyCard"));
        gf.getCurrUser().getCurrSave().setPuzzleCompleted(currRoom, currRoom.getPuzzle("Vent"), true);
        System.out.println(currRoom);
        App.safeSetGameplayRoot("ventRoom");
        gf.getCurrUser().getCurrSave().markRoomCompleted(gf.getCurrRoom());
        gf.setCurrRoom(gf.getCurrRoom().getExitByNextRoomName("Vent").getNextRoom());
        System.out.println(gf.getCurrRoom());
    }
}
