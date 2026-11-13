package com.leetcode.solutions.strings;

import com.leetcode.solutions.BaseSolution;
import com.leetcode.visualizer.core.Timeline;
import com.leetcode.visualizer.core.Visualizer;
import com.leetcode.visualizer.primitives.ArrayVisualizer;

import java.util.Arrays;

public class LC0242_ValidAnagram extends BaseSolution {
    @Override
    public int getProblemId() { return 242; }

    @Override
    public String getProblemTitle() { return "Valid Anagram"; }

    @Override
    public String getCategory() { return "Strings & Sorting"; }

    @Override
    public String getDifficulty() { return "Easy"; }

    @Override
    public String getCodeSource() {
        return "public boolean isAnagram(String s, String t) {\n" +
               "    s = s.replaceAll(\"\\\\s+\", \"\").toLowerCase();\n" +
               "    t = t.replaceAll(\"\\\\s+\", \"\").toLowerCase();\n" +
               "    if (s.length() != t.length()) return false;\n" +
               "    char[] a = s.toCharArray();\n" +
               "    char[] b = t.toCharArray();\n" +
               "    Arrays.sort(a);\n" +
               "    Arrays.sort(b);\n" +
               "    return Arrays.equals(a, b);\n" +
               "}";
    }

    @Override
    public Timeline run() {
        String str1 = "listen";
        String str2 = "silent";

        Visualizer viz = new Visualizer(242, "Valid Anagram", getCodeSource());

        viz.setVariable("str1", str1);
        viz.setVariable("str2", str2);
        viz.step("Initialize input strings: str1 = \"" + str1 + "\", str2 = \"" + str2 + "\"", 1);

        // Normalize
        str1 = str1.replaceAll("\\s+", "").toLowerCase();
        str2 = str2.replaceAll("\\s+", "").toLowerCase();
        viz.step("Normalize strings (remove spaces and convert to lowercase)", 2);

        if (str1.length() != str2.length()) {
            viz.step("Length mismatch (" + str1.length() + " != " + str2.length() + ") -> Not an Anagram", 4);
            return viz.getTimeline();
        }

        char[] a = str1.toCharArray();
        char[] b = str2.toCharArray();

        int[] arrA = new int[a.length];
        for (int i = 0; i < a.length; i++) arrA[i] = a[i];

        ArrayVisualizer arrayViz = new ArrayVisualizer(viz, arrA);
        viz.setVariable("char[] a", Arrays.toString(a));
        viz.setVariable("char[] b", Arrays.toString(b));
        viz.step("Convert strings to character arrays: a = " + Arrays.toString(a) + ", b = " + Arrays.toString(b), 5);

        Arrays.sort(a);
        Arrays.sort(b);

        for (int i = 0; i < a.length; i++) arrA[i] = a[i];
        for (int i = 0; i < a.length; i++) arrayViz.highlight(i, "sorted");

        viz.setVariable("sorted a", Arrays.toString(a));
        viz.setVariable("sorted b", Arrays.toString(b));
        viz.step("Sort both character arrays: a = " + Arrays.toString(a) + ", b = " + Arrays.toString(b), 7);

        boolean match = Arrays.equals(a, b);
        viz.setVariable("isMatch", match);
        if (match) {
            viz.step("Arrays.equals(a, b) -> TRUE! Strings \"" + str1 + "\" and \"" + str2 + "\" are valid anagrams! 🎯", 9);
        } else {
            viz.step("Arrays.equals(a, b) -> FALSE! Strings are not anagrams.", 9);
        }

        return viz.getTimeline();
    }
}
