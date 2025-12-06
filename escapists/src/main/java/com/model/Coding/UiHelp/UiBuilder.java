package com.model.Coding.UiHelp;

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
 * Class for UI building stuff that must be called from App class. 
 * Coolui cant have this stuff since that would cause circular dependency
 * @author Jeffen
 */
public class UiBuilder {
  /**
   * Converts a root anchor pane to a scrollable thing by wrapping it under a scroll pane
   * That scroll pane becomes the new root of pages
   * @param root old root
   * @return new root
   */
  public static Parent convertRootToScrollable(AnchorPane root) {
    Group group = new Group();

    ScrollPane scroll = new ScrollPane(group);
    scroll.setPannable(true);
    scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

    ImageView bgIv = (ImageView) root.lookup("#bgImage");
    double baseHeight = bgIv.getFitHeight(), baseWidth = bgIv.getFitHeight();

    // add content to the group (important: group preserves transformed bounds)
    Scene scene = root.getScene();
    group.getChildren().setAll(root);

    scroll.setFitToHeight(false);
    scroll.setFitToWidth(false);

    // Listen for changes to the viewport — recompute uniform scale so content height fits
    ChangeListener<Bounds> viewportListener = (obs, oldBounds, newBounds) -> {
        if (newBounds == null) return;

        double viewportHeight = newBounds.getHeight();
        double viewportWidth  = newBounds.getWidth();

        if (viewportHeight <= 0 || baseHeight <= 0) return;

        // scale so the content's HEIGHT fits the viewport
        double scale = viewportHeight / baseHeight;

        // Apply uniform scale to preserve aspect ratio
        root.setScaleX(scale);
        root.setScaleY(scale);


        // We ensured vbarPolicy=NEVER (or fit height), so no vertical scroll should appear.
        // Horizontal scrollbar will appear when scaledWidth > viewportWidth.
    };

    // Add listener to viewportBoundsProperty (fires when ScrollPane size or viewport changes)
    scroll.viewportBoundsProperty().addListener((obs, oldB, newB) -> viewportListener.changed(obs, oldB, newB));

    // Also run once in case the viewport is already ready
    if (scroll.getViewportBounds() != null) {
      viewportListener.changed(null, null, scroll.getViewportBounds());
    }

    // scene.setRoot(scroll);
    return scroll;
  } 
}
