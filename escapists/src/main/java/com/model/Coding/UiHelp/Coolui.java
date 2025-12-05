package com.model.Coding.UiHelp;

import java.io.IOException;

import com.escapists.App;
import com.model.Coding.Gameplay.GameFacade;
import com.model.Coding.Gameplay.Timer;
import com.model.Coding.Gameplay.InteractItems.Item;
import com.model.Coding.Gameplay.InteractItems.Puzzle;
import com.model.Coding.Gameplay.Map.Room;
import com.model.Coding.Progress.Progress;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * The true UI class
 * @author Jeffen 
 */

public class Coolui {
  private static StackPane pauseMenuBoxHolder;
  private static HBox hudHolder; 

  // NEEDS JAVADOCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
  public static void setRegionAbsSize(Region layoutThing /*all sizable nodes inherit from region*/, double sizeX, double sizeY) {
    layoutThing.setMinSize(sizeX, sizeY);
    layoutThing.setPrefSize(sizeX, sizeY);
    layoutThing.setMaxSize(sizeX, sizeY);
  }

  // NEEDS JAVADOCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
  private static Button makePauseMenuButton(String text, Pane parent /*all layouts inherit from pane*/) {
    Button but = new Button(text);
    but.setPrefSize(Double.MAX_VALUE, 70);
    parent.getChildren().add(but);
    return but;
  }

  // NEEDS JAVADOCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
  private static void onPauseButtonClicked() {
    VBox pauseMenuBox = (VBox) pauseMenuBoxHolder.lookup("#pauseMenuBox");
    Button pauseBut = (Button) hudHolder.lookup("#pauseButton");

    if (pauseMenuBox.isVisible()) {
      onResumeInput(pauseMenuBox, pauseBut);
    } else {
      onPauseInput(pauseMenuBox, pauseBut);
    }
  }

  // NEEDS JAVADOCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
  private static void onPauseInput(VBox pauseMenuBox, Button pauseButton) {
    pauseMenuBox.setVisible(true);
    pauseButton.getStyleClass().add("toggled");
  }

  // NEEDS JAVADOCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
  private static void onResumeInput(VBox pauseMenuBox, Button pauseButton) {
    pauseMenuBox.setVisible(false);
    pauseButton.getStyleClass().remove("toggled");
    
  }

  // NEEDS JAVADOCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
  private static StackPane createPauseMenu() {
    StackPane centerWrapper = new StackPane(); // holder for our menu box which. make this stack pane will center it ig
    Label header =  new Label("Paused"); 
    header.getStyleClass().add("menu-header");

    VBox menuBox = new VBox(15, header);
    menuBox.setId("pauseMenuBox");
    menuBox.setAlignment(Pos.TOP_CENTER);
    setRegionAbsSize(menuBox, 480, 600);

    menuBox.setMargin(header, new Insets(15, 0, 15, 0));
    Button resumeBut = makePauseMenuButton("Resume", menuBox);
    Button loadBut = makePauseMenuButton("Load", menuBox);
    Button saveBut = makePauseMenuButton("Save", menuBox);
    Button mainMenuBut = makePauseMenuButton("Main Menu", menuBox);
    
    resumeBut.setOnAction(e -> {
      Button pauseBut = (Button) hudHolder.lookup("#pauseButton");
      onResumeInput(menuBox, pauseBut);
    });

    saveBut.setOnAction(e -> {App.safeSetRoot("save");});
    loadBut.setOnAction(e -> { App.safeSetRoot("loadsave"); });

    mainMenuBut.setOnAction(e -> {App.safeSetRoot("mainMenu");});
    
    menuBox.setVisible(false);

    centerWrapper.getChildren().add(menuBox);
    centerWrapper.setPadding(new Insets(0, 0, 100, 0)); // shifts menu box a little up to compensate for lost space used by hotbar
    centerWrapper.setPickOnBounds(false);
    return centerWrapper;
  }

  // NEEDS JAVADOCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
  private static StackPane makeInvSlot(double size) {
    StackPane iconHolder = new StackPane();
    iconHolder.getStyleClass().add("item-slot");
    ImageView imgView = new ImageView();
    setRegionAbsSize(iconHolder, size, size);
    iconHolder.getChildren().add(imgView);

    return iconHolder;
  }

