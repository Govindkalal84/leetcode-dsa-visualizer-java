package com.leetcode.solutions.dp;

import com.leetcode.solutions.BaseSolution;
import com.leetcode.visualizer.core.Timeline;
import com.leetcode.visualizer.core.Visualizer;
import com.leetcode.visualizer.primitives.DPTableVisualizer;

import java.util.Arrays;

public class LC0322_CoinChange extends BaseSolution {
    @Override
    public int getProblemId() { return 322; }

    @Override
    public String getProblemTitle() { return "Coin Change"; }

    @Override
    public String getCategory() { return "Dynamic Programming"; }

    @Override
    public String getDifficulty() { return "Medium"; }

    @Override
    public String getCodeSource() {
        return "public int coinChange(int[] coins, int amount) {\n" +
               "    int[] dp = new int[amount + 1];\n" +
               "    Arrays.fill(dp, amount + 1);\n" +
               "    dp[0] = 0;\n" +
               "    for (int i = 1; i <= amount; i++) {\n" +
               "        for (int coin : coins) {\n" +
               "            if (i - coin >= 0) {\n" +
               "                dp[i] = Math.min(dp[i], dp[i - coin] + 1);\n" +
               "            }\n" +
               "        }\n" +
               "    }\n" +
               "    return dp[amount] > amount ? -1 : dp[amount];\n" +
               "}";
    }

    @Override
    public Timeline run() {
        int[] coins = {1, 2, 5};
        int amount = 7;

        Visualizer viz = new Visualizer(322, "Coin Change", getCodeSource());

        String[] colLabels = new String[amount + 1];
        for (int i = 0; i <= amount; i++) colLabels[i] = "$" + i;

        DPTableVisualizer dpViz = new DPTableVisualizer(viz, 1, amount + 1, new String[]{"dp"}, colLabels);

        int[] dp = new int[amount + 1];
        Arrays.fill(dp, 99);
        dp[0] = 0;
        dpViz.setCell(0, 0, 0);
        for (int i = 1; i <= amount; i++) dpViz.setCell(0, i, 99);

        viz.setVariable("coins", Arrays.toString(coins));
        viz.setVariable("amount", amount);
        viz.step("Initialize DP array of size " + (amount + 1) + ". Base case: dp[0] = 0 coins needed for $0. Others initialized to INF (99).", 4);

        for (int i = 1; i <= amount; i++) {
            dpViz.setActiveCell(0, i);
            viz.setVariable("currentAmount", i);

            for (int coin : coins) {
                viz.setVariable("checkingCoin", coin);
                dpViz.clearDependencies();

                if (i - coin >= 0) {
                    dpViz.addDependency(0, i - coin);
                    int candidate = dp[i - coin] + 1;
                    viz.setVariable("dp[i-coin]", dp[i - coin]);
                    viz.setVariable("candidateCost", candidate);

                    if (candidate < dp[i]) {
                        dp[i] = candidate;
                        dpViz.setCell(0, i, dp[i]);
                        viz.step("Amount $" + i + " with coin " + coin + ": dp[" + i + " - " + coin + "] + 1 = " 
                            + dp[i - coin] + " + 1 = " + candidate + ". Improved min coins to " + dp[i] + "!", 8);
                    } else {
                        viz.step("Amount $" + i + " with coin " + coin + ": candidate " + candidate + " not better than current dp[" + i + "] = " + dp[i] + ".", 8);
                    }
                } else {
                    viz.step("Coin " + coin + " is greater than amount $" + i + ". Cannot use.", 7);
                }
            }
        }

        dpViz.clearDependencies();
        viz.step("Finished computing DP table! Minimum coins required for $" + amount + " is: " + dp[amount], 11);
        return viz.getTimeline();
    }
}
