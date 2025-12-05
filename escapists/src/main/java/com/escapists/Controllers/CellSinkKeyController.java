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

public class CellSinkKeyController {

    GameFacade gf = GameFacade.getInstance();
    @FXML
    AnchorPane mainAp;

    @FXML
    public void initialize() {
        Coolui.layerPage(mainAp); // this function call adds the Hud

    }

    @FXML
    private Button btnBack;

    @FXML
    private Button btnKey;

    @FXML
    void btnBackClicked(ActionEvent event) throws IOException {
        App.setRoot("sink");
    }

    @FXML
    void btnKeyClicked(ActionEvent event) {
        // i think this is supposed to be in json
        Item key = Item.loadItem("Key");
        gf.getInventory().addItem(key);
        btnKey.setDisable(true);
        // temp
        System.out.println(gf.getInventory().displayInventory());
    }
}
