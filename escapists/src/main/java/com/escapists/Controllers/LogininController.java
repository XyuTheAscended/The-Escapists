package com.escapists.Controllers;

import com.escapists.App;
import com.model.Coding.Gameplay.GameFacade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LogininController {

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnRegister;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUsername;

    @FXML
    private Label lblError;

    @FXML
    void btnLoginClicked(ActionEvent event) throws IOException {
        GameFacade gf = GameFacade.getInstance();
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (gf.login(username, password)) {
            App.setRoot("mainMenu");
        } else {
            lblError.setText("Incorrect username or password!");
        }
    }

    @FXML
    void btnRegisterClicked(ActionEvent event) throws IOException {
        App.setRoot("register");
    }

    @FXML
    void back(ActionEvent event) throws IOException {
        App.setRoot("landing");
    }
}
