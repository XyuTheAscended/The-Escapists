package com.escapists.Controllers;

import com.escapists.App;
import com.model.Coding.UiHelp.Coolui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class CellSinkController {

    @FXML
    AnchorPane mainAp;

    @FXML
    public void initialize() {
        Coolui.layerPage(mainAp); // this function call adds the Hud

    }

    @FXML
    private Button btnBack;

    @FXML
    private Button btnDrain;

    @FXML
    void btnBackClicked(ActionEvent event) throws IOException {
        App.setRoot("cell");
    }

    @FXML
    void btnDrainClicked(ActionEvent event) throws IOException {
        App.setRoot("sinkWithKey");
    }
}
