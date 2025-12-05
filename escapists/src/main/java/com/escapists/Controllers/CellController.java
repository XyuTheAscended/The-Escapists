package com.escapists.Controllers;

import com.escapists.App;
import com.model.Coding.Gameplay.GameFacade;
import com.model.Coding.Gameplay.Map.Room;
import com.model.Coding.Progress.Progress;
import com.model.Coding.UiHelp.Coolui;

import com.model.Coding.UiHelp.UIDataCache;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * Controller for the cell screen
 * @author Mason, Jeffen
 */
public class CellController {
    GameFacade gf = GameFacade.getInstance();
    UIDataCache UIDC = UIDataCache.getInstance();
    
    @FXML
    AnchorPane root;

    /**
     * Initialize method to initialize required things for the room
     */
    @FXML
    public void initialize() {
        if (gf.getCurrUser() == null) gf.quickTestLogin();
        Coolui.layerPage(root); // this function call adds the Hud
        
        gf.startGame(1);

        Progress save = gf.getCurrUser().getCurrSave();
        save.setBackgroundImage("prison2.png");

        Room currRoom = gf.getCurrRoom();
        if (currRoom == null) {
            System.err.println("CellController.initialize(): currRoom is null - skipping UI restore");
            return;
        }


        Progress currSave = gf.getCurrUser().getCurrSave();

        // Ensure save knows about all puzzles in this room (idempotent)
        currSave.initializeRoomPuzzles(currRoom);

        if (cellMateDialog != null) {
            Button closeBtn = (Button) cellMateDialog.lookupButton(ButtonType.CLOSE);
            if (closeBtn != null) {
                closeBtn.setOnAction(e -> cellMateDialog.setVisible(false));
            }
        }

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
    private Button btnTurn;

    /**
     * Code for when the note is clicked. Makes the riddle visible
     */
    @FXML
    void btnNoteClicked() {
        Room currRoom = gf.getCurrRoom();
        Progress currSave = gf.getCurrUser().getCurrSave();

        // show UI
        noteRiddleAnswrBox.setVisible(true);
        noteRiddleText.setVisible(true);
        btnEnter.setVisible(true);

        // display riddle text from the model
        noteRiddleText.setText(currRoom.getPuzzle("Note").getDescription());

    }

    /**
     * Code for enter button for riddle. Does the answer checking logic
     */
    @FXML
    void btnEnterClicked() {
        Room currRoom = gf.getCurrRoom(); // WARNING: If a user is logged in who has a save thats current room is not the cell, there will be errors cause the wrong puzzles are assumed
        Progress currSave = gf.getCurrUser().getCurrSave();

        String answer = noteRiddleAnswrBox.getText();
        boolean solved = currRoom.getPuzzle("Note").checkAnswer(currRoom.getPuzzle("Note").userAnswer(answer));
        if (solved) {
            noteRiddleAnswrBox.setText("Riddle Completed");
            noteRiddleText.setText("2436. This might be useful");
            currSave.setPuzzleCompleted(currRoom, currRoom.getPuzzle("Note"), true);
            btnEnter.setDisable(true);
        } else {
            noteRiddleAnswrBox.setText("Wrong! Try again");
        }
    }

    /**
     *
     * Changes to the sink screen when clicked
     */
    @FXML
    void btnSinkClicked() throws IOException {
        App.safeSetGameplayRoot("sink");
    }

    /**
     * Pops up dialog for cellmate when clicked
     */
    @FXML
    void btnCellMateClicked() {
        Room currRoom = gf.getCurrRoom();
        if (cellMateDialog != null) cellMateDialog.setVisible(true);
        UIDC.setUIVisible(currRoom.getName(), "cellMateDialog", true);
    }

    /**
     * "Turns" around when button is clicked. Sets the screen to the cell door.
     */
    @FXML
    void btnTurnClicked() throws IOException {
        App.safeSetGameplayRoot("cellDoor");
    }
}
