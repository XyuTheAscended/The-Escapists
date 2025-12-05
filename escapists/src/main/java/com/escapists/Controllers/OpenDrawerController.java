package com.escapists.Controllers;

import com.escapists.App;
import com.model.Coding.Gameplay.GameFacade;
import com.model.Coding.Gameplay.InteractItems.Item;
import com.model.Coding.UiHelp.Coolui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class OpenDrawerController {

    GameFacade gf = GameFacade.getInstance();

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


    @FXML
    void btnBackClicked(ActionEvent event) throws IOException {
        App.setRoot("surveillanceRoom");
    }

    @FXML
    void btnKeycardClicked(ActionEvent event) {
        // Item keycard = Item.loadItem("KeyCard");
        // gf.getInventory().addItem(keycard);
        // btnKeycard.setDisable(true);
        // // temp
        // System.out.println(gf.getInventory().displayInventory());
    }
}
