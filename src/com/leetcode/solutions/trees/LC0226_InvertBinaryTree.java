package com.leetcode.solutions.trees;

import com.leetcode.solutions.BaseSolution;
import com.leetcode.visualizer.core.Timeline;
import com.leetcode.visualizer.core.Visualizer;
import com.leetcode.visualizer.primitives.TreeVisualizer;

public class LC0226_InvertBinaryTree extends BaseSolution {
    @Override
    public int getProblemId() { return 226; }

    @Override
    public String getProblemTitle() { return "Invert Binary Tree"; }

    @Override
    public String getCategory() { return "Binary Tree"; }

    @Override
    public String getDifficulty() { return "Easy"; }

    @Override
    public String getCodeSource() {
        return "public TreeNode invertTree(TreeNode root) {\n" +
               "    if (root == null) return null;\n" +
               "    TreeNode temp = root.left;\n" +
               "    root.left = invertTree(root.right);\n" +
               "    root.right = invertTree(temp);\n" +
               "    return root;\n" +
               "}";
    }

    @Override
    public Timeline run() {
        Visualizer viz = new Visualizer(226, "Invert Binary Tree", getCodeSource());
        TreeVisualizer treeViz = new TreeVisualizer(viz);

        // Build tree:
        //       4
        //     /   \
        //    2     7
        //   / \   / \
        //  1   3 6   9
        int n4 = treeViz.createNode(4);
        int n2 = treeViz.createNode(2);
        int n7 = treeViz.createNode(7);
        int n1 = treeViz.createNode(1);
        int n3 = treeViz.createNode(3);
        int n6 = treeViz.createNode(6);
        int n9 = treeViz.createNode(9);

        treeViz.setChildren(n4, n2, n7);
        treeViz.setChildren(n2, n1, n3);
        treeViz.setChildren(n7, n6, n9);

        viz.step("Initial tree structure with root = 4. Goal: swap all left and right subtrees recursively.", 1);

        // Recursive traversal simulation with visualizer
        invert(treeViz, viz, n4, 4);

        treeViz.clearActive();
        viz.step("Inversion complete! Tree is fully mirrored.", 6);
        return viz.getTimeline();
    }

    private void invert(TreeVisualizer treeViz, Visualizer viz, int nodeId, int val) {
        if (nodeId == -1) return;

        treeViz.setActiveNode(nodeId);
        viz.setVariable("currentNode", "Node(" + val + ")");
        viz.step("Visiting Node " + val + ". Preparing to swap its left and right children.", 2);

        treeViz.swapChildren(nodeId);
        viz.step("Swapped left and right children of Node " + val + "!", 4);

        TreeVisualizer.TreeNodeData node = treeViz.getNode(nodeId);
        int left = node.leftId;
        int right = node.rightId;

        if (left != -1) {
            invert(treeViz, viz, left, treeViz.getNode(left).val);
        }
        if (right != -1) {
            invert(treeViz, viz, right, treeViz.getNode(right).val);
        }
    }
}
