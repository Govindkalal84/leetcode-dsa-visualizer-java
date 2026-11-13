package com.leetcode.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Automates scaffolding of new LeetCode solution templates
 * equipped with visualizer hooks, enabling rapid scaling to 3,000+ problems.
 */
public class ProblemScaffolder {

    public static void scaffold(File srcDir, int id, String title, String category, String difficulty) {
        String cleanTitle = title.replaceAll("[^a-zA-Z0-9]", "");
        String paddedId = String.format("%04d", id);
        String className = "LC" + paddedId + "_" + cleanTitle;

        String subPkg = "custom";
        String visualizerImport = "com.leetcode.visualizer.primitives.ArrayVisualizer";
        String visualizerSetup = "ArrayVisualizer arrayViz = new ArrayVisualizer(viz, new int[]{1, 2, 3, 4, 5});";

        String lowerCat = category.toLowerCase();
        if (lowerCat.contains("tree")) {
            subPkg = "trees";
            visualizerImport = "com.leetcode.visualizer.primitives.TreeVisualizer";
            visualizerSetup = "TreeVisualizer treeViz = new TreeVisualizer(viz);\n        int root = treeViz.createNode(10);";
        } else if (lowerCat.contains("list")) {
            subPkg = "linkedlist";
            visualizerImport = "com.leetcode.visualizer.primitives.LinkedListVisualizer";
            visualizerSetup = "LinkedListVisualizer listViz = new LinkedListVisualizer(viz);\n        listViz.addNode(1, -1);";
        } else if (lowerCat.contains("dp") || lowerCat.contains("dynamic")) {
            subPkg = "dp";
            visualizerImport = "com.leetcode.visualizer.primitives.DPTableVisualizer";
            visualizerSetup = "DPTableVisualizer dpViz = new DPTableVisualizer(viz, 3, 5, null, null);";
        } else if (lowerCat.contains("graph")) {
            subPkg = "graphs";
            visualizerImport = "com.leetcode.visualizer.primitives.GraphVisualizer";
            visualizerSetup = "GraphVisualizer graphViz = new GraphVisualizer(viz);\n        graphViz.addNode(\"A\", \"Start\");";
        } else if (lowerCat.contains("window") || lowerCat.contains("sliding")) {
            subPkg = "slidingwindow";
            visualizerImport = "com.leetcode.visualizer.primitives.ArrayVisualizer";
            visualizerSetup = "ArrayVisualizer arrayViz = new ArrayVisualizer(viz, new int[]{1, 2, 3, 4, 5});\n        arrayViz.setWindow(0, 2);";
        } else {
            subPkg = "arrays";
        }

        File targetDir = new File(srcDir, "com/leetcode/solutions/" + subPkg);
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }

        File targetFile = new File(targetDir, className + ".java");
        if (targetFile.exists()) {
            System.out.println("⚠️ File already exists: " + targetFile.getAbsolutePath());
            return;
        }

        String template = "package com.leetcode.solutions." + subPkg + ";\n\n" +
            "import com.leetcode.solutions.BaseSolution;\n" +
            "import com.leetcode.visualizer.core.Timeline;\n" +
            "import com.leetcode.visualizer.core.Visualizer;\n" +
            "import " + visualizerImport + ";\n\n" +
            "public class " + className + " extends BaseSolution {\n" +
            "    @Override\n" +
            "    public int getProblemId() { return " + id + "; }\n\n" +
            "    @Override\n" +
            "    public String getProblemTitle() { return \"" + title + "\"; }\n\n" +
            "    @Override\n" +
            "    public String getCategory() { return \"" + category + "\"; }\n\n" +
            "    @Override\n" +
            "    public String getDifficulty() { return \"" + difficulty + "\"; }\n\n" +
            "    @Override\n" +
            "    public String getCodeSource() {\n" +
            "        return \"// TODO: Add solution algorithm code here\\n\" +\n" +
            "               \"public void solve() {\\n\" +\n" +
            "               \"    // implementation\\n\" +\n" +
            "               \"}\";\n" +
            "    }\n\n" +
            "    @Override\n" +
            "    public Timeline run() {\n" +
            "        Visualizer viz = new Visualizer(" + id + ", \"" + title + "\", getCodeSource());\n" +
            "        " + visualizerSetup + "\n\n" +
            "        viz.step(\"Step 1: Initializing problem state for " + title + "\", 1);\n" +
            "        // TODO: Wire up algorithmic steps, comparisons, and state transitions here\n" +
            "        viz.step(\"Completed solving " + title + "!\", 3);\n\n" +
            "        return viz.getTimeline();\n" +
            "    }\n" +
            "}\n";

        try (FileWriter writer = new FileWriter(targetFile)) {
            writer.write(template);
            System.out.println("✅ Generated new LeetCode problem template at: " + targetFile.getAbsolutePath());
            System.out.println("   Problem ID: " + id + " | Title: " + title + " | Pattern: " + category);
        } catch (IOException e) {
            System.err.println("❌ Failed to scaffold file: " + e.getMessage());
        }
    }
}
