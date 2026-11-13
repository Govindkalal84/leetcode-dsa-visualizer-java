package com.leetcode.visualizer.primitives;

import com.leetcode.visualizer.core.Visualizer;
import java.util.*;

/**
 * Reusable visual primitive for Binary Trees, Binary Search Trees,
 * Traversals (Inorder, Preorder, Postorder, BFS Level Order) and Mutations.
 */
public class TreeVisualizer {
    public static class TreeNodeData {
        public int id;
        public int val;
        public int leftId = -1;
        public int rightId = -1;

        public TreeNodeData(int id, int val) {
            this.id = id;
            this.val = val;
        }

        public Map<String, Object> toMap() {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", id);
            m.put("val", val);
            m.put("leftId", leftId);
            m.put("rightId", rightId);
            return m;
        }
    }

    private final Visualizer visualizer;
    private final Map<Integer, TreeNodeData> nodes = new LinkedHashMap<>();
    private int rootId = -1;
    private int activeNodeId = -1;
    private final Set<Integer> visitedNodes = new LinkedHashSet<>();

    public TreeVisualizer(Visualizer visualizer) {
        this.visualizer = visualizer;
        this.visualizer.setComponentType("TREE");
    }

    public int createNode(int val) {
        int id = nodes.size();
        nodes.put(id, new TreeNodeData(id, val));
        if (rootId == -1) rootId = id;
        sync();
        return id;
    }

    public void setChildren(int parentId, int leftId, int rightId) {
        TreeNodeData p = nodes.get(parentId);
        if (p != null) {
            p.leftId = leftId;
            p.rightId = rightId;
            sync();
        }
    }

    public void swapChildren(int parentId) {
        TreeNodeData p = nodes.get(parentId);
        if (p != null) {
            int tmp = p.leftId;
            p.leftId = p.rightId;
            p.rightId = tmp;
            sync();
        }
    }

    public void setActiveNode(int nodeId) {
        this.activeNodeId = nodeId;
        if (nodeId != -1) visitedNodes.add(nodeId);
        sync();
    }

    public void clearActive() {
        this.activeNodeId = -1;
        sync();
    }

    public int getRootId() {
        return rootId;
    }

    public TreeNodeData getNode(int id) {
        return nodes.get(id);
    }

    private void sync() {
        List<Map<String, Object>> nodeList = new ArrayList<>();
        for (TreeNodeData n : nodes.values()) {
            nodeList.add(n.toMap());
        }
        visualizer.updateState("nodes", nodeList);
        visualizer.updateState("rootId", rootId);
        visualizer.updateState("activeNodeId", activeNodeId);
        visualizer.updateState("visitedNodes", new ArrayList<>(visitedNodes));
    }
}
