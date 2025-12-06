package com.escapists.Controllers;
import com.escapists.App;
import com.model.Coding.Speech.Speak;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * Controller for the cutscene screen
 * @author Mason
 */
public class CutsceneController {

    @FXML
    public Button btnBack;
    @FXML
    public ImageView bgImage;
    @FXML
    public AnchorPane root;
    @FXML
    private TextArea cutsceneTextArea;
    @FXML private Button btnContinue;

    private Thread ttsThread;

    /**
     * Initialize method to initialize required things for the room. Starts TTS
     */
    @FXML
    public void initialize() {
        String storyText = cutsceneTextArea.getText();

        ttsThread = new Thread(() -> Speak.speak(storyText));
        ttsThread.setDaemon(true);
        ttsThread.start();
    }

    /**
     * Stops the TTS
     */
    private void stopTts() {
        if (ttsThread != null && ttsThread.isAlive()) {
            ttsThread.interrupt();
            System.out.println("TTS stopped.");
        }
    }

    /**
     * Stops TTS whenever scene is exited
     */
    @FXML
    public void onSceneExit() {
        stopTts();
    }

    /**
     * Switches screen to the cell to begin the game
     */
    public void btnContinueClicked() throws IOException {
        App.safeSetGameplayRoot("cell");
        onSceneExit();
    }

    /**
     * Takes player back to the main menu
     */
    public void btnBackClicked() throws IOException {
        onSceneExit();
        App.setRoot("mainMenu");
    }
}
