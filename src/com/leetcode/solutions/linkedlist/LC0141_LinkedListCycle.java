package com.leetcode.solutions.linkedlist;

import com.leetcode.solutions.BaseSolution;
import com.leetcode.visualizer.core.Timeline;
import com.leetcode.visualizer.core.Visualizer;
import com.leetcode.visualizer.primitives.LinkedListVisualizer;

public class LC0141_LinkedListCycle extends BaseSolution {
    @Override public int getProblemId() { return 141; }
    @Override public String getProblemTitle() { return "Linked List Cycle"; }
    @Override public String getCategory() { return "Linked List"; }
    @Override public String getDifficulty() { return "Easy"; }

    @Override
    public String getCodeSource() {
        return "public boolean hasCycle(ListNode head) {\n" +
               "    if (head == null || head.next == null) return false;\n" +
               "    ListNode slow = head;\n" +
               "    ListNode fast = head.next;\n" +
               "    while (slow != fast) {\n" +
               "        if (fast == null || fast.next == null) return false;\n" +
               "        slow = slow.next;\n" +
               "        fast = fast.next.next;\n" +
               "    }\n" +
               "    return true;\n" +
               "}";
    }

    @Override
    public Timeline run() {
        Visualizer viz = new Visualizer(141, "Linked List Cycle", getCodeSource());
        LinkedListVisualizer llViz = new LinkedListVisualizer(viz);

        // Build list: 3 -> 2 -> 0 -> -4 -> (cycles back to 2)
        int n3 = llViz.addNode(-4, -1); // will link to 2
        int n2 = llViz.addNode(0, n3);
        int n1 = llViz.addNode(2, n2);
        int n0 = llViz.addNode(3, n1);
        llViz.setHead(n0);
        llViz.pointNext(n3, n1); // cycle: -4 -> 2

        int[] nodeSeq = {n0, n1, n2, n3, n1, n2, n3, n1}; // cyclical sequence

        int slowIdx = 0;
        int fastIdx = 1;
        llViz.setPointer("slow", nodeSeq[slowIdx]);
        llViz.setPointer("fast", nodeSeq[fastIdx]);
        viz.setVariable("slow", "Node(3)");
        viz.setVariable("fast", "Node(2)");
        viz.step("Initialize slow at head (3) and fast at head.next (2). Floyd's Tortoise and Hare algorithm.", 4);

        int step = 1;
        while (slowIdx != fastIdx && step < 10) {
            slowIdx = (slowIdx + 1) % nodeSeq.length;
            fastIdx = (fastIdx + 2) % nodeSeq.length;

            llViz.setPointer("slow", nodeSeq[slowIdx]);
            llViz.setPointer("fast", nodeSeq[fastIdx]);

            viz.setVariable("slowStep", slowIdx);
            viz.setVariable("fastStep", fastIdx);
            viz.step("Slow moves 1 step. Fast moves 2 steps. slow at Node ID " + nodeSeq[slowIdx] 
                + ", fast at Node ID " + nodeSeq[fastIdx] + ".", 7);

            if (slowIdx == fastIdx) {
                viz.step("🎯 Fast pointer caught up with slow pointer at the same node! Cycle detected (returns true).", 10);
                return viz.getTimeline();
            }
            step++;
        }

        viz.step("Cycle detected! slow and fast met.", 10);
        return viz.getTimeline();
    }
}
