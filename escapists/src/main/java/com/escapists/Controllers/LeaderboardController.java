package com.escapists.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.escapists.App;
import com.model.Coding.Progress.Leaderboard;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;

public class LeaderboardController implements Initializable {

    @FXML
    private ChoiceBox<String> difficultyChoice;

    @FXML
    private ListView<String> lvScores;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        difficultyChoice.getItems().addAll("Easy", "Medium", "Hard");

        difficultyChoice.setValue("Easy");

        loadLeaderboard(0);

        difficultyChoice.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadLeaderboard(newVal.intValue());
            }
        });
    }

    private void loadLeaderboard(int difficulty) {
        lvScores.getItems().clear();
        lvScores.getItems().addAll(
            Leaderboard.getInstance().getFormattedOrderedTimes(difficulty)
        );
    }

    @FXML
    private void backClicked() {
        System.out.println("Back clicked");
        try {
            App.setRoot("mainMenu");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
