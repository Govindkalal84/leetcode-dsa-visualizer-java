package com.leetcode.solutions.arrays;

import com.leetcode.solutions.BaseSolution;
import com.leetcode.visualizer.core.Timeline;
import com.leetcode.visualizer.core.Visualizer;
import com.leetcode.visualizer.primitives.ArrayVisualizer;

public class LC0011_ContainerWithMostWater extends BaseSolution {
    @Override
    public int getProblemId() { return 11; }

    @Override
    public String getProblemTitle() { return "Container With Most Water"; }

    @Override
    public String getCategory() { return "Two Pointers"; }

    @Override
    public String getDifficulty() { return "Medium"; }

    @Override
    public String getCodeSource() {
        return "public int maxArea(int[] height) {\n" +
               "    int left = 0, right = height.length - 1;\n" +
               "    int maxArea = 0;\n" +
               "    while (left < right) {\n" +
               "        int h = Math.min(height[left], height[right]);\n" +
               "        int w = right - left;\n" +
               "        maxArea = Math.max(maxArea, h * w);\n" +
               "        if (height[left] < height[right]) {\n" +
               "            left++;\n" +
               "        } else {\n" +
               "            right--;\n" +
               "        }\n" +
               "    }\n" +
               "    return maxArea;\n" +
               "}";
    }

    @Override
    public Timeline run() {
        int[] height = {1, 8, 6, 2, 5, 4, 8, 3, 7};

        Visualizer viz = new Visualizer(11, "Container With Most Water", getCodeSource());
        ArrayVisualizer arrayViz = new ArrayVisualizer(viz, height);

        int left = 0;
        int right = height.length - 1;
        int maxArea = 0;

        arrayViz.setPointer("L", left);
        arrayViz.setPointer("R", right);
        viz.setVariable("left", left);
        viz.setVariable("right", right);
        viz.setVariable("maxArea", maxArea);
        viz.step("Initialize two pointers: L at start (0) and R at end (" + right + ").", 2);

        while (left < right) {
            arrayViz.clearHighlights();
            arrayViz.highlight(left, "pointer");
            arrayViz.highlight(right, "pointer");

            int h = Math.min(height[left], height[right]);
            int w = right - left;
            int currentArea = h * w;
            boolean newMax = currentArea > maxArea;
            if (newMax) {
                maxArea = currentArea;
            }

            viz.setVariable("L_val", height[left]);
            viz.setVariable("R_val", height[right]);
            viz.setVariable("width", w);
            viz.setVariable("height", h);
            viz.setVariable("area", currentArea);
            viz.setVariable("maxArea", maxArea);

            viz.step("Current window: width = " + w + ", height = min(" + height[left] + ", " + height[right] 
                + ") = " + h + ". Area = " + currentArea + (newMax ? " 🌟 (NEW MAX!)" : ""), 7);

            if (height[left] < height[right]) {
                viz.step("Left bar (" + height[left] + ") is shorter than right bar (" + height[right] + "). Advance L pointer to find a taller line.", 9);
                left++;
                arrayViz.setPointer("L", left);
            } else {
                viz.step("Right bar (" + height[right] + ") is shorter or equal to left (" + height[left] + "). Decrement R pointer.", 11);
                right--;
                arrayViz.setPointer("R", right);
            }
            viz.setVariable("left", left);
            viz.setVariable("right", right);
        }

        viz.step("Pointers met at index " + left + ". Maximum water container area found: " + maxArea, 14);
        return viz.getTimeline();
    }
}
