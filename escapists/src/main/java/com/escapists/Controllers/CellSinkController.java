package com.escapists.Controllers;

import com.escapists.App;
import com.model.Coding.Gameplay.GameFacade;
import com.model.Coding.Gameplay.InteractItems.Item;
import com.model.Coding.UiHelp.Coolui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class CellSinkController {
    private static boolean keyTaken = false;


    @FXML
    AnchorPane mainAp;

    @FXML
    ImageView bgImgView; 

    @FXML
    private Button btnKey;

    @FXML
    public void initialize() {
        Coolui.layerPage(mainAp); // this function call adds the Hud
        btnKey.setVisible(false);

        Coolui.setupItemPickup(btnKey);

        if (keyTaken) {
            String openSinkImgLink = getClass().getResource("/lootedSink.png").toExternalForm();
            bgImgView.setImage(new Image(openSinkImgLink));

        }
    }

    @FXML
    private Button btnBack;

    @FXML
    private Button btnDrain;

    @FXML
    void btnBackClicked(ActionEvent event) throws IOException {
        App.setRootToPrev();
    }

    @FXML
    void btnDrainClicked(ActionEvent event) throws IOException {
        if (keyTaken) return;
        String openSinkImgLink = getClass().getResource("/lootedSink.png").toExternalForm();
        bgImgView.setImage(new Image(openSinkImgLink));
        btnDrain.setVisible(false);
        btnKey.setVisible(true);
        
        
        // App.setRoot("sinkWithKey");
    }

    // @FXML
    // void btnKeyClicked(ActionEvent event)  {
    //     if (keyTaken) return;
        
        

    //     keyTaken = true;
    //     System.out.println(GameFacade.getInstance().getInventory().displayInventory());

    // }
}
