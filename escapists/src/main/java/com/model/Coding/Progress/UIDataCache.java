package com.model.Coding.Progress;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class UIDataCache {

    private static final UIDataCache instance = new UIDataCache();

    private UIDataCache() {}

    public static UIDataCache getInstance() {
        return instance;
    }

    private final Map<String, Map<String, Boolean>> uiVisibility = new HashMap<>();
    private final Map<String, Map<String, String>> uiText = new HashMap<>();
    private final Map<String, Map<String, Boolean>> uiDisabled = new HashMap<>();


    public void setUIVisible(String roomName, String elementId, boolean visible) {
        uiVisibility.putIfAbsent(roomName, new HashMap<>());
        uiVisibility.get(roomName).put(elementId, visible);
    }

    public boolean isUIVisible(String roomName, String elementId) {
        return uiVisibility.getOrDefault(roomName, Collections.emptyMap())
                .getOrDefault(elementId, false);
    }

    public void setUIText(String roomName, String elementId, String text) {
        uiText.putIfAbsent(roomName, new HashMap<>());
        uiText.get(roomName).put(elementId, text);
    }

    public String getUIText(String roomName, String elementId) {
        return uiText.getOrDefault(roomName, Collections.emptyMap())
                .getOrDefault(elementId, "");
    }

    public void setUIDisabled(String roomName, String elementId, boolean disabled) {
        uiDisabled.putIfAbsent(roomName, new HashMap<>());
        uiDisabled.get(roomName).put(elementId, disabled);
    }

    public boolean isUIDisabled(String roomName, String elementId) {
        return uiDisabled.getOrDefault(roomName, Collections.emptyMap())
                .getOrDefault(elementId, false); // default: false = enabled
    }
}
