package com.escapists.Controllers;

import com.escapists.App;
import com.model.Coding.Gameplay.GameFacade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    void btnLoginClicked(ActionEvent event) throws IOException {
        GameFacade gf = GameFacade.getInstance();
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (gf.login(username, password)) {
            System.out.println("Successful Login\n" + gf.getCurrUser().toString());
        } else {
            System.out.println("Unsuccessful Login");
        }
        App.setRoot("mainMenu");
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
