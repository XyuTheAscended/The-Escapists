package com.escapists;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;

import com.model.Coding.UiHelp.UiBuilder;

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
    private static String DEBUG_STARTER_ROOM_NAME = null; // USED to set starter room for testing purposes only. will be null during production
    private static String PRODUCTION_STARTER_PAGE = "landing"; // USED to set starter room for testing purposes only. will be null during production

    private static Scene scene;

    // this thing is for cacheing fxml pages so we dont have to entirely reload them everytime we switch roots. Now ideally, perhaps
    private static HashMap<String, Parent> fxmlCache = new HashMap<>();

    public static void clearFxmlCache() {
      fxmlCache.clear();
    }

    public static String[] CACHE_BLACK_LIST = {
      "save"
    };

    @Override
    public void start(Stage stage) throws IOException {
        if (DEBUG_STARTER_ROOM_NAME != null) {
          Parent gameplayPage =  UiBuilder.convertRootToScrollable((AnchorPane)loadFXML(DEBUG_STARTER_ROOM_NAME));
          scene = new Scene(gameplayPage, 640, 480);
          stage.setScene(scene);
          fxmlCache.put(DEBUG_STARTER_ROOM_NAME, gameplayPage);
          currFxml = DEBUG_STARTER_ROOM_NAME;
        } else {
          String startingFxml = PRODUCTION_STARTER_PAGE;
          scene = new Scene(loadFXML(startingFxml), 640, 480);
          stage.setScene(scene);
          currFxml = startingFxml;
        }
        stage.show();
    }

    /**
     * Sets root of the scene
     * @param fxml name of fxml page we want
     * @throws IOException for if something goes wrong when loading FXML
     */
    public static void setRoot(String fxml) throws IOException {
      setRoot(fxml, true, false);
    }

    /**
     * Sets root of fxml page while also specifying other things
     * @param fxml
     * @param recordPageHistory determines if we cache this page's root so later we dont gotta rerender it
     * @param isGameplayPage determines if next page we switch to 
     * @throws IOException
     */
    public static void setRoot(String fxml, boolean recordPageHistory, boolean isGameplayPage) throws IOException {
      if (recordPageHistory) recordLastFxml(currFxml);
      currFxml = fxml;
      Parent fxmlPage = fxmlCache.get(fxml); 
      if (fxmlPage == null) {
        fxmlPage = loadFXML(fxml);
        if (isGameplayPage) fxmlPage = UiBuilder.convertRootToScrollable((AnchorPane) fxmlPage);

        if (!Arrays.asList(CACHE_BLACK_LIST).contains(fxml)) fxmlCache.put(fxml, fxmlPage);
      }
      
      
      scene.setRoot(fxmlPage);
    }

    /**
     * Way to safely set root of scene when its a gameplay page. handles errors via try block
     * @param fxml
     */
    public static void safeSetGameplayRoot(String fxml) {
      try {
        setRoot(fxml, true, true);
      } catch (IOException e1) {
        e1.printStackTrace();
        throw new Error("Something really bad happened when we tried to set the root");
      }
    }

    /**
     * Way to safely set root of scene when its a normal page via try blokcs
     * @param fxml
     */
    public static void safeSetRoot(String fxml) {
      try {
        setRoot(fxml, true, false);
      } catch (IOException e1) {
        e1.printStackTrace();
        throw new Error("Something really bad happened when we tried to set the root");
      }
    }

    /**
     * Overwrites a page stored in cache, repalcing what Parent object is cached as its root
     * @param fxmlKey
     * @param fxmlPage
     */
    public static void overwritePageCache(String fxmlKey, Parent fxmlPage) {
      fxmlCache.put(fxmlKey, fxmlPage);
    }

    /**
     * Kind of like a back button method for going to last fxml page
     */
    public static void setRootToPrev() {
      if (lastFxmls.size() <= 0) {
        System.out.println("IMPSOIBLE! nO MORE FXML HISTORY LEFT");
        return;
      }

      String lastFxml = lastFxmls.removeLast();
      try {
        setRoot(lastFxml, false, false);
        // dont record a "lastFxml" here since we're rewinding
      } catch (IOException e1) {
        e1.printStackTrace();
        throw new Error("Something really bad happened when we tried to set the root");
      }
        
    }

    /**
     * Loads fxml file from our resources
     * @param fxml name of fxml without the .fxml extension
     * @return raw root of the fxml page
     * @throws IOException
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /**
     * Gets the last page name from our page history
     * @return
     */
    public static String getLastPageName() {
      return lastFxmls.peekLast();
    }

    /**
     * launches stuff so things render
     * @param args
     */
    public static void main(String[] args) {
        launch();
    }


}