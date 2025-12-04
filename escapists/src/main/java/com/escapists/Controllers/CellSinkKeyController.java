package com.escapists.Controllers;

import com.escapists.App;
import com.model.Coding.Gameplay.GameFacade;
import com.model.Coding.Gameplay.InteractItems.Item;
import com.model.Coding.Progress.UIDataCache;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class CellSinkKeyController {

    GameFacade gf = GameFacade.getInstance();
    UIDataCache UIDC = UIDataCache.getInstance();

    @FXML
    public void initialize() {
        btnKey.setDisable(UIDC.isUIDisabled(gf.getCurrRoom().getName(), "btnKey"));
    }

    @FXML
    private Button btnBack;

    @FXML
    private Button btnKey;

    @FXML
    void btnBackClicked(ActionEvent event) throws IOException {
        App.setRoot("sink");
    }

    @FXML
    void btnKeyClicked(ActionEvent event) {
        // i think this is supposed to be in json
        Item key = Item.loadItem("Key");
        gf.getInventory().addItem(key);
        btnKey.setDisable(true);
        UIDC.setUIDisabled(gf.getCurrRoom().getName(), "btnKey", true);
        // temp
        System.out.println(gf.getInventory().displayInventory());
    }
}
