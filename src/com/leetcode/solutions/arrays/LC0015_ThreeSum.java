package com.leetcode.solutions.arrays;

import com.leetcode.solutions.BaseSolution;
import com.leetcode.visualizer.core.Timeline;
import com.leetcode.visualizer.core.Visualizer;
import com.leetcode.visualizer.primitives.ArrayVisualizer;

public class LC0015_ThreeSum extends BaseSolution {
    @Override
    public int getProblemId() { return 15; }

    @Override
    public String getProblemTitle() { return "Three Sum"; }

    @Override
    public String getCategory() { return "Two Pointers"; }

    @Override
    public String getDifficulty() { return "Medium"; }

    @Override
    public String getCodeSource() {
        return "// TODO: Add solution algorithm code here\n" +
               "public void solve() {\n" +
               "    // implementation\n" +
               "}";
    }

    @Override
    public Timeline run() {
        Visualizer viz = new Visualizer(15, "Three Sum", getCodeSource());
        ArrayVisualizer arrayViz = new ArrayVisualizer(viz, new int[]{1, 2, 3, 4, 5});

        viz.step("Step 1: Initializing problem state for Three Sum", 1);
        // TODO: Wire up algorithmic steps, comparisons, and state transitions here
        viz.step("Completed solving Three Sum!", 3);

        return viz.getTimeline();
    }
}
