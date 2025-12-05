package com.model.Coding.UiHelp;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Used to save the state of UI elements between switching of screens. Was used heavily in dev until better solution was found
 * @author Mason
 */
public class UIDataCache {

    private static final UIDataCache instance = new UIDataCache();

    private UIDataCache() {}

    /**
     * Returns instance of UIDC
     * @return UIDC
     */
    public static UIDataCache getInstance() {
        return instance;
    }

    private final Map<String, Map<String, Boolean>> uiVisibility = new HashMap<>();
    private final Map<String, Map<String, String>> uiText = new HashMap<>();
    private final Map<String, Map<String, Boolean>> uiDisabled = new HashMap<>();

    /**
     * Sets the visibility of UI elements
     * @param roomName name of room element is in
     * @param elementId name of the element
     * @param visible boolean whether it's visible or not
     */
    public void setUIVisible(String roomName, String elementId, boolean visible) {
        uiVisibility.putIfAbsent(roomName, new HashMap<>());
        uiVisibility.get(roomName).put(elementId, visible);
    }

    /**
     * Returns where the element is visible or not
     * @param roomName name of room element is in
     * @param elementId name of the element
     * @return True if visible, false if not
     */
    public boolean isUIVisible(String roomName, String elementId) {
        return uiVisibility.getOrDefault(roomName, Collections.emptyMap())
                .getOrDefault(elementId, false);
    }

    /**
     * Sets the text contents of the UI element
     * @param roomName name of room element is in
     * @param elementId name of the element
     * @param text text contents of the element
     */
    public void setUIText(String roomName, String elementId, String text) {
        uiText.putIfAbsent(roomName, new HashMap<>());
        uiText.get(roomName).put(elementId, text);
    }

    /**
     * Returns the text contents of UI element
     * @param roomName name of room element is in
     * @param elementId name of the UI element
     * @return String containing contents of the UI element
     */
    public String getUIText(String roomName, String elementId) {
        return uiText.getOrDefault(roomName, Collections.emptyMap())
                .getOrDefault(elementId, "");
    }

    /**
     * Sets whether to UI element is disabled or not
     * @param roomName name of the room element is in
     * @param elementId name of the UI element
     * @param disabled boolean for if it's disabled or not
     */
    public void setUIDisabled(String roomName, String elementId, boolean disabled) {
        uiDisabled.putIfAbsent(roomName, new HashMap<>());
        uiDisabled.get(roomName).put(elementId, disabled);
    }

    /**
     * Returns whether the element is disabled or not
     * @param roomName name of the room element is in
     * @param elementId name of the element
     * @return True if it's disabled, false if not.
     */
    public boolean isUIDisabled(String roomName, String elementId) {
        return uiDisabled.getOrDefault(roomName, Collections.emptyMap())
                .getOrDefault(elementId, false); // default: false = enabled
    }
}
