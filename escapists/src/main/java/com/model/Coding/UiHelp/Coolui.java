package com.model.Coding.UiHelp;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;

import com.escapists.App;
import com.model.Coding.Gameplay.GameFacade;
import com.model.Coding.Gameplay.Timer;
import com.model.Coding.Gameplay.InteractItems.Inventory;
import com.model.Coding.Gameplay.InteractItems.Item;
import com.model.Coding.Gameplay.InteractItems.Puzzle;
import com.model.Coding.Gameplay.Map.Room;
import com.model.Coding.Progress.Progress;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
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
 * The true UI class that handles creation for important ui stuff like HUD and pause menu
 * @author Jeffen 
 */

public class Coolui {
  private static StackPane pauseMenuBoxHolder;
  private static HBox hudHolder; 

  /**
   * Sets the absoliute size of a region thing
   * @param layoutThing thing we're resizing
   * @param sizeX
   * @param sizeY
   */
  public static void setRegionAbsSize(Region layoutThing /*all sizable nodes inherit from region*/, double sizeX, double sizeY) {
    layoutThing.setMinSize(sizeX, sizeY);
    layoutThing.setPrefSize(sizeX, sizeY);
    layoutThing.setMaxSize(sizeX, sizeY);
  }

  /**
   * Creates a button for the pause menu
   * @param text
   * @param parent should be the pause menu holder itself
   * @return the button made
   */
  private static Button makePauseMenuButton(String text, Pane parent /*all layouts inherit from pane*/) {
    Button but = new Button(text);
    but.setPrefSize(Double.MAX_VALUE, 70);
    parent.getChildren().add(but);
    return but;
  }

  /**
   * Callback for functionality when pause button is clicked
   */
  private static void onPauseButtonClicked() {
    VBox pauseMenuBox = (VBox) pauseMenuBoxHolder.lookup("#pauseMenuBox");
    Button pauseBut = (Button) hudHolder.lookup("#pauseButton");

    if (pauseMenuBox.isVisible()) {
      onResumeInput(pauseMenuBox, pauseBut);
    } else {
      onPauseInput(pauseMenuBox, pauseBut);
    }
  }

  /**
   * Functionality for pause input
   * @param pauseMenuBox
   * @param pauseButton
   */
  private static void onPauseInput(VBox pauseMenuBox, Button pauseButton) {
    pauseMenuBox.setVisible(true);
    pauseButton.getStyleClass().add("toggled");
    Timer.getInstance().pause();
  }

  /**
   * Functinoality for resume input
   * @param pauseMenuBox
   * @param pauseButton
   */
  private static void onResumeInput(VBox pauseMenuBox, Button pauseButton) {
    pauseMenuBox.setVisible(false);
    pauseButton.getStyleClass().remove("toggled");
    Timer.getInstance().resume();
    
  }

  /**
   * Clears out hud elements so they get recreated next screen load
   */
  public static void resetHud() {
    hudHolder = null;
    pauseMenuBoxHolder = null;
  }

  /**
   * Pause menu creation function
   * @return stackpane holding the pause menu
   */
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
    // Button loadBut = makePauseMenuButton("Load", menuBox);
    Button saveBut = makePauseMenuButton("Saves", menuBox);
    Button mainMenuBut = makePauseMenuButton("Main Menu", menuBox);
    
    resumeBut.setOnAction(e -> {
      Button pauseBut = (Button) hudHolder.lookup("#pauseButton");
      onResumeInput(menuBox, pauseBut);
    });

    saveBut.setOnAction(e -> {
      App.safeSetRoot("save");
    });
    // loadBut.setOnAction(e -> { App.safeSetRoot("loadsave"); });

    mainMenuBut.setOnAction(e -> {
      GameFacade.getInstance().endGame();
      App.safeSetRoot("mainMenu");
    });
    
    menuBox.setVisible(false);

