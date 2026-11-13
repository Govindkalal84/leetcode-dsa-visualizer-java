# ⚡ LeetCode DSA Visualizer & Solutions Platform (Java Engine)

A high-performance, zero-dependency **Java-based Data Structures and Algorithms (DSA) Animation & Solution Engine** inspired by 3Blue1Brown/Manim.

It is designed to solve LeetCode problems and provide **step-by-step visual, pedagogical walkthroughs** of algorithmic executions across all core patterns (Two Pointers, Sliding Window, Trees, Graphs, Dynamic Programming, Monotonic Stacks, and Linked Lists), scaling to all 3,000+ questions.

---

## 🌟 Key Features

1. **Pure Modern Java (Java 26 / 17+)**:
   - Zero external Maven or Gradle dependencies required.
   - Compiles instantly with standard `javac`.
   - Built-in embedded HTTP server (`com.sun.net.httpserver.HttpServer`) serving a reactive Web UI.

2. **Dual Visual Playback Modes**:
   - **Interactive Web Player (`http://localhost:8080`)**:
     - Modern dark-themed dashboard.
     - Play, pause, step forward, step backward, timeline slider scrubber, and variable-speed playback (0.5x to 4x).
     - Visual animation canvas for Arrays, Linked Lists, Trees, DP Tables, and Graphs.
     - Real-time 3b1b-style narration box explaining the algorithm's intuition.
     - Live variable watcher table and synchronized code line highlight.
   - **Terminal ANSI Animation Mode**:
     - Watch algorithms execute right in your PowerShell / Command Prompt with ANSI colors, step-by-step ASCII box diagrams, and pointer arrows.

3. **Reusable DSA Visual Primitives**:
   - `ArrayVisualizer`: 1D arrays, multi-pointers (`L`, `R`, `pivot`), highlighted element states, and sliding windows.
   - `LinkedListVisualizer`: Node chains, next/prev link redirection, slow/fast cycle pointers.
   - `TreeVisualizer`: 2D binary tree layouts, active node highlights, child swapping.
   - `DPTableVisualizer`: 1D and 2D dynamic programming grids, active cell computation, source dependency highlighting.
   - `GraphVisualizer`: Node-edge network, unvisited/visiting/visited states, BFS queue and DFS stack tracking.

4. **Scalability Engine for 3,000+ Problems**:
   - **Problem Catalog**: Categorized index of algorithmic patterns and problem numbers.
   - **Scaffolder CLI**: Automatically generate ready-to-code solution classes and visual hook bindings:
     ```bash
     run.bat --new <id> "<title>" "<category>" <difficulty>
     ```

---

## 🚀 Quick Start

### 1. Build the Project
Open PowerShell or Command Prompt in this folder:
```cmd
build.bat
```
*(Or compile manually: `javac -d bin src/com/leetcode/**/*.java`)*

---

### 2. Launch Interactive Web Visualizer
```cmd
run.bat
```
Open your browser at:
👉 **[http://localhost:8080](http://localhost:8080)**

Select any problem from the dropdown (Two Sum, Container With Most Water, Invert Binary Tree, Coin Change, etc.) and click **Play** or drag the timeline slider!

---

### 3. Run in Terminal Animation Mode
Run step-by-step animations directly in your terminal:
```cmd
# LC 1: Two Sum (Hash Map & Array)
run.bat --cli --id 1

# LC 11: Container With Most Water (Two Pointers)
run.bat --cli --id 11

# LC 3: Longest Substring Without Repeating Characters (Sliding Window)
run.bat --cli --id 3

# LC 206: Reverse Linked List (Pointer Redirection)
run.bat --cli --id 206

# LC 226: Invert Binary Tree (Tree Recursion)
run.bat --cli --id 226

# LC 322: Coin Change (Dynamic Programming 2D Table)
run.bat --cli --id 322

# LC 200: Number of Islands (BFS / Graph Traversal)
run.bat --cli --id 200
```

---

### 4. Scaffold a New Problem Template
To add any new LeetCode question from the 3,000+ library:
```cmd
run.bat --new 15 "Three Sum" "Two Pointers" Medium
```
This automatically creates `src/com/leetcode/solutions/arrays/LC0015_ThreeSum.java` pre-configured with the `ArrayVisualizer` and `step(...)` narration hooks!

---

## 📂 Project Architecture

```
leetcode-dsa-visualizer-java/
├── build.bat                 # One-click compilation script
├── run.bat                   # App launcher (Web server or CLI mode)
├── README.md                 # Complete documentation
├── bin/                      # Compiled class files (.class)
└── src/
    ├── com/leetcode/
    │   ├── Main.java         # CLI & Server entry point
    │   ├── visualizer/
    │   │   ├── core/         # Event bus, frame snapshots, timeline & JSON serializer
    │   │   │   ├── VisualEvent.java
    │   │   │   ├── FrameSnapshot.java
    │   │   │   ├── Timeline.java
    │   │   │   └── Visualizer.java
    │   │   ├── primitives/   # General-purpose DSA visual primitives
    │   │   │   ├── ArrayVisualizer.java
    │   │   │   ├── LinkedListVisualizer.java
    │   │   │   ├── TreeVisualizer.java
    │   │   │   ├── DPTableVisualizer.java
    │   │   │   └── GraphVisualizer.java
    │   │   └── renderer/     # Terminal ANSI & Embedded Web Server
    │   │       ├── TerminalRenderer.java
    │   │       └── WebRenderer.java
    │   ├── generator/        # Scalability engine for 3000+ problems
    │   │   ├── ProblemCatalog.java
    │   │   └── ProblemScaffolder.java
    │   └── solutions/        # Curated pattern solutions
    │       ├── BaseSolution.java
    │       ├── arrays/
    │       │   ├── LC0001_TwoSum.java
    │       │   └── LC0011_ContainerWithMostWater.java
    │       ├── slidingwindow/
    │       │   └── LC0003_LongestSubstringWithoutRepeating.java
    │       ├── linkedlist/
    │       │   └── LC0206_ReverseLinkedList.java
    │       ├── trees/
    │       │   └── LC0226_InvertBinaryTree.java
    │       ├── dp/
    │       │   └── LC0322_CoinChange.java
    │       └── graphs/
    │           └── LC0200_NumberOfIslands.java
    └── web/                  # Web dashboard UI assets
        ├── index.html        # HTML layout & player controls
        ├── style.css         # Dark theme styling
        └── visualizer.js     # Responsive SVG animator & timeline scrub engine
```

---

## 💡 How to Add a New Solution with Visualizer Hooks

Creating a visualizer solution takes just a few lines of code:

```java
public class MySolution extends BaseSolution {
    @Override public int getProblemId() { return 167; }
    @Override public String getProblemTitle() { return "Two Sum II"; }
    @Override public String getCategory() { return "Two Pointers"; }
    @Override public String getDifficulty() { return "Medium"; }
    @Override public String getCodeSource() { return "... java code ..."; }

    @Override
    public Timeline run() {
        int[] nums = {2, 7, 11, 15};
        Visualizer viz = new Visualizer(167, "Two Sum II", getCodeSource());
        ArrayVisualizer arrayViz = new ArrayVisualizer(viz, nums);

        int left = 0, right = nums.length - 1;
        arrayViz.setPointer("L", left);
        arrayViz.setPointer("R", right);
        viz.step("Initialize two pointers at ends of array.", 2);

        // Algorithmic steps...
        arrayViz.highlight(left, "match");
        viz.step("Found target sum!", 8);

        return viz.getTimeline();
    }
}
```
Register the solution in `ProblemCatalog.java`, and it is immediately interactive in both the Web UI and Terminal mode!
