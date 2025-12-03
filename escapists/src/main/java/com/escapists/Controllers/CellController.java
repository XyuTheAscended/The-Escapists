package com.escapists.Controllers;

import com.escapists.App;
import com.model.Coding.Gameplay.GameFacade;
import com.model.Coding.Gameplay.InteractItems.Puzzle;
import com.model.Coding.Gameplay.Map.Room;
import com.model.Coding.Progress.Progress;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.ArrayList;

public class CellController {


    GameFacade gf = GameFacade.getInstance();

    @FXML
    public void initialize() {
        gf.startGame(1);
        Button closeBtn = (Button) cellMateDialog.lookupButton(ButtonType.CLOSE);
        closeBtn.setOnAction(e -> cellMateDialog.setVisible(false));


    }

    @FXML
    private Button btnCellMate;

    @FXML
    private Button btnNote;

    @FXML
    private Button btnSink;

    @FXML
    private DialogPane cellMateDialog;

    @FXML
    private TextField noteRiddleAnswrBox;

    @FXML
    private TextArea noteRiddleText;

    @FXML
    private Button btnEnter;

    @FXML
    void btnCellMateClicked(ActionEvent event) {
        cellMateDialog.setVisible(true);
    }

    @FXML
    void btnSinkClicked(ActionEvent event) throws IOException {
        App.setRoot("sink");
    }

    @FXML
    void btnNoteClicked(ActionEvent event) {

        Room currRoom = gf.getCurrRoom();
        noteRiddleAnswrBox.setVisible(true);
        noteRiddleText.setVisible(true);
        btnEnter.setVisible(true);
        noteRiddleText.setText(currRoom.getPuzzle("Note").getDescription());
    }

    @FXML
    void btnEnterClicked(ActionEvent event) {
        Room currRoom = gf.getCurrRoom();
        String answer = noteRiddleAnswrBox.getText();
        if(currRoom.getPuzzle("Note").checkAnswer(currRoom.getPuzzle("Note").userAnswer(answer))) {
            noteRiddleAnswrBox.setText("Riddle Completed");
            noteRiddleText.setText("2436");
            gf.getCurrUser().getCurrSave().setPuzzleCompleted(currRoom, currRoom.getPuzzle("Note"), true);
        } else {
            noteRiddleAnswrBox.setText("Wrong! Try again");
        }
    }
}
