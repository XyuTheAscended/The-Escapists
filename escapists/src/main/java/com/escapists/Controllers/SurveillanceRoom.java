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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class SurveillanceRoom {


    GameFacade gf = GameFacade.getInstance();
    UIDataCache UIDC = UIDataCache.getInstance();

    @FXML
    public void initialize() {
        Coolui.layerPage(mainAp); // this function call adds the Hud
        gf.setCurrRoom(gf.findRoomByName("SurveillanceRoom"));
        Progress currSave = gf.getCurrUser().getCurrSave();
        Room currRoom = gf.getCurrRoom();

        System.out.println(currRoom);
        if (currRoom == null) {
            System.err.println("CellController.initialize(): currRoom is null - skipping UI restore");
            return;
        }
        // Ensure save knows about all puzzles in this room (idempotent)
        currSave.initializeRoomPuzzles(currRoom);

        exitMessage.setVisible(UIDC.isUIVisible(currRoom.getName(), "exitMessage"));
        bgCamsOff.setVisible(UIDC.isUIVisible(currRoom.getName(), "bgCamsOff"));
        riddleAnswrDrawer.setVisible(UIDC.isUIVisible(currRoom.getName(), "riddleAnswrDrawer"));
        txtRiddleDrawer.setVisible(UIDC.isUIVisible(currRoom.getName(), "txtRiddleDrawer"));
        btnEnterDrawer.setVisible(UIDC.isUIVisible(currRoom.getName(), "btnEnterDrawer"));

        riddleAnswrDrawer.setText(UIDC.getUIText(currRoom.getName(), "riddleAnswrDrawer"));
        txtRiddleDrawer.setText(UIDC.getUIText(currRoom.getName(), "txtRiddleDrawer"));

        btnEnterDrawer.setDisable(UIDC.isUIDisabled(currRoom.getName(), "btnEnterDrawer"));

        riddleAnswrCams.setVisible(UIDC.isUIVisible(currRoom.getName(), "riddleAnswrCams"));
        txtRiddleCams.setVisible(UIDC.isUIVisible(currRoom.getName(), "txtRiddleCams"));
        btnEnterCams.setVisible(UIDC.isUIVisible(currRoom.getName(), "btnEnterCams"));

        riddleAnswrCams.setText(UIDC.getUIText(currRoom.getName(), "riddleAnswrCams"));
        txtRiddleCams.setText(UIDC.getUIText(currRoom.getName(), "txtRiddleCams"));

        btnEnterCams.setDisable(UIDC.isUIDisabled(currRoom.getName(), "btnEnterCams"));

        if(gf.getCurrRoom().getPuzzle("Cameras").getIsCompleted()) {
            System.out.println("ye");
            bgCamsOff.setVisible(true);
            UIDC.setUIDisabled(currRoom.getName(), "bgCamsOff", true);
        }
    }

    @FXML
    public Button btnDrawer;

    @FXML
    public AnchorPane mainAp;

    @FXML
    private ImageView bgCamsOff;

    @FXML
    private Button btnCamPuzzle;

    @FXML
    private Button btnEnterCams;

    @FXML
    private Button btnEnterDrawer;

    @FXML
    private Button btnExit;

    @FXML
    private TextField riddleAnswrCams;

    @FXML
    private TextField riddleAnswrDrawer;

    @FXML
    private TextArea txtRiddleCams;

    @FXML
    private TextArea txtRiddleDrawer;

    @FXML
    private TextArea exitMessage;

    @FXML
    void btnCamPuzzleClicked(ActionEvent event) {

        Room currRoom = gf.getCurrRoom();
        Puzzle puzzle = gf.getCurrRoom().getPuzzle("Cameras");
        txtRiddleCams.setText(puzzle.getDescription());

        if(puzzle.getIsCompleted()){
            txtRiddleCams.setText("Riddle Completed");
            UIDC.setUIText(currRoom.getName(), "txtRiddleCams", "Riddle Completed");
            btnEnterCams.setDisable(true);
            riddleAnswrCams.setDisable(true);
            UIDC.setUIDisabled(currRoom.getName(), "btnEnterCams", true);
            UIDC.setUIDisabled(currRoom.getName(), "riddleAnswrCams", true);
            riddleAnswrCams.setVisible(true);
            txtRiddleCams.setVisible(true);
            btnEnterCams.setVisible(true);
            return;
        }

        txtRiddleCams.setVisible(true);
        riddleAnswrCams.setVisible(true);
        btnEnterCams.setVisible(true);

        // persist UI visibility
        UIDC.setUIVisible(currRoom.getName(), "riddleAnswrCams", true);
        UIDC.setUIVisible(currRoom.getName(), "txtRiddleCams", true);
        UIDC.setUIVisible(currRoom.getName(), "btnEnterCams", true);

        UIDC.setUIText(currRoom.getName(), "txtRiddleCams", txtRiddleCams.getText());
        UIDC.setUIText(currRoom.getName(), "riddleAnswrCams", riddleAnswrCams.getText());
    }

    @FXML
    void btnEnterCamsClicked(ActionEvent event) {

        Room currRoom = gf.getCurrRoom();
        Progress currSave = gf.getCurrUser().getCurrSave();
        String answer = riddleAnswrCams.getText();
        boolean solved = currRoom.getPuzzle("Cameras").checkAnswer(currRoom.getPuzzle("Cameras").userAnswer(answer));

        if (solved) {

            txtRiddleCams.setText("Riddle Completed");
            UIDC.setUIText(currRoom.getName(), "txtRiddleCams", "Riddle Completed");
            currSave.setPuzzleCompleted(currRoom, currRoom.getPuzzle("Cameras"), true);
            btnEnterCams.setDisable(true);
            UIDC.setUIDisabled(currRoom.getName(), "btnEnterCams", true);
            System.out.println(currRoom);
            bgCamsOff.setVisible(true);
            UIDC.setUIDisabled(currRoom.getName(), "bgCamsOff", true);
        } else {
            riddleAnswrCams.setText("Wrong! Try again");
            UIDC.setUIText(currRoom.getName(), "riddleAnswrCams", "Wrong! Try again");
        }
    }

    @FXML
    void btnExitClicked(ActionEvent event) throws IOException {

        if(gf.getInventory().hasItem("KeyCard") && gf.getCurrUser().getCurrSave().allPuzzlesCompleted(gf.getCurrRoom())) {
            App.setRoot("hallway");
            gf.getCurrUser().getCurrSave().markRoomCompleted(gf.getCurrRoom());
            gf.setCurrRoom(gf.getCurrRoom().getExitByNextRoomName("Hallway").getNextRoom());
            System.out.println(gf.getCurrRoom());
        } else {
            exitMessage.setVisible(true);
            UIDC.setUIVisible(gf.getCurrRoom().getName(), "btnEnterCams", true);
        }
    }

    @FXML
    void btnDrawerClicked(ActionEvent event) {

        Room currRoom = gf.getCurrRoom();
        Puzzle puzzle = gf.getCurrRoom().getPuzzle("Drawer");
        txtRiddleDrawer.setText(puzzle.getDescription());

        if(puzzle.getIsCompleted()){
            txtRiddleDrawer.setText("Riddle Completed");
            UIDC.setUIText(currRoom.getName(), "txtRiddleDrawer", "Riddle Completed");
            btnEnterDrawer.setDisable(true);
            riddleAnswrDrawer.setDisable(true);
            UIDC.setUIDisabled(currRoom.getName(), "btnEnterDrawer", true);
            UIDC.setUIDisabled(currRoom.getName(), "riddleAnswrDrawer", true);
            riddleAnswrDrawer.setVisible(true);
            txtRiddleDrawer.setVisible(true);
            riddleAnswrDrawer.setVisible(true);
            return;
        }

        txtRiddleDrawer.setVisible(true);
        riddleAnswrDrawer.setVisible(true);
        btnEnterDrawer.setVisible(true);

        // persist UI visibility
        UIDC.setUIVisible(currRoom.getName(), "riddleAnswrDrawer", true);
        UIDC.setUIVisible(currRoom.getName(), "txtRiddleDrawer", true);
        UIDC.setUIVisible(currRoom.getName(), "btnEnterDrawer", true);

        UIDC.setUIText(currRoom.getName(), "txtRiddleDrawer", txtRiddleDrawer.getText());
        UIDC.setUIText(currRoom.getName(), "riddleAnswrDrawer", riddleAnswrDrawer.getText());
    }

    @FXML
    void btnEnterDrawerClicked(ActionEvent event) throws IOException {
        Room currRoom = gf.getCurrRoom();
        Progress currSave = gf.getCurrUser().getCurrSave();
        String answer = riddleAnswrDrawer.getText();
        boolean solved = currRoom.getPuzzle("Drawer").checkAnswer(currRoom.getPuzzle("Drawer").userAnswer(answer));

        if (solved) {
            txtRiddleDrawer.setText("Riddle Completed");
            UIDC.setUIText(currRoom.getName(), "txtRiddleDrawer", "Riddle Completed");
            currSave.setPuzzleCompleted(currRoom, currRoom.getPuzzle("Drawer"), true);
            btnEnterDrawer.setDisable(true);
            UIDC.setUIDisabled(currRoom.getName(), "btnEnterDrawer", true);
            System.out.println(currRoom);
            App.setRoot("openDrawer");
        } else {
            riddleAnswrDrawer.setText("Wrong! Try again");
            UIDC.setUIText(currRoom.getName(), "riddleAnswrDrawer", "Wrong! Try again");
        }
    }
}
