package com.leetcode.visualizer.core;

import java.util.*;

/**
 * Main visualizer orchestrator that solutions interact with to record
 * their execution flow, variable state, and visual data structure snapshots.
 */
public class Visualizer {
    private final Timeline timeline;
    private final Map<String, Object> currentVariables = new LinkedHashMap<>();
    private String currentComponentType = "ARRAY";
    private final Map<String, Object> currentState = new LinkedHashMap<>();

    public Visualizer(int problemId, String problemTitle, String codeSource) {
        this.timeline = new Timeline(problemId, problemTitle, codeSource);
    }

    public void setComponentType(String type) {
        this.currentComponentType = type;
    }

    public void setVariable(String name, Object value) {
        currentVariables.put(name, value);
    }

    public void updateState(String key, Object value) {
        currentState.put(key, value);
    }

    /**
     * Records a keyframe snapshot with narration and active code line.
     */
    public void step(String explanation, int codeLine) {
        FrameSnapshot snapshot = new FrameSnapshot(
            timeline.totalFrames(),
            explanation,
            codeLine,
            currentComponentType
        );

        // Copy variables
        for (Map.Entry<String, Object> entry : currentVariables.entrySet()) {
            snapshot.addVariable(entry.getKey(), entry.getValue());
        }

        // Copy state data
        for (Map.Entry<String, Object> entry : currentState.entrySet()) {
            // Deep clone int[] arrays if present
            if (entry.getValue() instanceof int[]) {
                int[] orig = (int[]) entry.getValue();
                snapshot.addState(entry.getKey(), Arrays.copyOf(orig, orig.length));
            } else if (entry.getValue() instanceof Map) {
                snapshot.addState(entry.getKey(), new LinkedHashMap<>((Map<?, ?>) entry.getValue()));
            } else if (entry.getValue() instanceof List) {
                snapshot.addState(entry.getKey(), new ArrayList<>((List<?>) entry.getValue()));
            } else {
                snapshot.addState(entry.getKey(), entry.getValue());
            }
        }

        timeline.addSnapshot(snapshot);
    }

    public Timeline getTimeline() {
        return timeline;
    }
}
