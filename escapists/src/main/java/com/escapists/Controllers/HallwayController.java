package com.escapists.Controllers;

import com.escapists.App;
import com.model.Coding.Gameplay.GameFacade;
import com.model.Coding.Gameplay.InteractItems.Inventory;
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
import javafx.beans.binding.Bindings;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * Controller for the hallway screen/room
 * @author Mason, Jeffen
 */
public class HallwayController {

    @FXML
    public Pane overlayPane;

    @FXML
    public TextArea txtAreaSurv;

    GameFacade gf = GameFacade.getInstance();
    private boolean clicked = false;

    @FXML
    AnchorPane mainAp;

    @FXML
    private TextField wireAnswr;

    @FXML
    private Button btnEnterSurv;

    @FXML
    private Button btnStorCloset;

    @FXML
    private Button btnSurvRoom;

    @FXML
    private Button btnVent;

    @FXML
    private ImageView bgImage;

    @FXML
    private Button btnEnterStor;

    @FXML
    private TextField keyPadAnswr;

    @FXML
    private TextArea txtAreaKeyCode;

    @FXML
    private TextArea txtVent;


    /**
     * Initialize method to initialize required things for the room
     */
    @FXML
    public void initialize() {
        Coolui.layerPage(mainAp); // this function call adds the Hud
        Progress currSave = gf.getCurrUser().getCurrSave();
        Room currRoom = gf.getCurrRoom();
        System.out.println(currRoom);
        if (currRoom == null) {
            System.err.println("CellController.initialize(): currRoom is null - skipping UI restore");
            return;
        }

        // Ensure save knows about all puzzles in this room (idempotent)
        currSave.initializeRoomPuzzles(currRoom);

        if(clicked){
            txtVent.setVisible(true);
        }
    }

    /**
     * Shows UI elements for the storage room riddle.
     */
    @FXML
    void btnStorClosetClicked() {

        Puzzle puzzle = gf.getCurrRoom().getPuzzle("KeyCode");

        if(puzzle.getIsCompleted()){
            txtAreaKeyCode.setText("Riddle Completed");

            btnEnterStor.setDisable(true);
            keyPadAnswr.setDisable(true);
            keyPadAnswr.setVisible(true);
            txtAreaKeyCode.setVisible(true);
            btnEnterStor.setVisible(true);
            return;
        }

        txtAreaKeyCode.setText(puzzle.getDescription());
        // show UI
        keyPadAnswr.setVisible(true);
        txtAreaKeyCode.setVisible(true);
        btnEnterStor.setVisible(true);
    }

    /**
     * Shows UI elements for surveillance room riddle
     */
    @FXML
    void btnSurvRoomClicked() {

        Room currRoom = gf.getCurrRoom();
        Puzzle puzzle = gf.getCurrRoom().getPuzzle("SecurityWires");

        if(puzzle.getIsCompleted()){
            txtAreaSurv.setText("Riddle Completed");
            btnEnterSurv.setDisable(true);
            wireAnswr.setDisable(true);
            wireAnswr.setVisible(true);
            txtAreaSurv.setVisible(true);
            btnEnterSurv.setVisible(true);
            return;
        }

        txtAreaSurv.setText(puzzle.getDescription());
        // show UI
        wireAnswr.setVisible(true);
        txtAreaSurv.setVisible(true);
        btnEnterSurv.setVisible(true);

    }

    /**
     * If the player has completed the room and has the necessary items, they proceed to the vent to escape
     */
    @FXML
    void btnVentClicked() throws IOException {

        Inventory inven = gf.getInventory();
        txtVent.setVisible(true);
        clicked = true;


        if(!inven.hasItem("Screwdriver") && !inven.hasItem("KeyCard")) {
            txtVent.setText("Hmmmm, I need something to pop the vent open and disable the security measures.");
        }
        else if(!inven.hasItem("KeyCard")) {
            txtVent.setText("Hmmmm, I need something to disable the security measures.");
        }
        else if(!inven.hasItem("Screwdriver")) {
            txtVent.setText("Hmmmm, I need something to pop the vent open.");
        }
        else {
            App.safeSetGameplayRoot("vent");
            txtVent.setVisible(false);
        }
    }

    /**
     * Enters the storage room if player gets the riddle correct
     */
    @FXML
    void btnEnterStorClicked() throws IOException {
        Room currRoom = gf.getCurrRoom(); // WARNING: If a user is logged in who has a save thats current room is not the cell, there will be errors cause the wrong puzzles are assumed
        Progress currSave = gf.getCurrUser().getCurrSave();

        String answer = keyPadAnswr.getText();
        boolean solved = currRoom.getPuzzle("KeyCode").checkAnswer(currRoom.getPuzzle("KeyCode").userAnswer(answer));

        if (solved) {
            txtAreaKeyCode.setText("Riddle Completed");
            currSave.setPuzzleCompleted(currRoom, currRoom.getPuzzle("KeyCode"), true);
            btnEnterStor.setDisable(true);
            App.safeSetGameplayRoot("storageRoom");
        } else {
            keyPadAnswr.setText("Wrong! Try again");
        }
    }

    /**
     * Enters the surveillance room if player gets the riddle correct
     */
    @FXML
    void btnEnterSurvClicked() throws IOException {
        Room currRoom = gf.getCurrRoom();
        Progress currSave = gf.getCurrUser().getCurrSave();

        String answer = wireAnswr.getText();
        boolean solved = currRoom.getPuzzle("SecurityWires").checkAnswer(currRoom.getPuzzle("SecurityWires").userAnswer(answer));

        if (solved) {
            txtAreaSurv.setText("Riddle Completed");
            currSave.setPuzzleCompleted(currRoom, currRoom.getPuzzle("SecurityWires"), true);
            btnEnterSurv.setDisable(true);
            System.out.println(currRoom);
            App.safeSetGameplayRoot("surveillanceRoom");
        } else {
            wireAnswr.setText("Wrong! Try again");
        }
    }
}