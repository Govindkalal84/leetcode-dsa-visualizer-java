package com.leetcode.visualizer.primitives;

import com.leetcode.visualizer.core.Visualizer;
import java.util.*;

/**
 * Reusable visual primitive for 1D arrays, multi-pointers,
 * highlights, comparisons, swaps, and sliding windows.
 */
public class ArrayVisualizer {
    private final Visualizer visualizer;
    private int[] elements;
    private final Map<String, Integer> pointers = new LinkedHashMap<>();
    private final Map<Integer, String> highlights = new LinkedHashMap<>();
    private int windowStart = -1;
    private int windowEnd = -1;

    public ArrayVisualizer(Visualizer visualizer, int[] initialArray) {
        this.visualizer = visualizer;
        this.elements = Arrays.copyOf(initialArray, initialArray.length);
        this.visualizer.setComponentType("ARRAY");
        sync();
    }

    public void setPointer(String pointerName, int index) {
        pointers.put(pointerName, index);
        sync();
    }

    public void removePointer(String pointerName) {
        pointers.remove(pointerName);
        sync();
    }

    public void highlight(int index, String colorOrType) {
        highlights.put(index, colorOrType);
        sync();
    }

    public void clearHighlights() {
        highlights.clear();
        sync();
    }

    public void setWindow(int start, int end) {
        this.windowStart = start;
        this.windowEnd = end;
        sync();
    }

    public void clearWindow() {
        this.windowStart = -1;
        this.windowEnd = -1;
        sync();
    }

    public void set(int index, int value) {
        elements[index] = value;
        sync();
    }

    public void swap(int i, int j) {
        int tmp = elements[i];
        elements[i] = elements[j];
        elements[j] = tmp;
        sync();
    }

    public int[] getElements() {
        return elements;
    }

    private void sync() {
        visualizer.updateState("array", Arrays.copyOf(elements, elements.length));
        visualizer.updateState("pointers", new LinkedHashMap<>(pointers));
        visualizer.updateState("highlights", new LinkedHashMap<>(highlights));
        visualizer.updateState("windowStart", windowStart);
        visualizer.updateState("windowEnd", windowEnd);
    }
}
