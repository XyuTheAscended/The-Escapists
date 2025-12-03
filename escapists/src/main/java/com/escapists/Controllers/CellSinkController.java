package com.escapists.Controllers;

import com.escapists.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class CellSinkController {
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
