package com.leetcode.solutions;

import com.leetcode.visualizer.core.Timeline;

/**
 * Base class for all LeetCode solutions.
 * Each solution implements its algorithm while recording execution frames
 * to the visualizer timeline.
 */
public abstract class BaseSolution {
    public abstract int getProblemId();
    public abstract String getProblemTitle();
    public abstract String getCategory();
    public abstract String getDifficulty();
    public abstract String getCodeSource();

    /**
     * Executes the solution and returns the generated animation timeline.
     */
    public abstract Timeline run();
}
