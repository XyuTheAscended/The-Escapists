package com.escapists.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class MainMenuController {

    @FXML
    private AnchorPane avatar;

    @FXML
    private Button btnAccount;

    @FXML
    private Button btnExitGame;

    @FXML
    private Button btnLoadSave;

    @FXML
    private Button btnNewGame;

    @FXML
    private Label lblUsername;

    @FXML
    void accountClicked(ActionEvent event) {

    }

    @FXML
    void avatarClicked(MouseEvent event) {

    }

    @FXML
    void exitGameClicked(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void loadSaveClicked(ActionEvent event) {

    }

    @FXML
    void newGameClicked(ActionEvent event) {

    }

}

