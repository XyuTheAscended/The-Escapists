package com.escapists.Controllers;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.escapists.App;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * Controller for game landing page
 * @author Jeffen
 */
public class LandingController implements Initializable {

    /**
     * Switches to the log-in screen
     */
  @FXML
  private void switchToLogin() throws IOException {
      App.setRoot("login");
  }

    /**
     * Initialize method
     * @param url
     * @param rb
     */
  @Override
  public void initialize(URL url, ResourceBundle rb) {

  }
}
