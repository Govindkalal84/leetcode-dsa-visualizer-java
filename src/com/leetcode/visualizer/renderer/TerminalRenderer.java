package com.leetcode.visualizer.renderer;

import com.leetcode.visualizer.core.FrameSnapshot;
import com.leetcode.visualizer.core.Timeline;
import java.util.*;

/**
 * Terminal renderer that draws ANSI colorized, animated visual data structures
 * in real-time right in the console.
 */
public class TerminalRenderer {
    // ANSI Colors
    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";
    private static final String BLUE = "\u001B[34m";
    private static final String MAGENTA = "\u001B[35m";
    private static final String BG_BLUE = "\u001B[44m\u001B[37m";
    private static final String BG_GREEN = "\u001B[42m\u001B[30m";

    public static void play(Timeline timeline, int delayMs) {
        System.out.println(BOLD + CYAN + "=== Starting Terminal Playback: LC " 
            + timeline.getProblemId() + " - " + timeline.getProblemTitle() + " ===" + RESET);

        for (int i = 0; i < timeline.getSnapshots().size(); i++) {
            renderFrame(timeline, i);
            try {
                Thread.sleep(delayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("\n" + BOLD + GREEN + "✔ Playback complete!" + RESET);
    }

    @SuppressWarnings("unchecked")
    public static void renderFrame(Timeline timeline, int frameIndex) {
        FrameSnapshot frame = timeline.getSnapshots().get(frameIndex);

        // Clear terminal
        System.out.print("\033[H\033[2J");
        System.out.flush();

        // Header
        System.out.println(BOLD + BG_BLUE + "  LeetCode " + timeline.getProblemId() + ": " 
            + timeline.getProblemTitle() + "  " + RESET + "  "
            + YELLOW + "Step [" + (frameIndex + 1) + " / " + timeline.totalFrames() + "]" + RESET);
        System.out.println(CYAN + "─".repeat(65) + RESET);

        // Narration box
        System.out.println(BOLD + "💡 Explanation:" + RESET + " " + frame.getExplanation());
        if (frame.getCodeLine() > 0) {
            System.out.println(MAGENTA + "▶ Executing Line: " + frame.getCodeLine() + RESET);
        }
        System.out.println(CYAN + "─".repeat(65) + RESET);

        // Render Data Structure
        String comp = frame.getComponentType();
        if ("ARRAY".equals(comp)) {
            renderArray(frame);
        } else if ("LINKED_LIST".equals(comp)) {
            renderLinkedList(frame);
        } else if ("DP_TABLE".equals(comp)) {
            renderDPTable(frame);
        } else if ("TREE".equals(comp)) {
            renderTree(frame);
        } else if ("GRAPH".equals(comp)) {
            renderGraph(frame);
        }

        // Variables Watcher
        System.out.println(CYAN + "─".repeat(65) + RESET);
        System.out.print(BOLD + "🔍 Variables: " + RESET);
        if (frame.getVariables().isEmpty()) {
            System.out.println("(none)");
        } else {
            List<String> varParts = new ArrayList<>();
            for (Map.Entry<String, String> e : frame.getVariables().entrySet()) {
                varParts.add(YELLOW + e.getKey() + RESET + " = " + GREEN + e.getValue() + RESET);
            }
            System.out.println(String.join(",  ", varParts));
        }
        System.out.println();
    }

    @SuppressWarnings("unchecked")
    private static void renderArray(FrameSnapshot frame) {
        int[] arr = (int[]) frame.getStateData().get("array");
        if (arr == null) return;

        Map<String, Integer> pointers = (Map<String, Integer>) frame.getStateData().get("pointers");
        Map<Integer, String> highlights = (Map<Integer, String>) frame.getStateData().get("highlights");
        Integer wStart = (Integer) frame.getStateData().get("windowStart");
        Integer wEnd = (Integer) frame.getStateData().get("windowEnd");

        System.out.println(BOLD + "Data Structure: Array" + RESET);

        // Top line
        StringBuilder top = new StringBuilder("  ");
        StringBuilder middle = new StringBuilder("  ");
        StringBuilder bottom = new StringBuilder("  ");
        StringBuilder indexLine = new StringBuilder("  ");

        for (int i = 0; i < arr.length; i++) {
            boolean isHighlighted = highlights != null && highlights.containsKey(i);
            boolean inWindow = wStart != null && wEnd != null && i >= wStart && i <= wEnd;

            String boxColor = isHighlighted ? GREEN : (inWindow ? YELLOW : CYAN);

            top.append(boxColor).append("┌────┐").append(RESET);
            middle.append(boxColor).append("│").append(RESET)
                  .append(BOLD).append(String.format("%3d ", arr[i])).append(RESET)
                  .append(boxColor).append("│").append(RESET);
            bottom.append(boxColor).append("└────┘").append(RESET);
            indexLine.append(String.format("  [%d] ", i));
        }

        System.out.println(top);
        System.out.println(middle);
        System.out.println(bottom);
        System.out.println(indexLine);

        // Pointers line
        if (pointers != null && !pointers.isEmpty()) {
            StringBuilder ptrLine = new StringBuilder("  ");
            for (int i = 0; i < arr.length; i++) {
                List<String> ptrsAtI = new ArrayList<>();
                for (Map.Entry<String, Integer> p : pointers.entrySet()) {
                    if (p.getValue() == i) {
                        ptrsAtI.add(p.getKey());
                    }
                }
                if (!ptrsAtI.isEmpty()) {
                    String label = "^" + String.join(",", ptrsAtI);
                    ptrLine.append(BOLD).append(YELLOW).append(String.format("%-6s", label)).append(RESET);
                } else {
                    ptrLine.append("      ");
                }
            }
            System.out.println(ptrLine);
        }
    }

    @SuppressWarnings("unchecked")
    private static void renderLinkedList(FrameSnapshot frame) {
        List<Map<String, Object>> nodes = (List<Map<String, Object>>) frame.getStateData().get("nodes");
        Map<String, Integer> pointers = (Map<String, Integer>) frame.getStateData().get("pointers");

        System.out.println(BOLD + "Data Structure: Linked List" + RESET);
        if (nodes == null || nodes.isEmpty()) {
            System.out.println("  (empty)");
            return;
        }

        StringBuilder sb = new StringBuilder("  ");
        for (int i = 0; i < nodes.size(); i++) {
            Map<String, Object> node = nodes.get(i);
            int id = (int) node.get("id");
            int val = (int) node.get("val");
            int nextId = (int) node.get("nextId");

            // Pointer label check
            List<String> ptrsHere = new ArrayList<>();
            if (pointers != null) {
                for (Map.Entry<String, Integer> pe : pointers.entrySet()) {
                    if (pe.getValue() == id) ptrsHere.add(pe.getKey());
                }
            }

            if (!ptrsHere.isEmpty()) {
                sb.append(YELLOW).append("[").append(String.join(",", ptrsHere)).append("] ").append(RESET);
            }

            sb.append(BOLD).append(GREEN).append("(").append(val).append(")").append(RESET);
            if (nextId != -1) {
                sb.append(CYAN).append(" ──► ").append(RESET);
            } else {
                sb.append(RED).append(" ──► NULL").append(RESET);
            }
        }
        System.out.println(sb);
    }

    @SuppressWarnings("unchecked")
    private static void renderDPTable(FrameSnapshot frame) {
        int[][] table = (int[][]) frame.getStateData().get("table");
        List<String> colLabels = (List<String>) frame.getStateData().get("colLabels");
        List<String> rowLabels = (List<String>) frame.getStateData().get("rowLabels");
        Integer activeR = (Integer) frame.getStateData().get("activeRow");
        Integer activeC = (Integer) frame.getStateData().get("activeCol");

        System.out.println(BOLD + "Data Structure: Dynamic Programming Table" + RESET);
        if (table == null) return;

        // Print header row
        System.out.print("       ");
        for (String c : colLabels) {
            System.out.printf("%6s", c);
        }
        System.out.println();

        for (int r = 0; r < table.length; r++) {
            String rLabel = (rowLabels != null && r < rowLabels.size()) ? rowLabels.get(r) : "R" + r;
            System.out.printf(CYAN + "%-6s │" + RESET, rLabel);
            for (int c = 0; c < table[r].length; c++) {
                boolean isActive = activeR != null && activeC != null && activeR == r && activeC == c;
                if (isActive) {
                    System.out.print(BG_GREEN + String.format("%5d ", table[r][c]) + RESET);
                } else {
                    System.out.printf("%5d ", table[r][c]);
                }
            }
            System.out.println();
        }
    }

    @SuppressWarnings("unchecked")
    private static void renderTree(FrameSnapshot frame) {
        List<Map<String, Object>> nodes = (List<Map<String, Object>>) frame.getStateData().get("nodes");
        Integer activeNode = (Integer) frame.getStateData().get("activeNodeId");

        System.out.println(BOLD + "Data Structure: Binary Tree" + RESET);
        if (nodes == null || nodes.isEmpty()) return;

        for (Map<String, Object> n : nodes) {
            int id = (int) n.get("id");
            int val = (int) n.get("val");
            int l = (int) n.get("leftId");
            int r = (int) n.get("rightId");

            boolean isActive = activeNode != null && activeNode == id;
            String prefix = isActive ? (BG_GREEN + " ▶ ACTIVE " + RESET + " ") : "   ";
            System.out.printf("%sNode [%d] (val=%d) -> Left: %s, Right: %s%n",
                prefix, id, val, (l != -1 ? "Node " + l : "NULL"), (r != -1 ? "Node " + r : "NULL"));
        }
    }

    @SuppressWarnings("unchecked")
    private static void renderGraph(FrameSnapshot frame) {
        List<Map<String, Object>> nodes = (List<Map<String, Object>>) frame.getStateData().get("nodes");
        List<String> qs = (List<String>) frame.getStateData().get("queueOrStack");
        String active = (String) frame.getStateData().get("activeNodeId");

        System.out.println(BOLD + "Data Structure: Graph" + RESET);
        if (nodes != null) {
            System.out.print("  Nodes: ");
            for (Map<String, Object> n : nodes) {
                String id = (String) n.get("id");
                String state = (String) n.get("state");
                boolean isActive = id.equals(active);
                String color = isActive ? BG_GREEN : ("VISITED".equals(state) ? GREEN : CYAN);
                System.out.print(color + " " + id + " " + RESET + " ");
            }
            System.out.println();
        }
        if (qs != null) {
            System.out.println(YELLOW + "  Queue / Traversal Stack: " + RESET + qs);
        }
    }
}
