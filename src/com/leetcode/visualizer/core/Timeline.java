package com.leetcode.visualizer.core;

import java.util.*;

/**
 * Records and manages the sequence of animation frames.
 * Contains a zero-dependency JSON serializer for web visualization.
 */
public class Timeline {
    private final List<FrameSnapshot> snapshots = new ArrayList<>();
    private final String problemTitle;
    private final int problemId;
    private final String codeSource;

    public Timeline(int problemId, String problemTitle, String codeSource) {
        this.problemId = problemId;
        this.problemTitle = problemTitle;
        this.codeSource = codeSource;
    }

    public void addSnapshot(FrameSnapshot snapshot) {
        snapshots.add(snapshot);
    }

    public List<FrameSnapshot> getSnapshots() {
        return snapshots;
    }

    public int totalFrames() {
        return snapshots.size();
    }

    public int getProblemId() {
        return problemId;
    }

    public String getProblemTitle() {
        return problemTitle;
    }

    public String getCodeSource() {
        return codeSource;
    }

    /**
     * Converts the complete timeline and frames into standard JSON
     * without any third-party dependencies.
     */
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"problemId\":").append(problemId).append(",");
        sb.append("\"problemTitle\":").append(quote(problemTitle)).append(",");
        sb.append("\"codeSource\":").append(quote(codeSource)).append(",");
        sb.append("\"totalFrames\":").append(snapshots.size()).append(",");
        sb.append("\"frames\":[");

        for (int i = 0; i < snapshots.size(); i++) {
            if (i > 0) sb.append(",");
            FrameSnapshot snap = snapshots.get(i);
            sb.append("{");
            sb.append("\"frameIndex\":").append(snap.getFrameIndex()).append(",");
            sb.append("\"explanation\":").append(quote(snap.getExplanation())).append(",");
            sb.append("\"codeLine\":").append(snap.getCodeLine()).append(",");
            sb.append("\"componentType\":").append(quote(snap.getComponentType())).append(",");

            // Variables map
            sb.append("\"variables\":{");
            int vCount = 0;
            for (Map.Entry<String, String> entry : snap.getVariables().entrySet()) {
                if (vCount > 0) sb.append(",");
                sb.append(quote(entry.getKey())).append(":").append(quote(entry.getValue()));
                vCount++;
            }
            sb.append("},");

            // State data map
            sb.append("\"state\":");
            serializeMap(sb, snap.getStateData());

            sb.append("}");
        }

        sb.append("]}");
        return sb.toString();
    }

    private void serializeMap(StringBuilder sb, Map<?, ?> map) {
        sb.append("{");
        int count = 0;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (count > 0) sb.append(",");
            sb.append(quote(String.valueOf(entry.getKey()))).append(":");
            serializeValue(sb, entry.getValue());
            count++;
        }
        sb.append("}");
    }

    private void serializeValue(StringBuilder sb, Object val) {
        if (val == null) {
            sb.append("null");
        } else if (val instanceof String) {
            sb.append(quote((String) val));
        } else if (val instanceof Number || val instanceof Boolean) {
            sb.append(val);
        } else if (val instanceof int[]) {
            int[] arr = (int[]) val;
            sb.append("[");
            for (int j = 0; j < arr.length; j++) {
                if (j > 0) sb.append(",");
                sb.append(arr[j]);
            }
            sb.append("]");
        } else if (val instanceof int[][]) {
            int[][] grid = (int[][]) val;
            sb.append("[");
            for (int r = 0; r < grid.length; r++) {
                if (r > 0) sb.append(",");
                sb.append("[");
                for (int c = 0; c < grid[r].length; c++) {
                    if (c > 0) sb.append(",");
                    sb.append(grid[r][c]);
                }
                sb.append("]");
            }
            sb.append("]");
        } else if (val instanceof List) {
            List<?> list = (List<?>) val;
            sb.append("[");
            for (int j = 0; j < list.size(); j++) {
                if (j > 0) sb.append(",");
                serializeValue(sb, list.get(j));
            }
            sb.append("]");
        } else if (val instanceof Map) {
            serializeMap(sb, (Map<?, ?>) val);
        } else {
            sb.append(quote(val.toString()));
        }
    }

    private String quote(String s) {
        if (s == null) return "\"\"";
        StringBuilder res = new StringBuilder("\"");
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"': res.append("\\\""); break;
                case '\\': res.append("\\\\"); break;
                case '\b': res.append("\\b"); break;
                case '\f': res.append("\\f"); break;
                case '\n': res.append("\\n"); break;
                case '\r': res.append("\\r"); break;
                case '\t': res.append("\\t"); break;
                default:
                    if (c < ' ') {
                        res.append(String.format("\\u%04x", (int) c));
                    } else {
                        res.append(c);
                    }
            }
        }
        res.append("\"");
        return res.toString();
    }
}
