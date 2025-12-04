package com.escapists.Controllers;

import com.escapists.App;
import com.model.Coding.Gameplay.GameFacade;
import com.model.Coding.Gameplay.InteractItems.Puzzle;
import com.model.Coding.Gameplay.Map.Room;
import com.model.Coding.Progress.Progress;
import com.model.Coding.Progress.UIDataCache;
import com.model.Coding.UiHelp.Coolui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class StorageRoomController {

    GameFacade gf = GameFacade.getInstance();
    UIDataCache UIDC = UIDataCache.getInstance();


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

        riddleAnswr.setVisible(UIDC.isUIVisible(currRoom.getName(), "riddleAnswr"));
        riddleText.setVisible(UIDC.isUIVisible(currRoom.getName(), "riddleText"));
        btnEnter.setVisible(UIDC.isUIVisible(currRoom.getName(), "btnEnterS"));

        riddleAnswr.setText(UIDC.getUIText(currRoom.getName(), "riddleAnswr"));
        riddleText.setText(UIDC.getUIText(currRoom.getName(), "riddleText"));

        btnEnter.setDisable(UIDC.isUIDisabled(currRoom.getName(), "btnEnter"));
    }

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
    void btnEnterClicked(ActionEvent event) {

        Room currRoom = gf.getCurrRoom();
        Progress currSave = gf.getCurrUser().getCurrSave();
        String answer = riddleAnswr.getText();
        boolean solved = currRoom.getPuzzle("StorageRiddle").checkAnswer(currRoom.getPuzzle("StorageRiddle").userAnswer(answer));

        if (solved) {
            riddleText.setText("Riddle Completed");
            riddleAnswr.setText("You may proceed!");
            UIDC.setUIText(currRoom.getName(), "riddleAnswr", "You May Proceed");
            UIDC.setUIText(currRoom.getName(), "riddleText", "Riddle Completed");
            currSave.setPuzzleCompleted(currRoom, currRoom.getPuzzle("StorageRiddle"), true);
            btnEnter.setDisable(true);
            UIDC.setUIDisabled(currRoom.getName(), "btnEnter", true);
            System.out.println(currRoom);

        } else {
            riddleAnswr.setText("Wrong! Try again");
            UIDC.setUIText(currRoom.getName(), "riddleAnswr", "Wrong! Try again");
        }
    }

    @FXML
    void btnToolBoxClicked(ActionEvent event) {

        Room currRoom = gf.getCurrRoom();
        System.out.println(currRoom);
        Puzzle puzzle = gf.getCurrRoom().getPuzzle("StorageRiddle");
        riddleText.setText(puzzle.getDescription());

        riddleText.setVisible(true);
        riddleAnswr.setVisible(true);
        btnEnter.setVisible(true);

        // persist UI visibility
        UIDC.setUIVisible(currRoom.getName(), "riddleAnswr", true);
        UIDC.setUIVisible(currRoom.getName(), "riddleText", true);
        UIDC.setUIVisible(currRoom.getName(), "btnEnter", true);

        UIDC.setUIText(currRoom.getName(), "riddleText", riddleText.getText());
        UIDC.setUIText(currRoom.getName(), "riddleAnswr", riddleAnswr.getText());
    }

}
