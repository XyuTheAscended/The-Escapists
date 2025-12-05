package com.escapists.Controllers;

import com.escapists.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;
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

    @FXML
    private Text txtAccount;

    @FXML
    private Button btnExitGame;

    @FXML
    private Button btnLoadSave;

    @FXML
    private Button btnNewGame;

    @FXML
    private Label lblUsername;

    @FXML
    void leaderboardClicked(ActionEvent event) {
        try {
            App.setRoot("leaderboard");  // loads leaderboard.fxml
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    try {
        App.setRoot("loadsave");            
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    @FXML
    void newGameClicked(ActionEvent event) throws IOException {
        gf.getCurrUser().createSave(); // DONT REMOVE THIS PLEASEE
        App.setRoot("cutscene");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (gf.getCurrUser() == null) gf.quickTestLogin(); // FOR TESTING PURPOSES
        User currUser = gf.getCurrUser();
        System.out.println("Logged in " + currUser.getUserName());
        txtAccount.setText(currUser.getUserName());
    }

    

}

