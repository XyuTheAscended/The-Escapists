package com.escapists.Controllers;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.escapists.App;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class LandingController implements Initializable {
  @FXML
  private void switchToLogin() throws IOException {
      App.setRoot("login");
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    
  }
}
