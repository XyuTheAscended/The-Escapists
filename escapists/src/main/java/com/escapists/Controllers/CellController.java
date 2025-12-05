package com.escapists.Controllers;

import com.escapists.App;
import com.model.Coding.Gameplay.GameFacade;
import com.model.Coding.Gameplay.InteractItems.Item;
import com.model.Coding.Gameplay.InteractItems.Puzzle;
import com.model.Coding.Gameplay.Map.Room;
import com.model.Coding.Progress.Progress;
import com.model.Coding.UiHelp.Coolui;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import com.model.Coding.Progress.UIDataCache;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CellController {
    GameFacade gf = GameFacade.getInstance();
    UIDataCache UIDC = UIDataCache.getInstance();
    
    @FXML
    AnchorPane root;

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

    @FXML
    void btnNoteClicked(ActionEvent event) {
        Room currRoom = gf.getCurrRoom();
        Progress currSave = gf.getCurrUser().getCurrSave();

        // show UI
        noteRiddleAnswrBox.setVisible(true);
        noteRiddleText.setVisible(true);
        btnEnter.setVisible(true);

        // display riddle text from the model
        noteRiddleText.setText(currRoom.getPuzzle("Note").getDescription());

    }

    @FXML
    void btnEnterClicked(ActionEvent event) {
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

    @FXML
    void btnSinkClicked(ActionEvent event) throws IOException {
        App.safeSetGameplayRoot("sink");
    }

    @FXML
    void btnCellMateClicked(ActionEvent event) {
        Room currRoom = gf.getCurrRoom();
        if (cellMateDialog != null) cellMateDialog.setVisible(true);
        UIDC.setUIVisible(currRoom.getName(), "cellMateDialog", true);
    }

    @FXML
    void btnTurnClicked(ActionEvent event) throws IOException {
        App.safeSetGameplayRoot("cellDoor");
    }
}
