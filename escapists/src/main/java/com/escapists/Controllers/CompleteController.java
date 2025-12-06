package com.escapists.Controllers;

import com.escapists.App;
import com.model.Coding.Gameplay.GameFacade;
import com.model.Coding.Gameplay.Timer;
import com.model.Coding.Progress.Leaderboard;
import com.model.Coding.Progress.Progress;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CompleteController implements Initializable {

    private final GameFacade gf = GameFacade.getInstance();

    @FXML
    public ImageView bgImage;

    @FXML private Label lblTimeLeft;
    @FXML private Label lblScore;
    @FXML private Label lblHints;
    @FXML private Label lblTimeCompleted;

    // Leaderboard score labels
    @FXML private Label score1;
    @FXML private Label score2;
    @FXML private Label score3;
    @FXML private Label score4;
    @FXML private Label score5;

    @FXML private Button btnContinue;

    @FXML
    private VBox vBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Progress p = gf.getCurrUser().getCurrSave();
        Timer t = Timer.getInstance();

        // TIME COMPLETED
        lblTimeCompleted.setText(t.getTimePassedFormatted());

        // SCORE
        int score = t.getTimePassed() * p.getDifficulty();
        lblScore.setText(String.valueOf(score));

        // HINTS USED
        lblHints.setText(String.valueOf(p.getHintsUsed()));

        // TIME LEFT
        int remaining = t.getRemainingTime();
        int minutes = remaining / 60;
        int seconds = remaining % 60;
        lblTimeLeft.setText(String.format("%02d:%02d", minutes, seconds));

        // LEADERBOARD
        loadLeaderboard(p);

        vBox.setStyle("-fx-background-color: #24201F;");
    }

    /**
     * Load leaderboard times into the 5 boxes.
     */
    private void loadLeaderboard(Progress p) {
        int diff = p.getDifficulty();

        ArrayList<String> times =
                Leaderboard.getInstance().getFormattedOrderedTimes(diff);

        if (times.size() > 0) score1.setText(times.get(0));
        if (times.size() > 1) score2.setText(times.get(1));
        if (times.size() > 2) score3.setText(times.get(2));
        if (times.size() > 3) score4.setText(times.get(3));
        if (times.size() > 4) score5.setText(times.get(4));
    }

    public void btnContinue(ActionEvent actionEvent) throws IOException {
        App.setRoot("mainMenu");
    }
}
