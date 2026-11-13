package com.leetcode.solutions.linkedlist;

import com.leetcode.solutions.BaseSolution;
import com.leetcode.visualizer.core.Timeline;
import com.leetcode.visualizer.core.Visualizer;
import com.leetcode.visualizer.primitives.LinkedListVisualizer;

public class LC0206_ReverseLinkedList extends BaseSolution {
    @Override
    public int getProblemId() { return 206; }

    @Override
    public String getProblemTitle() { return "Reverse Linked List"; }

    @Override
    public String getCategory() { return "Linked List"; }

    @Override
    public String getDifficulty() { return "Easy"; }

    @Override
    public String getCodeSource() {
        return "public ListNode reverseList(ListNode head) {\n" +
               "    ListNode prev = null;\n" +
               "    ListNode curr = head;\n" +
               "    while (curr != null) {\n" +
               "        ListNode next = curr.next;\n" +
               "        curr.next = prev;\n" +
               "        prev = curr;\n" +
               "        curr = next;\n" +
               "    }\n" +
               "    return prev;\n" +
               "}";
    }

    @Override
    public Timeline run() {
        Visualizer viz = new Visualizer(206, "Reverse Linked List", getCodeSource());
        LinkedListVisualizer llViz = new LinkedListVisualizer(viz);

        // Build list: 1 -> 2 -> 3 -> 4 -> 5 -> null
        int n5 = llViz.addNode(5, -1);
        int n4 = llViz.addNode(4, n5);
        int n3 = llViz.addNode(3, n4);
        int n2 = llViz.addNode(2, n3);
        int n1 = llViz.addNode(1, n2);
        llViz.setHead(n1);

        llViz.setPointer("curr", n1);
        viz.setVariable("prev", "null");
        viz.setVariable("curr", "Node(1)");
        viz.step("Initialize: prev = null, curr = head (Node 1).", 3);

        int prevId = -1;
        int currId = n1;
        int[] originalNexts = {n2, n3, n4, n5, -1};

        int stepCounter = 0;
        while (currId != -1 && stepCounter < 5) {
            int nextId = originalNexts[stepCounter];
            if (nextId != -1) {
                llViz.setPointer("next", nextId);
                viz.setVariable("next", "Node(" + (stepCounter + 2) + ")");
            } else {
                llViz.removePointer("next");
                viz.setVariable("next", "null");
            }
            viz.step("Save next pointer: next = curr.next.", 5);

            // Reverse the pointer
            llViz.pointNext(currId, prevId);
            viz.step("Reverse direction: curr.next = prev (now points backwards!).", 6);

            // Advance prev and curr
            prevId = currId;
            currId = nextId;

            llViz.setPointer("prev", prevId);
            if (currId != -1) {
                llViz.setPointer("curr", currId);
            } else {
                llViz.removePointer("curr");
            }

            viz.setVariable("prev", "Node(" + (stepCounter + 1) + ")");
            viz.setVariable("curr", currId != -1 ? "Node(" + (stepCounter + 2) + ")" : "null");
            viz.step("Advance: prev = curr, curr = next.", 8);

            stepCounter++;
        }

        llViz.setHead(prevId);
        viz.step("Reversal complete! The new head is prev (Node 5).", 10);
        return viz.getTimeline();
    }
}
