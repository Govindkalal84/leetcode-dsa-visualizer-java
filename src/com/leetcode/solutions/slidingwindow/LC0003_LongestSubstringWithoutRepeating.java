package com.leetcode.solutions.slidingwindow;

import com.leetcode.solutions.BaseSolution;
import com.leetcode.visualizer.core.Timeline;
import com.leetcode.visualizer.core.Visualizer;
import com.leetcode.visualizer.primitives.ArrayVisualizer;

import java.util.HashMap;
import java.util.Map;

public class LC0003_LongestSubstringWithoutRepeating extends BaseSolution {
    @Override
    public int getProblemId() { return 3; }

    @Override
    public String getProblemTitle() { return "Longest Substring Without Repeating Characters"; }

    @Override
    public String getCategory() { return "Sliding Window"; }

    @Override
    public String getDifficulty() { return "Medium"; }

    @Override
    public String getCodeSource() {
        return "public int lengthOfLongestSubstring(String s) {\n" +
               "    Map<Character, Integer> map = new HashMap<>();\n" +
               "    int maxLen = 0, left = 0;\n" +
               "    for (int right = 0; right < s.length(); right++) {\n" +
               "        char c = s.charAt(right);\n" +
               "        if (map.containsKey(c)) {\n" +
               "            left = Math.max(left, map.get(c) + 1);\n" +
               "        }\n" +
               "        map.put(c, right);\n" +
               "        maxLen = Math.max(maxLen, right - left + 1);\n" +
               "    }\n" +
               "    return maxLen;\n" +
               "}";
    }

    @Override
    public Timeline run() {
        String s = "pwwkew";
        // Convert chars to ASCII int array for visualizer
        int[] chars = new int[s.length()];
        for (int i = 0; i < s.length(); i++) chars[i] = s.charAt(i);

        Visualizer viz = new Visualizer(3, "Longest Substring Without Repeating Characters", getCodeSource());
        ArrayVisualizer arrayViz = new ArrayVisualizer(viz, chars);

        int maxLen = 0;
        int left = 0;
        Map<Character, Integer> map = new HashMap<>();

        arrayViz.setPointer("L", 0);
        arrayViz.setPointer("R", 0);
        arrayViz.setWindow(0, 0);
        viz.setVariable("s", s);
        viz.setVariable("left", 0);
        viz.setVariable("right", 0);
        viz.setVariable("maxLen", 0);

        viz.step("Initialize sliding window on string \"" + s + "\" with L=0, R=0.", 3);

        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            arrayViz.setPointer("R", right);
            arrayViz.clearHighlights();
            arrayViz.highlight(right, "active");

            viz.setVariable("currentChar", String.valueOf(c));
            viz.setVariable("right", right);

            if (map.containsKey(c) && map.get(c) >= left) {
                int prevPos = map.get(c);
                arrayViz.highlight(prevPos, "duplicate");
                left = prevPos + 1;
                arrayViz.setPointer("L", left);
                viz.setVariable("left", left);
                viz.step("Character '" + c + "' already seen at index " + prevPos + "! Shrink window: advance L to " + left + ".", 6);
            }

            map.put(c, right);
            arrayViz.setWindow(left, right);
            int currentLen = right - left + 1;
            boolean newMax = currentLen > maxLen;
            maxLen = Math.max(maxLen, currentLen);

            viz.setVariable("window", s.substring(left, right + 1));
            viz.setVariable("maxLen", maxLen);
            viz.setVariable("map", map.toString());

            viz.step("Window is [\"" + s.substring(left, right + 1) + "\"], length = " + currentLen 
                + (newMax ? " 🚀 (NEW MAX!)" : ""), 9);
        }

        viz.step("Finished iterating string. Longest substring length without repeats is: " + maxLen, 12);
        return viz.getTimeline();
    }
}
