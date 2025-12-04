package com.escapists.Controllers;

import com.escapists.App;
import com.model.Coding.Gameplay.GameFacade;
import com.model.Coding.Gameplay.InteractItems.Item;
import com.model.Coding.Progress.UIDataCache;
import com.model.Coding.UiHelp.Coolui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ToolboxController {

    GameFacade gf = GameFacade.getInstance();
    UIDataCache UIDC = UIDataCache.getInstance();

    @FXML
    public void initialize() {
        btnTools.setDisable(UIDC.isUIDisabled(gf.getCurrRoom().getName(), "btnTools"));
        Coolui.layerPage(mainAp); // this function call adds the Hud
    }

    @FXML
    AnchorPane mainAp;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnTools;

    @FXML
    void btnBackClicked(ActionEvent event) throws IOException {
        App.setRoot("storageRoom");
    }

    @FXML
    void btnToolsClicked(ActionEvent event) {
        Item screwdriver = new Item(2,"Screwdriver", "A screwdriver");
        gf.getInventory().addItem(screwdriver);
        btnTools.setDisable(true);
        UIDC.setUIDisabled(gf.getCurrRoom().getName(), "btnTools", true);
        // temp
        System.out.println(gf.getInventory().displayInventory());
    }
}
