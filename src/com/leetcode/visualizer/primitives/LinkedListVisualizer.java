package com.leetcode.visualizer.primitives;

import com.leetcode.visualizer.core.Visualizer;
import java.util.*;

/**
 * Reusable visual primitive for Singly & Doubly Linked Lists.
 */
public class LinkedListVisualizer {
    public static class Node {
        public int id;
        public int val;
        public int nextId; // -1 for null

        public Node(int id, int val, int nextId) {
            this.id = id;
            this.val = val;
            this.nextId = nextId;
        }

        public Map<String, Object> toMap() {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", id);
            m.put("val", val);
            m.put("nextId", nextId);
            return m;
        }
    }

    private final Visualizer visualizer;
    private final Map<Integer, Node> nodes = new LinkedHashMap<>();
    private final Map<String, Integer> pointers = new LinkedHashMap<>();
    private int headId = -1;

    public LinkedListVisualizer(Visualizer visualizer) {
        this.visualizer = visualizer;
        this.visualizer.setComponentType("LINKED_LIST");
    }

    public int addNode(int val, int nextId) {
        int id = nodes.size();
        nodes.put(id, new Node(id, val, nextId));
        if (headId == -1) headId = id;
        sync();
        return id;
    }

    public void setHead(int id) {
        this.headId = id;
        sync();
    }

    public void pointNext(int fromId, int toId) {
        if (nodes.containsKey(fromId)) {
            nodes.get(fromId).nextId = toId;
            sync();
        }
    }

    public void setPointer(String name, int nodeId) {
        pointers.put(name, nodeId);
        sync();
    }

    public void removePointer(String name) {
        pointers.remove(name);
        sync();
    }

    private void sync() {
        List<Map<String, Object>> nodeList = new ArrayList<>();
        for (Node n : nodes.values()) {
            nodeList.add(n.toMap());
        }
        visualizer.updateState("nodes", nodeList);
        visualizer.updateState("headId", headId);
        visualizer.updateState("pointers", new LinkedHashMap<>(pointers));
    }
}