  private static int MAX_ITEMS = 5;
  // NEEDS JAVADOCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
  private static HBox makeInvFrame(double heightSize) {
    double spacing = (heightSize * 0.1 * MAX_ITEMS) / (MAX_ITEMS-2);
    HBox invFrame = new HBox(spacing);
    invFrame.setId("invFrame");
    double invfSizeX = 250; 
    setRegionAbsSize(invFrame, (heightSize*1.1) * (MAX_ITEMS), heightSize);
    invFrame.setAlignment(Pos.CENTER);
    
    for (int i = 0; i < MAX_ITEMS; ++i) {
      StackPane slot = makeInvSlot(heightSize*0.9);
      invFrame.getChildren().add(slot);
    }
    
    return invFrame; 
  }

  // NEEDS JAVADOCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
  private static HBox createHud() {
    HBox holder = new HBox(2);
    // parentPane.getChildren().add(holder); 
    holder.setId("hotbarHBox");

    Button pauseBut = new Button("█ █");
    pauseBut.setId("pauseButton");
    pauseBut.setOnAction(e -> onPauseButtonClicked());
    int pauseButSize = 80;
    setRegionAbsSize(pauseBut, pauseButSize, pauseButSize);

    Label timerLbl = new Label("--:--:--");
    timerLbl.setId("timerLbl");
    HBox.setMargin(timerLbl, new Insets(0, 0, 0, 150));

    Runnable timerLblUpdater = () -> timerLbl.setText( Timer.getInstance().getTimeRemainingFormatted() );
    Timer.getInstance().setDisplayRunnable(() -> {
      Platform.runLater( timerLblUpdater  );
    });

    HBox invFrame = makeInvFrame(pauseButSize);

    // holder.prefWidthProperty().bind(parentPane.widthProperty()); // give it the parent's width. if we dont do this, hotbar wont spread across screen
    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    holder.getChildren().addAll(
      pauseBut,
      timerLbl,
      spacer,
      invFrame
    );

    holder.setAlignment(Pos.CENTER_LEFT);
    holder.setLayoutY(10);

    return holder;
  }

  // NEEDS JAVADOCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
  public static void layerPage(AnchorPane root) {
      ChangeListener<Scene> listener = new ChangeListener<>() {
        @Override
        public void changed(ObservableValue<? extends Scene> obs, Scene oldScene, Scene newScene) {
            if (newScene != null) {
              if (root.lookup("#hudLayer") == null) {
                System.out.println("THis ran Babeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
                for (Node child : root.getChildren()) {
                  System.out.println(child.getId());
                }
                // run later to avoid reentrant modifications while the scene is still being attached
                Platform.runLater(() -> {
                  // This is hud initialization code
                  BorderPane hudLayer = new BorderPane();
                  hudLayer.setId("hudLayer");
                  hudLayer.setPickOnBounds(false);
                  
                  if (pauseMenuBoxHolder == null) {
                    pauseMenuBoxHolder = createPauseMenu();
                  }
                  
                  hudLayer.prefWidthProperty().bind(root.widthProperty());
                  hudLayer.prefHeightProperty().bind(root.heightProperty());
                  if (hudHolder == null) {
                    hudHolder = createHud();
                  }
                  
                  root.getChildren().add(hudLayer);
                  // listen to when root gets new children so we can make hudLayer keep going in front of them
                  root.getChildren().addListener((ListChangeListener<Node>) change -> {
                      while (change.next()) {
                          if (change.wasAdded() || change.wasRemoved()) {
                              // the weird :: thing schedules toFront instead of calling it right away, to avoid reentrancy during layout passes
                              Platform.runLater(hudLayer::toFront);
                          }
                      }
                  });
                  hudLayer.toFront();

                  hudLayer.setCenter(pauseMenuBoxHolder);
                  hudLayer.setTop(hudHolder);
                });
              } else {

                // if hudlayer already exists on the page, that means we've already loaded it before. all we gotta do is reparent the hud stuff
                BorderPane hudLayer = (BorderPane) root.lookup("#hudLayer");
                hudLayer.setCenter(pauseMenuBoxHolder);
                hudLayer.setTop(hudHolder);
              
              }
                
            }
        }
    };
    
    root.sceneProperty().addListener(listener);
        
  }


  public static void setupItemPickup(Button imageButton) {
    String itemName = imageButton.getId();
    
    if (itemName == null || Item.searchForItem(itemName) == null) {
      throw new Error("Item image button sucks: its id is invalid or doesn't exist. Set its css id to the item name it represents.");
    }

    imageButton.setOnAction(e -> {
      Item key = Item.loadItem(itemName);
      GameFacade.getInstance().getInventory().addItem(key);
      imageButton.setVisible(false);

      ImageView iv = (ImageView) imageButton.getGraphic();
      Image itemImg = (iv != null) ? iv.getImage() : null;

      imageButton.setOnAction(null); // make it only run once ever
    });
  }
}

