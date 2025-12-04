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
import javafx.beans.binding.Bindings;
import javafx.scene.layout.Pane;

import java.io.IOException;


public class HallwayController {

    @FXML
    public Pane overlayPane;

    @FXML
    public TextArea txtAreaSurv;

    GameFacade gf = GameFacade.getInstance();
    UIDataCache UIDC = UIDataCache.getInstance();

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

        // 1) Make background always fill the AnchorPane
        bgImage.fitWidthProperty().bind(mainAp.widthProperty());
        bgImage.fitHeightProperty().bind(mainAp.heightProperty());

        // zero out any leftover layout offsets (safe)
        btnSurvRoom.setLayoutX(0); btnSurvRoom.setLayoutY(0);
        btnStorCloset.setLayoutX(0); btnStorCloset.setLayoutY(0);
        btnVent.setLayoutX(0); btnVent.setLayoutY(0);

        // --------- SURV/STORE/VENT buttons (use layout in overlayPane) ----------
        // btnSurvRoom (orig x=122, y=562, w=164, h=162)
        btnSurvRoom.layoutXProperty().bind(overlayPane.widthProperty().multiply(0.0635));
        btnSurvRoom.layoutYProperty().bind(overlayPane.heightProperty().multiply(0.5203));
        btnSurvRoom.prefWidthProperty().bind(overlayPane.widthProperty().multiply(0.0854166667));
        btnSurvRoom.prefHeightProperty().bind(overlayPane.heightProperty().multiply(0.15));

        // btnStorCloset (orig x=1765, y=568, w=130, h=220)
        btnStorCloset.layoutXProperty().bind(overlayPane.widthProperty().multiply(0.9192708333));
        btnStorCloset.layoutYProperty().bind(overlayPane.heightProperty().multiply(0.5259259259));
        btnStorCloset.prefWidthProperty().bind(overlayPane.widthProperty().multiply(0.0677083333));
        btnStorCloset.prefHeightProperty().bind(overlayPane.heightProperty().multiply(0.2037037037));

        // btnVent (orig x=1095, y=583, w=73, h=58)
        btnVent.layoutXProperty().bind(overlayPane.widthProperty().multiply(0.5703125));
        btnVent.layoutYProperty().bind(overlayPane.heightProperty().multiply(0.5398148148));
        btnVent.prefWidthProperty().bind(overlayPane.widthProperty().multiply(0.0380208333));
        btnVent.prefHeightProperty().bind(overlayPane.heightProperty().multiply(0.0537037037));



        // --------- RESTORE UI STATE (unchanged) ----------------
        keyPadAnswr.setVisible(UIDC.isUIVisible(currRoom.getName(), "keyPadAnswr"));
        txtAreaKeyCode.setVisible(UIDC.isUIVisible(currRoom.getName(), "txtAreaKeyCode"));
        btnEnterStor.setVisible(UIDC.isUIVisible(currRoom.getName(), "btnEnterStor"));

        keyPadAnswr.setText(UIDC.getUIText(currRoom.getName(), "keyPadAnswr"));
        txtAreaKeyCode.setText(UIDC.getUIText(currRoom.getName(), "txtAreaKeyCode"));

        btnEnterStor.setDisable(UIDC.isUIDisabled(currRoom.getName(), "btnEnterStor"));
        keyPadAnswr.setDisable(UIDC.isUIDisabled(currRoom.getName(), "keyPadAnswr"));

        wireAnswr.setVisible(UIDC.isUIVisible(currRoom.getName(), "wireAnswr"));
        txtAreaSurv.setVisible(UIDC.isUIVisible(currRoom.getName(), "txtAreaSurv"));
        btnEnterSurv.setVisible(UIDC.isUIVisible(currRoom.getName(), "btnEnterSurv"));

        wireAnswr.setText(UIDC.getUIText(currRoom.getName(), "wireAnswr"));
        txtAreaSurv.setText(UIDC.getUIText(currRoom.getName(), "txtAreaSurv"));

