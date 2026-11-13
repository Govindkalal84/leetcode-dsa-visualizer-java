package com.leetcode.visualizer.core;

import java.util.*;

public class VisualEvent {
    public enum Type {
        HIGHLIGHT,
        COMPARE,
        SWAP,
        SET_VALUE,
        MOVE_POINTER,
        WINDOW_UPDATE,
        TREE_TRAVERSE,
        TREE_SWAP_CHILDREN,
        LINKED_LIST_POINTER,
        DP_CELL_COMPUTE,
        GRAPH_VISIT,
        LOG
    }

    private final Type type;
    private final String description;
    private final Map<String, Object> data = new HashMap<>();

    public VisualEvent(Type type, String description) {
        this.type = type;
        this.description = description;
    }

    public Type getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public VisualEvent addData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }
}
