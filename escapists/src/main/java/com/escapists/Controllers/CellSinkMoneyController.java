package com.escapists.Controllers;

import com.escapists.App;
import com.model.Coding.Gameplay.GameFacade;
import com.model.Coding.Gameplay.InteractItems.Item;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class CellSinkMoneyController {

    GameFacade gf = GameFacade.getInstance();

    @FXML
    private Button btnBack;

    @FXML
    private Button btnMoney;

    @FXML
    void btnBackClicked(ActionEvent event) throws IOException {
        App.setRoot("sink");
    }

    @FXML
    void btnMoneyClicked(ActionEvent event) {
        // i think this is supposed to be in json
        Item money = new Item(1,"Money", "Money to bribe the warden.");
        gf.getInventory().addItem(money);
        // temp
        System.out.println(gf.getInventory().displayInventory());
    }
}
