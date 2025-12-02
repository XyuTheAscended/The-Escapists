package com.escapists.Controllers;
import com.escapists.App;
import com.model.Coding.Speech.Speak;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.io.IOException;

public class CutsceneController {

    public Button btnBack;
    @FXML
    private TextArea cutsceneTextArea;
    @FXML private Button btnContinue;

    private Thread ttsThread;

    @FXML
    public void initialize() {
        String storyText = cutsceneTextArea.getText();

        ttsThread = new Thread(() -> Speak.speak(storyText));
        ttsThread.setDaemon(true);
        ttsThread.start();
    }

    private void stopTts() {
        if (ttsThread != null && ttsThread.isAlive()) {
            ttsThread.interrupt();
            System.out.println("TTS stopped.");
        }
    }

    @FXML
    public void onSceneExit() {
        stopTts();
    }

    public void btnContinueClicked(ActionEvent actionEvent) throws IOException {
        App.setRoot("cell");
        onSceneExit();
    }

    public void btnBackClicked(ActionEvent actionEvent) throws IOException {
        onSceneExit();
        App.setRoot("mainMenu");
    }
}
