package com.escapists.Controllers;

import com.escapists.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class LoadSaveController {

    @FXML
    private Button backButton;

    /**
     * Called automatically after FXML is loaded.
     * We hook the Back button here so you don't have to change the FXML.
     */
    @FXML
    private void initialize() {
        if (backButton != null) {
            backButton.setOnAction(e -> {
                try {
                    App.setRoot("mainMenu");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        }
    }

    /**
     * Optional: if you later add onAction="#backClicked" in the FXML,
     * this handler will work too.
     */
    @FXML
    private void backClicked(ActionEvent event) throws IOException {
        App.setRoot("mainMenu");
    }
}
