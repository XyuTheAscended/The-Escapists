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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * Controller for the surveillance room
 * @author Mason, Jeffen
 */
public class SurveillanceRoom {


    GameFacade gf = GameFacade.getInstance();

    /**
     *Initialize method to initialize required things for the room
     */
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

        if(gf.getCurrRoom().getPuzzle("Cameras").getIsCompleted()) {
            System.out.println("ye");
            bgCamsOff.setVisible(true);
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

    /**
     * Shows UI elements for the camera riddle
     */
    @FXML
    void btnCamPuzzleClicked() {

        Room currRoom = gf.getCurrRoom();
        Puzzle puzzle = gf.getCurrRoom().getPuzzle("Cameras");
        txtRiddleCams.setText(puzzle.getDescription());

        if(puzzle.getIsCompleted()){
            txtRiddleCams.setText("Riddle Completed");
            btnEnterCams.setDisable(true);
            riddleAnswrCams.setDisable(true);
            riddleAnswrCams.setVisible(true);
            txtRiddleCams.setVisible(true);
            btnEnterCams.setVisible(true);
            return;
        }

        txtRiddleCams.setVisible(true);
        riddleAnswrCams.setVisible(true);
        btnEnterCams.setVisible(true);
    }

    /**
     * Checks answer for camera riddle. If correct, cameras turn off
     */
    @FXML
    void btnEnterCamsClicked() {

        Room currRoom = gf.getCurrRoom();
        Progress currSave = gf.getCurrUser().getCurrSave();
        String answer = riddleAnswrCams.getText();
        boolean solved = currRoom.getPuzzle("Cameras").checkAnswer(currRoom.getPuzzle("Cameras").userAnswer(answer));

        if (solved) {

            txtRiddleCams.setText("Riddle Completed");
            currSave.setPuzzleCompleted(currRoom, currRoom.getPuzzle("Cameras"), true);
            btnEnterCams.setDisable(true);
            System.out.println(currRoom);
            bgCamsOff.setVisible(true);
        } else {
            riddleAnswrCams.setText("Wrong! Try again");
        }
    }

    /**
     * Handles exit logic for surveillance room. Player can only leave if they have the keycard and have turned off the cameras
     */
    @FXML
    void btnExitClicked(ActionEvent event) throws IOException {

        if(gf.getInventory().hasItem("KeyCard") && gf.getCurrUser().getCurrSave().allPuzzlesCompleted(gf.getCurrRoom())) {
            App.safeSetGameplayRoot("hallway");
            gf.getCurrUser().getCurrSave().markRoomCompleted(gf.getCurrRoom());
            gf.setCurrRoom(gf.getCurrRoom().getExitByNextRoomName("Hallway").getNextRoom());
            System.out.println(gf.getCurrRoom());
        } else {
            exitMessage.setVisible(true);
        }
    }

    /**
     * Shows UI elements for drawer riddle
     */
    @FXML
    void btnDrawerClicked() {

        Room currRoom = gf.getCurrRoom();
        Puzzle puzzle = gf.getCurrRoom().getPuzzle("Drawer");
        txtRiddleDrawer.setText(puzzle.getDescription());

        if(puzzle.getIsCompleted()){
            txtRiddleDrawer.setText("Riddle Completed");
            btnEnterDrawer.setDisable(true);
            riddleAnswrDrawer.setDisable(true);
            riddleAnswrDrawer.setVisible(true);
            txtRiddleDrawer.setVisible(true);
            riddleAnswrDrawer.setVisible(true);
            return;
        }

        txtRiddleDrawer.setVisible(true);
        riddleAnswrDrawer.setVisible(true);
        btnEnterDrawer.setVisible(true);
    }

    /**
     * Checks answer for drawer riddle. Changes to the open drawer screen if correct
     */
    @FXML
    void btnEnterDrawerClicked(ActionEvent event) throws IOException {
        Room currRoom = gf.getCurrRoom();
        Progress currSave = gf.getCurrUser().getCurrSave();
        String answer = riddleAnswrDrawer.getText();
        boolean solved = currRoom.getPuzzle("Drawer").checkAnswer(currRoom.getPuzzle("Drawer").userAnswer(answer));

        if (solved) {
            txtRiddleDrawer.setText("Riddle Completed");
            currSave.setPuzzleCompleted(currRoom, currRoom.getPuzzle("Drawer"), true);
            btnEnterDrawer.setDisable(true);
            System.out.println(currRoom);
            App.safeSetGameplayRoot("openDrawer");
        } else {
            riddleAnswrDrawer.setText("Wrong! Try again");
        }
    }
}
