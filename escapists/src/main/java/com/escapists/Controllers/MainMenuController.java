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

/**
 * Controller for the main menu screen
 * @author Mason, Tyler, Jeffen, Liam
 */
public class MainMenuController {
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

    /**
     * Takes the user to the leaderboard
     */
    @FXML
    void leaderboardClicked() {
        try {
            App.setRoot("leaderboard");  // loads leaderboard.fxml
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Exits the game
     */
    @FXML
    void exitGameClicked() {
        System.exit(0);
    }

    /**
     * Takes the user to the load-save menu
     */
    @FXML
    void loadSaveClicked() {
    try {
        App.setRoot("save");            
    } catch (IOException e) {
        e.printStackTrace();
        }
    }

    /**
     * Creates a new save for user and sends them along their way
     */
    @FXML
    void newGameClicked() throws IOException {
        App.setRoot("cutscene");
        
    }

    /**
     * Initialize method for main menu.
     */
    @FXML
    public void initialize() {
        if (gf.getCurrUser() == null) gf.quickTestLogin(); // FOR TESTING PURPOSES
        User currUser = gf.getCurrUser();
        System.out.println("Logged in " + currUser.getUserName());
        txtAccount.setText(currUser.getUserName());
    }

    public void avatarClicked(MouseEvent mouseEvent) {
    }
}

