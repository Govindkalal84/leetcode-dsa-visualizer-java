package com.leetcode.solutions.arrays;

import com.leetcode.solutions.BaseSolution;
import com.leetcode.visualizer.core.Timeline;
import com.leetcode.visualizer.core.Visualizer;
import com.leetcode.visualizer.primitives.ArrayVisualizer;

import java.util.HashMap;
import java.util.Map;

public class LC0001_TwoSum extends BaseSolution {
    @Override
    public int getProblemId() { return 1; }

    @Override
    public String getProblemTitle() { return "Two Sum"; }

    @Override
    public String getCategory() { return "Arrays & Hash Table"; }

    @Override
    public String getDifficulty() { return "Easy"; }

    @Override
    public String getCodeSource() {
        return "public int[] twoSum(int[] nums, int target) {\n" +
               "    Map<Integer, Integer> map = new HashMap<>();\n" +
               "    for (int i = 0; i < nums.length; i++) {\n" +
               "        int complement = target - nums[i];\n" +
               "        if (map.containsKey(complement)) {\n" +
               "            return new int[] { map.get(complement), i };\n" +
               "        }\n" +
               "        map.put(nums[i], i);\n" +
               "    }\n" +
               "    return new int[]{};\n" +
               "}";
    }

    @Override
    public Timeline run() {
        int[] nums = {2, 7, 11, 15};
        int target = 9;

        Visualizer viz = new Visualizer(1, "Two Sum", getCodeSource());
        ArrayVisualizer arrayViz = new ArrayVisualizer(viz, nums);

        viz.setVariable("target", target);
        viz.step("Initialize problem with array [2, 7, 11, 15] and target = 9. Create empty hash map.", 2);

        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            arrayViz.clearHighlights();
            arrayViz.setPointer("i", i);
            arrayViz.highlight(i, "active");
            int val = nums[i];
            int complement = target - val;

            viz.setVariable("i", i);
            viz.setVariable("nums[i]", val);
            viz.setVariable("complement", complement);
            viz.setVariable("map", map.toString());

            viz.step("Examining index " + i + " (val=" + val + "). Looking for complement = 9 - " + val + " = " + complement + " in map.", 4);

            if (map.containsKey(complement)) {
                int prevIdx = map.get(complement);
                arrayViz.highlight(prevIdx, "match");
                arrayViz.highlight(i, "match");
                arrayViz.setPointer("found", prevIdx);

                viz.setVariable("foundIndices", "[" + prevIdx + ", " + i + "]");
                viz.step("🎯 Complement " + complement + " found in map at index " + prevIdx + "! Solution: [" + prevIdx + ", " + i + "].", 6);
                return viz.getTimeline();
            }

            map.put(val, i);
            viz.setVariable("map", map.toString());
            viz.step("Complement " + complement + " not yet in map. Adding (" + val + " -> " + i + ") to map.", 8);
        }

        viz.step("No two sum solution found.", 10);
        return viz.getTimeline();
    }
}
