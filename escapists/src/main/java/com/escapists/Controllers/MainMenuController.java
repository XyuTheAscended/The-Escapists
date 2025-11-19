package com.escapists.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.util.ResourceBundle;

import com.model.Coding.Gameplay.GameFacade;
import com.model.Coding.User.User;

import javafx.fxml.Initializable;

public class MainMenuController implements Initializable {
    private static GameFacade gf = GameFacade.getInstance();

    @FXML
    private AnchorPane avatar;

    @FXML
    private Button btnAccount;
    private Label lblAccount;

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        User currUser = gf.getCurrUser();
        System.out.println("Logged in " + currUser.getUserName());
        lblAccount.setText(currUser.getUserName());
    }

}

