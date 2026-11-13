package com.leetcode.visualizer.core;

import java.util.*;

/**
 * Captures a complete point-in-time snapshot of the algorithm state.
 */
public class FrameSnapshot {
    private final int frameIndex;
    private final String explanation;
    private final int codeLine;
    private final Map<String, String> variables = new LinkedHashMap<>();
    private final String componentType; // ARRAY, LINKED_LIST, TREE, GRAPH, DP_TABLE, STACK
    private final Map<String, Object> stateData = new LinkedHashMap<>();

    public FrameSnapshot(int frameIndex, String explanation, int codeLine, String componentType) {
        this.frameIndex = frameIndex;
        this.explanation = explanation;
        this.codeLine = codeLine;
        this.componentType = componentType;
    }

    public int getFrameIndex() {
        return frameIndex;
    }

    public String getExplanation() {
        return explanation;
    }

    public int getCodeLine() {
        return codeLine;
    }

    public String getComponentType() {
        return componentType;
    }

    public FrameSnapshot addVariable(String name, Object value) {
        this.variables.put(name, String.valueOf(value));
        return this;
    }

    public Map<String, String> getVariables() {
        return variables;
    }

    public FrameSnapshot addState(String key, Object value) {
        this.stateData.put(key, value);
        return this;
    }

    public Map<String, Object> getStateData() {
        return stateData;
    }
}
