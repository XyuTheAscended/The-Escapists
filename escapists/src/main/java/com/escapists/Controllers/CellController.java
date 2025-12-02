package com.escapists.Controllers;

import com.model.Coding.Gameplay.GameFacade;
import com.model.Coding.Gameplay.InteractItems.Puzzle;
import com.model.Coding.Gameplay.Map.Room;
import com.model.Coding.Progress.Progress;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;

public class CellController {


    GameFacade gf = GameFacade.getInstance();

    Room currRoom = gf.getCurrRoom();
    Progress currSave = gf.getCurrUser().getCurrSave();

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
    void btnCellMateClicked(ActionEvent event) {
        cellMateDialog.setVisible(true);
    }

    @FXML
    void btnSinkClicked(ActionEvent event) {

    }

    @FXML
    void btnNoteClicked(ActionEvent event) {
        ArrayList<Room> rooms = gf.getRooms();
        for (Room room : rooms) {
            System.out.println(room.getName());
        }
        System.out.println(currRoom);
        noteRiddleAnswrBox.setText(currRoom.getPuzzle("note").getDescription());
    }
}
