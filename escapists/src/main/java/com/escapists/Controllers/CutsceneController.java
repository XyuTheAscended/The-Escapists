package com.escapists.Controllers;
import com.model.Coding.Speech.Speak;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class CutsceneController {

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

        btnContinue.setOnAction(e -> stopTts());
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

    public void btnContinueClicked(ActionEvent actionEvent) {
    }
}
