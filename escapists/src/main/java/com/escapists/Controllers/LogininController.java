package com.escapists.Controllers;

import com.model.Coding.Gameplay.GameFacade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

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
    void btnLoginClicked(ActionEvent event) {
        GameFacade gf = GameFacade.getInstance();
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (gf.login(username, password)) {
            System.out.println("Successful Login\n" + gf.getCurrUser().toString());
        } else {
            System.out.println("Unsuccessful Login");
        }
    }

    @FXML
    void btnRegisterClicked(ActionEvent event) {

    }
}
