package com.leetcode.generator;

import com.leetcode.solutions.BaseSolution;
import com.leetcode.solutions.arrays.LC0001_TwoSum;
import com.leetcode.solutions.arrays.LC0011_ContainerWithMostWater;
import com.leetcode.solutions.arrays.LC0704_BinarySearch;
import com.leetcode.solutions.dp.LC0070_ClimbingStairs;
import com.leetcode.solutions.dp.LC0322_CoinChange;
import com.leetcode.solutions.graphs.LC0200_NumberOfIslands;
import com.leetcode.solutions.linkedlist.LC0141_LinkedListCycle;
import com.leetcode.solutions.linkedlist.LC0206_ReverseLinkedList;
import com.leetcode.solutions.slidingwindow.LC0003_LongestSubstringWithoutRepeating;
import com.leetcode.solutions.trees.LC0226_InvertBinaryTree;

import java.util.*;
import java.util.function.Supplier;

/**
 * Registry containing metadata for LeetCode questions and factory instances.
 */
public class ProblemCatalog {
    public static class ProblemMeta {
        public final int id;
        public final String title;
        public final String category;
        public final String difficulty;
        public final Supplier<BaseSolution> factory;

        public ProblemMeta(int id, String title, String category, String difficulty, Supplier<BaseSolution> factory) {
            this.id = id;
            this.title = title;
            this.category = category;
            this.difficulty = difficulty;
            this.factory = factory;
        }
    }

    private static final Map<Integer, ProblemMeta> registry = new LinkedHashMap<>();

    static {
        register(new ProblemMeta(1, "Two Sum", "Arrays & Hash Table", "Easy", LC0001_TwoSum::new));
        register(new ProblemMeta(3, "Longest Substring Without Repeating Characters", "Sliding Window", "Medium", LC0003_LongestSubstringWithoutRepeating::new));
        register(new ProblemMeta(11, "Container With Most Water", "Two Pointers", "Medium", LC0011_ContainerWithMostWater::new));
        register(new ProblemMeta(70, "Climbing Stairs", "Dynamic Programming", "Easy", LC0070_ClimbingStairs::new));
        register(new ProblemMeta(141, "Linked List Cycle", "Linked List", "Easy", LC0141_LinkedListCycle::new));
        register(new ProblemMeta(200, "Number of Islands", "Graphs & Matrix", "Medium", LC0200_NumberOfIslands::new));
        register(new ProblemMeta(206, "Reverse Linked List", "Linked List", "Easy", LC0206_ReverseLinkedList::new));
        register(new ProblemMeta(226, "Invert Binary Tree", "Binary Tree", "Easy", LC0226_InvertBinaryTree::new));
        register(new ProblemMeta(242, "Valid Anagram", "Strings & Sorting", "Easy", com.leetcode.solutions.strings.LC0242_ValidAnagram::new));
        register(new ProblemMeta(322, "Coin Change", "Dynamic Programming", "Medium", LC0322_CoinChange::new));
        register(new ProblemMeta(704, "Binary Search", "Binary Search", "Easy", LC0704_BinarySearch::new));
    }

    public static void register(ProblemMeta meta) {
        registry.put(meta.id, meta);
    }

    public static BaseSolution getSolutionInstance(int problemId) {
        ProblemMeta meta = registry.get(problemId);
        if (meta != null && meta.factory != null) {
            return meta.factory.get();
        }
        return null;
    }

    public static Collection<ProblemMeta> getAll() {
        return registry.values();
    }

    public static String getProblemsJson() {
        StringBuilder sb = new StringBuilder("[");
        int count = 0;
        for (ProblemMeta m : registry.values()) {
            if (count > 0) sb.append(",");
            sb.append("{")
              .append("\"id\":").append(m.id).append(",")
              .append("\"title\":\"").append(m.title).append("\",")
              .append("\"category\":\"").append(m.category).append("\",")
              .append("\"difficulty\":\"").append(m.difficulty).append("\"")
              .append("}");
            count++;
        }
        sb.append("]");
        return sb.toString();
    }
}
