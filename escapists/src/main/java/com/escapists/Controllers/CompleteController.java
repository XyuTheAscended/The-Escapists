package com.escapists.Controllers;

import com.escapists.App;
import com.model.Coding.Gameplay.GameFacade;
import com.model.Coding.Gameplay.Timer;
import com.model.Coding.Progress.Progress;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CompleteController implements Initializable {

    private final GameFacade gf = GameFacade.getInstance();

    @FXML private Label lblTimeLeft;
    @FXML private Label lblScore;
    @FXML private Label lblHints;
    @FXML private Label lblTimeCompleted; // Make sure you add this to your FXML!

    @FXML private Button btnContinue;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Get the current save (Progress)
        Progress p = gf.getCurrUser().getCurrSave();

        // Get the global timer instance
        Timer t = Timer.getInstance();

        
        // TIME COMPLETED (final time)
        
        lblTimeCompleted.setText(t.getTimePassedFormatted());

        
        // SCORE = timePassed * difficulty
        
        int score = t.getTimePassed() * p.getDifficulty();
        lblScore.setText(String.valueOf(score));


        lblHints.setText(String.valueOf(p.getHintsUsed()));

        // ============================
        // TIME LEFT BEFORE ESCAPE
        // ============================
        int remaining = t.getRemainingTime();
        int minutes = remaining / 60;
        int seconds = remaining % 60;
        lblTimeLeft.setText(String.format("%02d:%02d", minutes, seconds));

        
        // CONTINUE BUTTON
        
        btnContinue.setOnAction(e -> {
            try {
                App.setRoot("mainMenu");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
}

