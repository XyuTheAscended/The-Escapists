package com.escapists.Controllers;

import com.escapists.App;
import com.model.Coding.Gameplay.GameFacade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * Controller for the log-in screen
 * @author Mason
 */
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

    /**
     * Attempts to log the user in with given credentials
     */
    @FXML
    void btnLoginClicked() throws IOException {
        GameFacade gf = GameFacade.getInstance();
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        System.out.println(username + " is tryna log");
        if (gf.login(username, password)) {
            System.out.println("logged in gu");
            App.setRoot("mainMenu");
        } else {
            lblError.setText("Incorrect username or password!");
        }
    }

    /**
     * Takes the user to the register screen
     */
    @FXML
    void btnRegisterClicked() throws IOException {
        App.setRoot("register");
    }
}