        btnEnterSurv.setDisable(UIDC.isUIDisabled(currRoom.getName(), "btnEnterSurv"));
    }


    @FXML
    void btnStorClosetClicked(ActionEvent event) {

        Room currRoom = gf.getCurrRoom();
        Progress currSave = gf.getCurrUser().getCurrSave();
        Puzzle puzzle = gf.getCurrRoom().getPuzzle("KeyCode");

        if(puzzle.getIsCompleted()){
            txtAreaKeyCode.setText("Riddle Completed");
            UIDC.setUIText(currRoom.getName(), "txtAreaKeyCode", "Riddle Completed");
            btnEnterStor.setDisable(true);
            keyPadAnswr.setDisable(true);
            UIDC.setUIDisabled(currRoom.getName(), "btnEnterStor", true);
            UIDC.setUIDisabled(currRoom.getName(), "keyPadAnswr", true);
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

        // persist UI visibility
        UIDC.setUIVisible(currRoom.getName(), "keyPadAnswr", true);
        UIDC.setUIVisible(currRoom.getName(), "txtAreaKeyCode", true);
        UIDC.setUIVisible(currRoom.getName(), "btnEnterStor", true);

        UIDC.setUIText(currRoom.getName(), "txtAreaKeyCode", txtAreaKeyCode.getText());
        UIDC.setUIText(currRoom.getName(), "keyPadAnswr", keyPadAnswr.getText());

    }

    @FXML
    void btnSurvRoomClicked(ActionEvent event) {

        Room currRoom = gf.getCurrRoom();
        Puzzle puzzle = gf.getCurrRoom().getPuzzle("SecurityWires");

        if(puzzle.getIsCompleted()){
            txtAreaSurv.setText("Riddle Completed");
            UIDC.setUIText(currRoom.getName(), "txtAreaSurv", "Riddle Completed");
            btnEnterSurv.setDisable(true);
            wireAnswr.setDisable(true);
            UIDC.setUIDisabled(currRoom.getName(), "btnEnterSurv", true);
            UIDC.setUIDisabled(currRoom.getName(), "wireAnswr", true);
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



        // persist UI visibility
        UIDC.setUIVisible(currRoom.getName(), "wireAnswr", true);
        UIDC.setUIVisible(currRoom.getName(), "txtAreaSurv", true);
        UIDC.setUIVisible(currRoom.getName(), "btnEnterSurv", true);

        UIDC.setUIText(currRoom.getName(), "txtAreaSurv", txtAreaSurv.getText());
        UIDC.setUIText(currRoom.getName(), "wireAnswr", wireAnswr.getText());
    }

    @FXML
    void btnVentClicked(ActionEvent event) {

        Room currRoom = gf.getCurrRoom();
        Progress currSave = gf.getCurrUser().getCurrSave();

    }

    @FXML
    void btnEnterStorClicked(ActionEvent event) throws IOException {
        Room currRoom = gf.getCurrRoom(); // WARNING: If a user is logged in who has a save thats current room is not the cell, there will be errors cause the wrong puzzles are assumed
        Progress currSave = gf.getCurrUser().getCurrSave();

        String answer = keyPadAnswr.getText();
        boolean solved = currRoom.getPuzzle("KeyCode").checkAnswer(currRoom.getPuzzle("KeyCode").userAnswer(answer));

        if (solved) {
            txtAreaKeyCode.setText("Riddle Completed");
            keyPadAnswr.setText("You may proceed!");
            UIDC.setUIText(currRoom.getName(), "txtAreaKeyCode", "You May Proceed");
            UIDC.setUIText(currRoom.getName(), "keyPadAnswr", "Riddle Completed");
            currSave.setPuzzleCompleted(currRoom, currRoom.getPuzzle("KeyCode"), true);
            btnEnterStor.setDisable(true);
            UIDC.setUIDisabled(currRoom.getName(), "btnEnterStor", true);
            App.setRoot("storageRoom");
        } else {
            keyPadAnswr.setText("Wrong! Try again");
            UIDC.setUIText(currRoom.getName(), "keyPadAnswr", "Wrong! Try again");
        }
    }


    @FXML
    void btnEnterSurvClicked(ActionEvent event) {
        Room currRoom = gf.getCurrRoom();
        Progress currSave = gf.getCurrUser().getCurrSave();

        String answer = wireAnswr.getText();
        boolean solved = currRoom.getPuzzle("SecurityWires").checkAnswer(currRoom.getPuzzle("SecurityWires").userAnswer(answer));

        if (solved) {
            txtAreaSurv.setText("Riddle Completed");
            wireAnswr.setText("You may proceed!");
            UIDC.setUIText(currRoom.getName(), "txtAreaSurv", "You May Proceed");
            UIDC.setUIText(currRoom.getName(), "wireAnswr", "Riddle Completed");
            currSave.setPuzzleCompleted(currRoom, currRoom.getPuzzle("SecurityWires"), true);
            btnEnterSurv.setDisable(true);
            UIDC.setUIDisabled(currRoom.getName(), "btnEnterSurv", true);
            System.out.println(currRoom);
            // App.setRoot("surveulenceRom");
        } else {
            wireAnswr.setText("Wrong! Try again");
            UIDC.setUIText(currRoom.getName(), "wireAnswr", "Wrong! Try again");
        }
    }
}