package com.escapists.Controllers;

import com.escapists.App;
import com.model.Coding.Gameplay.GameFacade;
import com.model.Coding.Gameplay.InteractItems.Puzzle;
import com.model.Coding.Gameplay.Map.Room;
import com.model.Coding.Progress.Progress;
import com.model.Coding.UiHelp.Coolui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class StorageRoomController {


    GameFacade gf = GameFacade.getInstance();

    @FXML
    public void initialize() {
        Coolui.layerPage(mainAp); // this function call adds the Hud
        gf.setCurrRoom(gf.findRoomByName("StorageRoom"));
        Progress currSave = gf.getCurrUser().getCurrSave();
        Room currRoom = gf.getCurrRoom();

        System.out.println(currRoom);

        if (currRoom == null) {
            System.err.println("CellController.initialize(): currRoom is null - skipping UI restore");
            return;
        }

        // Ensure save knows about all puzzles in this room (idempotent)
        currSave.initializeRoomPuzzles(currRoom);
    }

    @FXML
    public TextArea exitMessage;

    @FXML
    private Button btnEnter;

    @FXML
    private Button btnToolbox;

    @FXML
    private AnchorPane mainAp;

    @FXML
    private TextField riddleAnswr;

    @FXML
    private TextArea riddleText;

    @FXML
    private Button btnExit;

    @FXML
    void btnEnterClicked(ActionEvent event) throws IOException {

        Room currRoom = gf.getCurrRoom();
        Progress currSave = gf.getCurrUser().getCurrSave();
        String answer = riddleAnswr.getText();
        boolean solved = currRoom.getPuzzle("StorageRiddle").checkAnswer(currRoom.getPuzzle("StorageRiddle").userAnswer(answer));

        if (solved) {

            riddleAnswr.setText("Riddle Completed");
            currSave.setPuzzleCompleted(currRoom, currRoom.getPuzzle("StorageRiddle"), true);
            btnEnter.setDisable(true);
            System.out.println(currRoom);
            App.setRoot("toolbox");
        } else {
            riddleAnswr.setText("Wrong! Try again");
        }
    }

    @FXML
    void btnToolBoxClicked(ActionEvent event) {

        Room currRoom = gf.getCurrRoom();
        Puzzle puzzle = gf.getCurrRoom().getPuzzle("StorageRiddle");
        riddleText.setText(puzzle.getDescription());

        if(puzzle.getIsCompleted()){
            riddleText.setText("Riddle Completed");
            btnEnter.setDisable(true);
            riddleAnswr.setDisable(true);
            riddleAnswr.setVisible(true);
            riddleText.setVisible(true);
            riddleAnswr.setVisible(true);
            return;
        }


        riddleText.setVisible(true);
        riddleAnswr.setVisible(true);
        btnEnter.setVisible(true);
    }

    @FXML
    void btnExitClicked(ActionEvent event) throws IOException {

        if(gf.getInventory().hasItem("Screwdriver")) {
            App.setRoot("hallway");
            gf.getCurrUser().getCurrSave().markRoomCompleted(gf.getCurrRoom());
            gf.setCurrRoom(gf.getCurrRoom().getExitByNextRoomName("Hallway").getNextRoom());
            System.out.println(gf.getCurrRoom());
        } else {
            exitMessage.setVisible(true);
        }
    }
}
