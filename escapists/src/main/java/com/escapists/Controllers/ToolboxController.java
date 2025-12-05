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
 * Controller for the tool box screen
 * @author Mason, Jeffen
 */
public class ToolboxController {

    /**
     *Initialize method to initialize required things for the room
     */
    @FXML
    public void initialize() {
        Coolui.layerPage(mainAp); // this function call adds the Hud
        Coolui.setupItemPickup(btnTools, "Screwdriver");
    }

    @FXML
    AnchorPane mainAp;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnTools;

    /**
     * Takes the user back to the storage room
     */
    @FXML
    void btnBackClicked() throws IOException {
        App.safeSetGameplayRoot("storageRoom");
    }

    // UNUSED METHOD
    @FXML
    void btnToolsClicked(ActionEvent event) {
    //     Item screwdriver = Item.loadItem("Screwdriver");
    //     gf.getInventory().addItem(screwdriver);
    //     btnTools.setDisable(true);
    //     // temp
    //     System.out.println(gf.getInventory().displayInventory());
    }
}
