package com.leetcode.solutions.graphs;

import com.leetcode.solutions.BaseSolution;
import com.leetcode.visualizer.core.Timeline;
import com.leetcode.visualizer.core.Visualizer;
import com.leetcode.visualizer.primitives.GraphVisualizer;

public class LC0200_NumberOfIslands extends BaseSolution {
    @Override
    public int getProblemId() { return 200; }

    @Override
    public String getProblemTitle() { return "Number of Islands"; }

    @Override
    public String getCategory() { return "Graphs & Matrix"; }

    @Override
    public String getDifficulty() { return "Medium"; }

    @Override
    public String getCodeSource() {
        return "public int numIslands(char[][] grid) {\n" +
               "    int count = 0;\n" +
               "    for (int r = 0; r < grid.length; r++) {\n" +
               "        for (int c = 0; c < grid[0].length; c++) {\n" +
               "            if (grid[r][c] == '1') {\n" +
               "                count++;\n" +
               "                bfs(grid, r, c);\n" +
               "            }\n" +
               "        }\n" +
               "    }\n" +
               "    return count;\n" +
               "}";
    }

    @Override
    public Timeline run() {
        Visualizer viz = new Visualizer(200, "Number of Islands", getCodeSource());
        GraphVisualizer graphViz = new GraphVisualizer(viz);

        // Small 3x3 grid:
        // 1 1 0
        // 1 0 0
        // 0 0 1
        String[][] grid = {
            {"1", "1", "0"},
            {"1", "0", "0"},
            {"0", "0", "1"}
        };

        // Add nodes
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                String id = r + "," + c;
                graphViz.addNode(id, "(" + r + "," + c + ")=" + grid[r][c]);
            }
        }
        graphViz.addEdge("0,0", "0,1");
        graphViz.addEdge("0,0", "1,0");

        int islandCount = 0;
        viz.setVariable("islandCount", 0);
        viz.step("Initialize grid graph with 3x3 cells. Looking for unvisited '1's (land).", 2);

        // Island 1 at (0,0)
        islandCount++;
        viz.setVariable("islandCount", islandCount);
        graphViz.setActiveNode("0,0");
        graphViz.setNodeState("0,0", "VISITED");
        graphViz.pushQueueOrStack("(0,0)");
        viz.step("Found new land at (0,0)! Incremented island count to " + islandCount + ". Enqueue (0,0) for BFS.", 5);

        // BFS step 1: explore neighbors
        graphViz.popQueueOrStack();
        graphViz.setActiveNode("0,1");
        graphViz.setNodeState("0,1", "VISITED");
        graphViz.pushQueueOrStack("(0,1)");
        viz.step("BFS visiting neighbor (0,1). It is land! Mark visited and enqueue.", 7);

        graphViz.setActiveNode("1,0");
        graphViz.setNodeState("1,0", "VISITED");
        graphViz.pushQueueOrStack("(1,0)");
        viz.step("BFS visiting neighbor (1,0). It is land! Mark visited and enqueue.", 7);

        graphViz.popQueueOrStack();
        graphViz.popQueueOrStack();
        viz.step("Finished BFS for Island 1. All connected land cells marked as visited.", 8);

        // Island 2 at (2,2)
        islandCount++;
        viz.setVariable("islandCount", islandCount);
        graphViz.setActiveNode("2,2");
        graphViz.setNodeState("2,2", "VISITED");
        viz.step("Found isolated land cell at (2,2)! Incremented island count to " + islandCount + ".", 5);

        graphViz.setActiveNode(null);
        viz.step("Grid traversal finished. Total number of distinct islands found: " + islandCount, 11);
        return viz.getTimeline();
    }
}
