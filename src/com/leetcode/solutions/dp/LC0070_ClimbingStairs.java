package com.leetcode.solutions.dp;

import com.leetcode.solutions.BaseSolution;
import com.leetcode.visualizer.core.Timeline;
import com.leetcode.visualizer.core.Visualizer;
import com.leetcode.visualizer.primitives.DPTableVisualizer;

public class LC0070_ClimbingStairs extends BaseSolution {
    @Override public int getProblemId() { return 70; }
    @Override public String getProblemTitle() { return "Climbing Stairs"; }
    @Override public String getCategory() { return "Dynamic Programming"; }
    @Override public String getDifficulty() { return "Easy"; }

    @Override
    public String getCodeSource() {
        return "public int climbStairs(int n) {\n" +
               "    if (n <= 2) return n;\n" +
               "    int[] dp = new int[n + 1];\n" +
               "    dp[1] = 1;\n" +
               "    dp[2] = 2;\n" +
               "    for (int i = 3; i <= n; i++) {\n" +
               "        dp[i] = dp[i - 1] + dp[i - 2];\n" +
               "    }\n" +
               "    return dp[n];\n" +
               "}";
    }

    @Override
    public Timeline run() {
        int n = 5;
        Visualizer viz = new Visualizer(70, "Climbing Stairs", getCodeSource());

        String[] colLabels = new String[n + 1];
        for (int i = 0; i <= n; i++) colLabels[i] = "step_" + i;

        DPTableVisualizer dpViz = new DPTableVisualizer(viz, 1, n + 1, new String[]{"ways"}, colLabels);

        int[] dp = new int[n + 1];
        dp[1] = 1;
        dp[2] = 2;
        dpViz.setCell(0, 1, 1);
        dpViz.setCell(0, 2, 2);

        viz.setVariable("n", n);
        viz.step("Base cases: dp[1] = 1 way (1 step), dp[2] = 2 ways (1+1 or 2 steps).", 4);

        for (int i = 3; i <= n; i++) {
            dpViz.clearDependencies();
            dpViz.addDependency(0, i - 1);
            dpViz.addDependency(0, i - 2);
            dpViz.setActiveCell(0, i);

            dp[i] = dp[i - 1] + dp[i - 2];
            dpViz.setCell(0, i, dp[i]);

            viz.setVariable("i", i);
            viz.setVariable("dp[i-1]", dp[i - 1]);
            viz.setVariable("dp[i-2]", dp[i - 2]);
            viz.setVariable("dp[i]", dp[i]);

            viz.step("Step " + i + ": dp[" + i + "] = dp[" + (i - 1) + "] + dp[" + (i - 2) + "] = " 
                + dp[i - 1] + " + " + dp[i - 2] + " = " + dp[i] + " distinct ways.", 7);
        }

        dpViz.clearDependencies();
        viz.step("Finished! Total distinct ways to climb " + n + " stairs: " + dp[n], 9);
        return viz.getTimeline();
    }
}