    centerWrapper.getChildren().add(menuBox);
    centerWrapper.setPadding(new Insets(0, 0, 100, 0)); // shifts menu box a little up to compensate for lost space used by hotbar
    centerWrapper.setPickOnBounds(false);
    return centerWrapper;
  }

  private static ArrayDeque<StackPane> emptyInvSlots = new ArrayDeque<>();
  private static ArrayList<StackPane> filledInvSlots = new ArrayList<>();

  /**
   * creates an inventory slot
   * @param size
   * @return stack pane that is the square holding the item picture
   */
  private static StackPane makeInvSlot(double size) {
    StackPane iconHolder = new StackPane();
    iconHolder.getStyleClass().add("item-slot");
    ImageView imgView = new ImageView();
    setRegionAbsSize(iconHolder, size, size);
    imgView.setPreserveRatio(true);
    imgView.setSmooth(true);
    imgView.fitWidthProperty().bind(iconHolder.widthProperty());
    imgView.fitHeightProperty().bind(iconHolder.heightProperty());
    iconHolder.getChildren().add(imgView);

    return iconHolder;
  }

  /**
   * Fills inventory slot with item data/picture 
   * @param item
   */
  private static void fillInvSlot(Item item) {
    if (emptyInvSlots.size() <= 0)
      throw new Error(item.getName() + " can't be put in inv slot cause no more inventory slots available!!!");


    StackPane invSlot = emptyInvSlots.removeLast();

    // fill code
    invSlot.getProperties().put("ItemName", item.getName());
    ImageView imgView = (ImageView) invSlot.lookup("ImageView");
    imgView.setImage(new Image(item.getIconUrl()));

    // list updates
    filledInvSlots.add(invSlot);
  }


  /**
   * Removes inventory slot by removing its item icon
   * @param item item we're removing
   */
  private static void removeInvSlot(Item item) {
    for (StackPane slot : filledInvSlots) {
      String slotItemName = (String) slot.getProperties().get("ItemName");
      if (!item.getName().equalsIgnoreCase(slotItemName)) continue; 

      // slot clearance code
      slot.getProperties().put("ItemName", null);
      ImageView imgView = (ImageView) slot.lookup("ImageView");
      imgView.setImage(null);

      // list updates
      filledInvSlots.remove(slot);
      emptyInvSlots.addLast(slot); // adding last instead of adding first makes the emptyInvSlots work more like a stack than a queue since we also remove last

      return;
    }

    // code here only ever executes if we never find a slot to remove
    throw new Error("Invenotry never had a " + item.getName());


  }

  

  private static int MAX_ITEMS = 5;
  /**
   * Creates the entire inventory bar
   * @param heightSize height of the inventory bar. also determines its width (5*)
   * @return HBox composing of the inventory bar
   */
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
      emptyInvSlots.add(slot);
    }

    // getting inventory like this does assume that inventory and user 
    // and the game save has been initiated before the hud stuff is initiatied. maybe add checks later on to make sure this is the case
    GameFacade.getInstance().getInventory().setItemAddedCallback((item) -> { 
      Platform.runLater(() -> fillInvSlot(item));
    });

    GameFacade.getInstance().getInventory().setItemRemovedCallback((item) -> { 
      Platform.runLater(() -> removeInvSlot(item));
    });

    Inventory inv = GameFacade.getInstance().getInventory();
    if (inv != null && inv.getItems().size() > 0) {
      for (Item tem : inv.getItems()) {
        tem.loadItem(tem.getName(), null); // inventory doesnt automatically load these so we need this call
        Platform.runLater(() -> fillInvSlot(tem));
      }
    }
    
    return invFrame; 
  }

  /**
   * Hud creation
   * @return HBox that is holding the Hud
   */
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




  /**
   * Sets up the root of a page by layer a hud over it. only called for gameplay pages instead of menus
   * @param root the anchor pane of a gameplay page
   */
  public static void layerPage(AnchorPane root) {
      ChangeListener<Scene> listener = new ChangeListener<>() {
        @Override
        public void changed(ObservableValue<? extends Scene> obs, Scene oldScene, Scene newScene) {
            if (newScene != null) {
              if (root.lookup("#hudLayer") == null) {

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

  /**
   * this can be used for item buttons that have their icon images inside of them
   * @param imageButton item pickup button with an image view directly childed to it
   * @param itemName name of item associated
   */
  public static void setupItemPickup(Button imageButton, String itemName) {
      setupItemPickup(imageButton, itemName, null);
  }

  /**
   * Hooks functionality for a button that activates item pickup
   * @param itemButton button of that thing
   * @param itemName item name button represents
   * @param resourceUrl URL to the image of the item
   */
  public static void setupItemPickup(Button itemButton, String itemName, String resourceUrl) {
    itemButton.setOnAction(e -> {
      ImageView iv = (ImageView) itemButton.getGraphic();
      String itemImgUrl = (iv != null) ? iv.getImage().getUrl() : resourceUrl;

      Item key = Item.loadItem(itemName, itemImgUrl);
      GameFacade.getInstance().getInventory().addItem(key);
      itemButton.setVisible(false);


      itemButton.setOnAction(null); // make it only run once ever
    });
  }
}

