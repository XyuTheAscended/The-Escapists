package com.escapists.Controllers;
import java.io.IOException;

import com.escapists.App;

import javafx.fxml.FXML;

public class LandingController {
  @FXML
  private void switchToLogin() throws IOException {
      App.setRoot("login");
  }
}
