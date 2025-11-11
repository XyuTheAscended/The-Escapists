package com.escapists.Controllers;
import java.io.IOException;

import com.escapists.App;

import javafx.fxml.FXML;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}