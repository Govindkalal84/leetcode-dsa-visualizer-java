package com.leetcode.visualizer.primitives;

import com.leetcode.visualizer.core.Visualizer;
import java.util.*;

/**
 * Reusable visual primitive for Dynamic Programming 1D and 2D tables,
 * transition dependencies, and memoization grids.
 */
public class DPTableVisualizer {
    private final Visualizer visualizer;
    private final int rows;
    private final int cols;
    private final String[] rowLabels;
    private final String[] colLabels;
    private final int[][] table;
    private int activeRow = -1;
    private int activeCol = -1;
    private final List<int[]> dependencyCells = new ArrayList<>();

    public DPTableVisualizer(Visualizer visualizer, int rows, int cols, String[] rowLabels, String[] colLabels) {
        this.visualizer = visualizer;
        this.rows = rows;
        this.cols = cols;
        this.rowLabels = rowLabels != null ? rowLabels : generateDefaultLabels("R", rows);
        this.colLabels = colLabels != null ? colLabels : generateDefaultLabels("C", cols);
        this.table = new int[rows][cols];
        for (int[] r : this.table) {
            Arrays.fill(r, 0);
        }
        this.visualizer.setComponentType("DP_TABLE");
        sync();
    }

    private static String[] generateDefaultLabels(String prefix, int count) {
        String[] labels = new String[count];
        for (int i = 0; i < count; i++) {
            labels[i] = prefix + i;
        }
        return labels;
    }

    public void setCell(int r, int c, int val) {
        table[r][c] = val;
        sync();
    }

    public void setActiveCell(int r, int c) {
        this.activeRow = r;
        this.activeCol = c;
        sync();
    }

    public void clearDependencies() {
        dependencyCells.clear();
        sync();
    }

    public void addDependency(int r, int c) {
        dependencyCells.add(new int[]{r, c});
        sync();
    }

    public int get(int r, int c) {
        return table[r][c];
    }

    private void sync() {
        visualizer.updateState("rows", rows);
        visualizer.updateState("cols", cols);
        visualizer.updateState("rowLabels", Arrays.asList(rowLabels));
        visualizer.updateState("colLabels", Arrays.asList(colLabels));
        visualizer.updateState("table", table);
        visualizer.updateState("activeRow", activeRow);
        visualizer.updateState("activeCol", activeCol);

        List<List<Integer>> deps = new ArrayList<>();
        for (int[] d : dependencyCells) {
            deps.add(Arrays.asList(d[0], d[1]));
        }
        visualizer.updateState("dependencies", deps);
    }
}
