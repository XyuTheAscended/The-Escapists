package com.escapists.Controllers;

import java.io.IOException;

import com.escapists.App;
import com.model.Coding.Progress.Leaderboard;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;

/**
 * Controller for leaderboard
 * @author Liam
 */
public class LeaderboardController {

    @FXML
    private ChoiceBox<String> difficultyChoice;

    @FXML
    private ListView<String> lvScores;

    /**
     * Gets things initialized for leaderboard
     */
    @FXML
    public void initialize() {
        difficultyChoice.getItems().addAll("Easy", "Medium", "Hard");

        difficultyChoice.setValue("Easy");

        loadLeaderboard(0);

        difficultyChoice.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadLeaderboard(newVal.intValue());
            }
        });
    }

    /**
     * Loads the information to needed to display
     * @param difficulty the difficulty the game was completed on
     */
    private void loadLeaderboard(int difficulty) {
        lvScores.getItems().clear();
        lvScores.getItems().addAll(
            Leaderboard.getInstance().getFormattedOrderedTimes(difficulty)
        );
    }

    /**
     * Returns user back to the main menu
     */
    @FXML
    private void backClicked() {
        try {
            App.setRoot("mainMenu");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
