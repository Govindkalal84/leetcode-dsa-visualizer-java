package com.leetcode.solutions.arrays;

import com.leetcode.solutions.BaseSolution;
import com.leetcode.visualizer.core.Timeline;
import com.leetcode.visualizer.core.Visualizer;
import com.leetcode.visualizer.primitives.ArrayVisualizer;

public class LC0704_BinarySearch extends BaseSolution {
    @Override public int getProblemId() { return 704; }
    @Override public String getProblemTitle() { return "Binary Search"; }
    @Override public String getCategory() { return "Binary Search"; }
    @Override public String getDifficulty() { return "Easy"; }

    @Override
    public String getCodeSource() {
        return "public int search(int[] nums, int target) {\n" +
               "    int low = 0, high = nums.length - 1;\n" +
               "    while (low <= high) {\n" +
               "        int mid = low + (high - low) / 2;\n" +
               "        if (nums[mid] == target) return mid;\n" +
               "        else if (nums[mid] < target) low = mid + 1;\n" +
               "        else high = mid - 1;\n" +
               "    }\n" +
               "    return -1;\n" +
               "}";
    }

    @Override
    public Timeline run() {
        int[] nums = {-1, 0, 3, 5, 9, 12};
        int target = 9;

        Visualizer viz = new Visualizer(704, "Binary Search", getCodeSource());
        ArrayVisualizer arrayViz = new ArrayVisualizer(viz, nums);

        int low = 0, high = nums.length - 1;
        arrayViz.setPointer("low", low);
        arrayViz.setPointer("high", high);
        arrayViz.setWindow(low, high);
        viz.setVariable("target", target);
        viz.setVariable("low", low);
        viz.setVariable("high", high);

        viz.step("Initialize search range: low = 0, high = " + high + " for target = " + target + ".", 2);

        while (low <= high) {
            int mid = low + (high - low) / 2;
            arrayViz.setPointer("mid", mid);
            arrayViz.clearHighlights();
            arrayViz.highlight(mid, "active");

            viz.setVariable("mid", mid);
            viz.setVariable("nums[mid]", nums[mid]);
            viz.step("Compute mid = " + low + " + (" + high + " - " + low + ") / 2 = " + mid + ". Value at mid is " + nums[mid] + ".", 4);

            if (nums[mid] == target) {
                arrayViz.highlight(mid, "match");
                viz.step("🎯 Target " + target + " found at index " + mid + "!", 5);
                return viz.getTimeline();
            } else if (nums[mid] < target) {
                viz.step("nums[mid] (" + nums[mid] + ") < target (" + target + "). Search right half: low = mid + 1.", 6);
                low = mid + 1;
            } else {
                viz.step("nums[mid] (" + nums[mid] + ") > target (" + target + "). Search left half: high = mid - 1.", 7);
                high = mid - 1;
            }

            if (low <= high) {
                arrayViz.setPointer("low", low);
                arrayViz.setPointer("high", high);
                arrayViz.setWindow(low, high);
            } else {
                arrayViz.removePointer("low");
                arrayViz.removePointer("high");
                arrayViz.clearWindow();
            }
            viz.setVariable("low", low);
            viz.setVariable("high", high);
        }

        viz.step("Target not found in array. Return -1.", 9);
        return viz.getTimeline();
    }
}
