package com.leetcode;

import com.leetcode.generator.ProblemCatalog;
import com.leetcode.generator.ProblemScaffolder;
import com.leetcode.solutions.BaseSolution;
import com.leetcode.visualizer.core.Timeline;
import com.leetcode.visualizer.renderer.TerminalRenderer;
import com.leetcode.visualizer.renderer.WebRenderer;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && "--help".equalsIgnoreCase(args[0])) {
            printHelp();
            return;
        }

        if (args.length > 0 && "--list".equalsIgnoreCase(args[0])) {
            System.out.println("\n=== Available LeetCode Problems ===");
            for (ProblemCatalog.ProblemMeta m : ProblemCatalog.getAll()) {
                System.out.printf("  LC %-4d | %-45s | %-20s | %s%n", m.id, m.title, m.category, m.difficulty);
            }
            return;
        }

        if (args.length > 0 && "--new".equalsIgnoreCase(args[0])) {
            if (args.length < 5) {
                System.out.println("Usage: java com.leetcode.Main --new <id> <title> <category> <difficulty>");
                System.out.println("Example: java com.leetcode.Main --new 15 \"Three Sum\" \"Two Pointers\" Medium");
                return;
            }
            int id = Integer.parseInt(args[1]);
            String title = args[2];
            String cat = args[3];
            String diff = args[4];
            File srcDir = new File("src");
            if (!srcDir.exists()) {
                srcDir = new File(System.getProperty("user.dir"), "src");
            }
            ProblemScaffolder.scaffold(srcDir, id, title, cat, diff);
            return;
        }

        boolean cliMode = false;
        int targetId = 1;
        int delay = 500;
        int port = 8080;

        for (int i = 0; i < args.length; i++) {
            if ("--cli".equalsIgnoreCase(args[i]) || "-c".equalsIgnoreCase(args[i])) {
                cliMode = true;
            } else if ("--id".equalsIgnoreCase(args[i]) || "-p".equalsIgnoreCase(args[i])) {
                if (i + 1 < args.length) targetId = Integer.parseInt(args[++i]);
            } else if ("--delay".equalsIgnoreCase(args[i])) {
                if (i + 1 < args.length) delay = Integer.parseInt(args[++i]);
            } else if ("--port".equalsIgnoreCase(args[i])) {
                if (i + 1 < args.length) port = Integer.parseInt(args[++i]);
            }
        }

        if (cliMode) {
            BaseSolution sol = ProblemCatalog.getSolutionInstance(targetId);
            if (sol == null) {
                System.err.println("❌ Problem ID " + targetId + " not found in catalog. Run with --list to view available problems.");
                return;
            }
            Timeline timeline = sol.run();
            TerminalRenderer.play(timeline, delay);
        } else {
            // Web mode (default)
            File webDir = new File("src/web");
            if (!webDir.exists()) {
                webDir = new File(System.getProperty("user.dir"), "src/web");
            }
            try {
                WebRenderer web = new WebRenderer(port, webDir);
                web.start();
            } catch (Exception e) {
                System.err.println("❌ Error starting Web Visualizer: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static void printHelp() {
        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║            LeetCode Java DSA Animation Engine                ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println("Commands:");
        System.out.println("  java com.leetcode.Main                     Start interactive Web Player at http://localhost:8080");
        System.out.println("  java com.leetcode.Main --cli --id <ID>     Play ANSI terminal animation (e.g. --id 1)");
        System.out.println("  java com.leetcode.Main --cli --id 11       Play Container With Most Water (Two Pointers)");
        System.out.println("  java com.leetcode.Main --cli --id 3        Play Longest Substring (Sliding Window)");
        System.out.println("  java com.leetcode.Main --cli --id 206      Play Reverse Linked List");
        System.out.println("  java com.leetcode.Main --cli --id 226      Play Invert Binary Tree");
        System.out.println("  java com.leetcode.Main --cli --id 322      Play Coin Change (DP Table)");
        System.out.println("  java com.leetcode.Main --cli --id 200      Play Number of Islands (Graphs)");
        System.out.println("  java com.leetcode.Main --list              List all registered problems");
        System.out.println("  java com.leetcode.Main --new <ID> <Title> <Cat> <Diff>  Scaffold new problem template\n");
    }
}
