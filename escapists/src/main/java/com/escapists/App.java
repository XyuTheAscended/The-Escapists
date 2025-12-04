package com.escapists;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayDeque;

/**
 * JavaFX App
 */
public class App extends Application {
    private final static int FXML_HISTORY_MAX = 7;
    private static ArrayDeque<String> lastFxmls = new ArrayDeque<>();
    private static String currFxml;
    private static void recordLastFxml(String fxmlName) {
      if (lastFxmls.size() >= FXML_HISTORY_MAX) 
          lastFxmls.removeFirst();

      lastFxmls.add(fxmlName);
    }

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        String startingFxml = "mainMenu";
        scene = new Scene(loadFXML(startingFxml), 640, 480);
        stage.setScene(scene);
        currFxml = startingFxml;
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
        recordLastFxml(currFxml);
        currFxml = fxml;
    }

    public static void safeSetRoot(String fxml) {
      try {
        setRoot(fxml);
      } catch (IOException e1) {
        e1.printStackTrace();
        throw new Error("Something really bad happened when we tried to set the root");
      }
    }

    public static void setRootToPrev() {
      if (lastFxmls.size() <= 0) {
        System.out.println("IMPSOIBLE! nO MORE FXML HISTORY LEFT");
        return;
      }

      String lastFxml = lastFxmls.removeLast();
      try {
        scene.setRoot(loadFXML(lastFxml));
        // dont record a "lastFxml" here since we're rewinding
        currFxml = lastFxml;
      } catch (IOException e1) {
        e1.printStackTrace();
        throw new Error("Something really bad happened when we tried to set the root");
      }
        
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    

    public static void main(String[] args) {
        launch();
    }

    public static Scene getScene() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getScene'");
    }

}