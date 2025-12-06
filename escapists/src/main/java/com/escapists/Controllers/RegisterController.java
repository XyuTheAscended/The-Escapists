package com.escapists.Controllers;

import com.escapists.App;
import com.model.Coding.User.User;
import com.model.Coding.Gameplay.GameFacade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * Controller for the register screen
 * @author Mason
 */
public class RegisterController {

    @FXML
    private Button btnRegister;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUsername;

    @FXML
    private Label lblError;

    /**
     * Registers the user and takes them to the main menu
     */
    @FXML
    void btnRegisterClicked() throws IOException {
        GameFacade gf = GameFacade.getInstance();
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        User user = gf.register(username, password);
        if(user == null){
            lblError.setText("Registration Failed, please try again!");
        } else {
            App.setRoot("mainMenu");
        }
    }

    /**
     * Takes the user back to log-in screen
     */
    @FXML
    void back() throws IOException {
        App.setRoot("login");
    }
}
