package com.escapists.Controllers;

import com.escapists.App;
import com.model.Coding.Gameplay.GameFacade;
import com.model.Coding.Gameplay.InteractItems.Item;
import com.model.Coding.Gameplay.Map.Room;
import com.model.Coding.Progress.Progress;
import com.model.Coding.UiHelp.Coolui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * Controller for the cell door screen
 * @author Mason
 */
public class CellDoorController {

    GameFacade gf = GameFacade.getInstance();
    boolean completed = false;

    @FXML
    AnchorPane root;

    /**
     * Initialize method to initialize required things for the room.
     */
    @FXML
    public void initialize() {
        if (gf.getCurrUser() == null) gf.quickTestLogin();
        Coolui.layerPage(root); // this function call adds the Hud
        
    }
    
    @FXML
    private Button btnLock;

    @FXML
    private Button btnTurn;

    @FXML
    private TextArea txtAreaKeyMessage;

    /**
     * When the lock is clicked, if the player has the key and has completed the puzzles in the cell room, it unlocks.
     */
    @FXML
    void btnLockClicked() throws IOException {

        Room currRoom = gf.getCurrRoom();
        Progress currSave = gf.getCurrUser().getCurrSave();


        Item key = gf.getInventory().getItem("key");
        if(gf.getInventory().hasItem("Key")) {
            gf.getInventory().removeItem(key);
            gf.getCurrUser().getCurrSave().setPuzzleCompleted(currRoom, currRoom.getPuzzle("CellDoorLock"), true);
            completed = true;
        }

        if(!completed) {
            txtAreaKeyMessage.setVisible(true);
        }

        if(completed) {
            txtAreaKeyMessage.setText("Hmmm, it seems I'm missing something.");
            txtAreaKeyMessage.setVisible(true);
        }

        if(currSave.allPuzzlesCompleted(gf.getCurrRoom())) {
            App.safeSetGameplayRoot("openCellDoor");
        }
    }

    /**
     * "Turns" around when button is clicked. Sets the screen to the cell.
     */
    @FXML
    void btnTurnClicked() throws IOException {
        App.safeSetGameplayRoot("cell");
    }
}
