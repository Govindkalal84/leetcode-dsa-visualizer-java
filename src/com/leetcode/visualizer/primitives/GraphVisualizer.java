package com.leetcode.visualizer.primitives;

import com.leetcode.visualizer.core.Visualizer;
import java.util.*;

/**
 * Reusable visual primitive for Graph algorithms (BFS, DFS, Dijkstra, TopoSort)
 * and 2D Grid Graphs (e.g. Number of Islands).
 */
public class GraphVisualizer {
    public static class GraphNode {
        public String id;
        public String label;
        public String state; // UNVISITED, VISITING, VISITED

        public GraphNode(String id, String label, String state) {
            this.id = id;
            this.label = label;
            this.state = state;
        }

        public Map<String, Object> toMap() {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", id);
            m.put("label", label);
            m.put("state", state);
            return m;
        }
    }

    private final Visualizer visualizer;
    private final Map<String, GraphNode> nodes = new LinkedHashMap<>();
    private final List<Map<String, String>> edges = new ArrayList<>();
    private final List<String> queueOrStack = new ArrayList<>();
    private String activeNodeId = null;

    public GraphVisualizer(Visualizer visualizer) {
        this.visualizer = visualizer;
        this.visualizer.setComponentType("GRAPH");
        sync();
    }

    public void addNode(String id, String label) {
        nodes.put(id, new GraphNode(id, label, "UNVISITED"));
        sync();
    }

    public void addEdge(String from, String to) {
        Map<String, String> edge = new LinkedHashMap<>();
        edge.put("from", from);
        edge.put("to", to);
        edges.add(edge);
        sync();
    }

    public void setNodeState(String id, String state) {
        if (nodes.containsKey(id)) {
            nodes.get(id).state = state;
            sync();
        }
    }

    public void setActiveNode(String id) {
        this.activeNodeId = id;
        sync();
    }

    public void pushQueueOrStack(String item) {
        queueOrStack.add(item);
        sync();
    }

    public String popQueueOrStack() {
        if (!queueOrStack.isEmpty()) {
            String item = queueOrStack.remove(0);
            sync();
            return item;
        }
        return null;
    }

    private void sync() {
        List<Map<String, Object>> nodeList = new ArrayList<>();
        for (GraphNode gn : nodes.values()) {
            nodeList.add(gn.toMap());
        }
        visualizer.updateState("nodes", nodeList);
        visualizer.updateState("edges", edges);
        visualizer.updateState("queueOrStack", new ArrayList<>(queueOrStack));
        visualizer.updateState("activeNodeId", activeNodeId);
    }
}
