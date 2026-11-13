// LeetCode DSA Visualizer Front-End Client

let problems = [];
let currentTimeline = null;
let currentFrameIndex = 0;
let isPlaying = false;
let playTimer = null;

// DOM Elements
const problemSelect = document.getElementById("problemSelect");
const difficultyBadge = document.getElementById("difficultyBadge");
const categoryBadge = document.getElementById("categoryBadge");
const componentTypeTag = document.getElementById("componentTypeTag");
const visualSvg = document.getElementById("visualSvg");
const narrationText = document.getElementById("narrationText");
const varsBody = document.getElementById("varsBody");
const codeBlock = document.getElementById("codeBlock");
const timelineSlider = document.getElementById("timelineSlider");
const frameCounter = document.getElementById("frameCounter");
const btnPlay = document.getElementById("btnPlay");
const btnPrev = document.getElementById("btnPrev");
const btnNext = document.getElementById("btnNext");
const btnReset = document.getElementById("btnReset");
const speedSelect = document.getElementById("speedSelect");

// Initialize
window.addEventListener("DOMContentLoaded", async () => {
    setupEventListeners();
    await loadProblems();
});

function setupEventListeners() {
    problemSelect.addEventListener("change", (e) => {
        loadProblemTimeline(e.target.value);
    });

    btnPlay.addEventListener("click", togglePlay);
    btnPrev.addEventListener("click", () => step(-1));
    btnNext.addEventListener("click", () => step(1));
    btnReset.addEventListener("click", () => goToFrame(0));

    timelineSlider.addEventListener("input", (e) => {
        goToFrame(parseInt(e.target.value, 10));
    });

    speedSelect.addEventListener("change", () => {
        if (isPlaying) {
            pause();
            play();
        }
    });
}

async function loadProblems() {
    try {
        const res = await fetch("/api/problems");
        problems = await res.json();

        problemSelect.innerHTML = "";
        problems.forEach(p => {
            const opt = document.createElement("option");
            opt.value = p.id;
            opt.textContent = `LC ${p.id}: ${p.title}`;
            problemSelect.appendChild(opt);
        });

        if (problems.length > 0) {
            await loadProblemTimeline(problems[0].id);
        }
    } catch (err) {
        console.error("Failed to load problem list:", err);
        narrationText.textContent = "Error connecting to Java visualizer backend.";
    }
}

async function loadProblemTimeline(problemId) {
    pause();
    const meta = problems.find(p => p.id == problemId);
    if (meta) {
        difficultyBadge.textContent = meta.difficulty;
        difficultyBadge.className = `badge ${meta.difficulty}`;
        categoryBadge.textContent = meta.category;
    }

    try {
        const res = await fetch(`/api/timeline?id=${problemId}`);
        currentTimeline = await res.json();

        timelineSlider.max = Math.max(0, currentTimeline.frames.length - 1);
        renderSourceCode(currentTimeline.codeSource);
        goToFrame(0);
    } catch (err) {
        console.error("Failed to load timeline for problem", problemId, err);
    }
}

function renderSourceCode(codeText) {
    if (!codeText) {
        codeBlock.innerHTML = "";
        return;
    }
    const lines = codeText.split("\n");
    codeBlock.innerHTML = lines.map((line, idx) => {
        const lineNum = idx + 1;
        return `<span class="code-line" id="line-${lineNum}"><span style="color:#64748b; margin-right: 12px;">${String(lineNum).padStart(2, ' ')}</span>${escapeHtml(line)}</span>`;
    }).join("");
}

function escapeHtml(str) {
    return str.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;");
}

function togglePlay() {
    if (isPlaying) {
        pause();
    } else {
        play();
    }
}

function play() {
    if (!currentTimeline || currentTimeline.frames.length === 0) return;
    if (currentFrameIndex >= currentTimeline.frames.length - 1) {
        currentFrameIndex = 0;
    }
    isPlaying = true;
    btnPlay.textContent = "⏸ Pause";
    btnPlay.style.background = "var(--accent-yellow)";

    const delay = parseInt(speedSelect.value, 10) || 600;
    playTimer = setInterval(() => {
        if (currentFrameIndex < currentTimeline.frames.length - 1) {
            goToFrame(currentFrameIndex + 1);
        } else {
            pause();
        }
    }, delay);
}

function pause() {
    isPlaying = false;
    btnPlay.textContent = "▶ Play";
    btnPlay.style.background = "";
    if (playTimer) {
        clearInterval(playTimer);
        playTimer = null;
    }
}

function step(delta) {
    pause();
    if (!currentTimeline) return;
    const nextIdx = currentFrameIndex + delta;
    if (nextIdx >= 0 && nextIdx < currentTimeline.frames.length) {
        goToFrame(nextIdx);
    }
}

function goToFrame(index) {
    if (!currentTimeline || !currentTimeline.frames[index]) return;
    currentFrameIndex = index;
    timelineSlider.value = index;
    frameCounter.textContent = `Frame ${index + 1} / ${currentTimeline.frames.length}`;

    const frame = currentTimeline.frames[index];
    componentTypeTag.textContent = frame.componentType;
    narrationText.textContent = frame.explanation;

    // Update variables
    varsBody.innerHTML = "";
    const vars = frame.variables || {};
    const varKeys = Object.keys(vars);
    if (varKeys.length === 0) {
        varsBody.innerHTML = "<tr><td colspan='2' style='color:#64748b'>(no variables)</td></tr>";
    } else {
        varKeys.forEach(k => {
            const row = document.createElement("tr");
            row.innerHTML = `<td>${escapeHtml(k)}</td><td>${escapeHtml(String(vars[k]))}</td>`;
            varsBody.appendChild(row);
        });
    }

    // Highlight code line
    document.querySelectorAll(".code-line").forEach(el => el.classList.remove("active-line"));
    if (frame.codeLine > 0) {
        const activeEl = document.getElementById(`line-${frame.codeLine}`);
        if (activeEl) {
            activeEl.classList.add("active-line");
            activeEl.scrollIntoView({ behavior: "smooth", block: "nearest" });
        }
    }

    // Render Data Structure on SVG
    renderVisual(frame);
}

function renderVisual(frame) {
    visualSvg.innerHTML = "";
    const comp = frame.componentType;

    if (comp === "ARRAY") {
        renderArrayVisual(frame);
    } else if (comp === "LINKED_LIST") {
        renderLinkedListVisual(frame);
    } else if (comp === "TREE") {
        renderTreeVisual(frame);
    } else if (comp === "DP_TABLE") {
        renderDPTableVisual(frame);
    } else if (comp === "GRAPH") {
        renderGraphVisual(frame);
    }
}

function renderArrayVisual(frame) {
    const arr = frame.state.array || [];
    const pointers = frame.state.pointers || {};
    const highlights = frame.state.highlights || {};
    const wStart = frame.state.windowStart;
    const wEnd = frame.state.windowEnd;

    const boxW = 60;
    const boxH = 60;
    const spacing = 16;
    const startX = 60;
    const startY = 180;

    // Draw sliding window bracket
    if (wStart !== undefined && wEnd !== undefined && wStart >= 0 && wEnd >= wStart) {
        const winX = startX + wStart * (boxW + spacing) - 8;
        const winW = (wEnd - wStart + 1) * (boxW + spacing) - spacing + 16;
        const rect = document.createElementNS("http://www.w3.org/2000/svg", "rect");
        rect.setAttribute("x", winX);
        rect.setAttribute("y", startY - 14);
        rect.setAttribute("width", winW);
        rect.setAttribute("height", boxH + 28);
        rect.setAttribute("rx", 10);
        rect.setAttribute("fill", "rgba(56, 189, 248, 0.08)");
        rect.setAttribute("stroke", "#38bdf8");
        rect.setAttribute("stroke-dasharray", "6,6");
        rect.setAttribute("stroke-width", "2");
        visualSvg.appendChild(rect);

        // Window label
        const txt = document.createElementNS("http://www.w3.org/2000/svg", "text");
        txt.setAttribute("x", winX + winW / 2);
        txt.setAttribute("y", startY - 22);
        txt.setAttribute("fill", "#38bdf8");
        txt.setAttribute("font-size", "12");
        txt.setAttribute("font-weight", "bold");
        txt.setAttribute("text-anchor", "middle");
        txt.textContent = "Sliding Window";
        visualSvg.appendChild(txt);
    }

    arr.forEach((val, i) => {
        const x = startX + i * (boxW + spacing);
        const y = startY;

        let fill = "#1e293b";
        let stroke = "#334155";
        let textColor = "#f8fafc";

        if (highlights[i]) {
            const h = highlights[i];
            if (h === "match" || h === "success") {
                fill = "rgba(16, 185, 129, 0.25)";
                stroke = "#10b981";
                textColor = "#34d399";
            } else if (h === "duplicate" || h === "fail") {
                fill = "rgba(244, 63, 94, 0.25)";
                stroke = "#f43f5e";
                textColor = "#fb7185";
            } else {
                fill = "rgba(56, 189, 248, 0.25)";
                stroke = "#38bdf8";
                textColor = "#38bdf8";
            }
        }

        // Box
        const rect = document.createElementNS("http://www.w3.org/2000/svg", "rect");
        rect.setAttribute("x", x);
        rect.setAttribute("y", y);
        rect.setAttribute("width", boxW);
        rect.setAttribute("height", boxH);
        rect.setAttribute("rx", 8);
        rect.setAttribute("fill", fill);
        rect.setAttribute("stroke", stroke);
        rect.setAttribute("stroke-width", "2");
        visualSvg.appendChild(rect);

        // Value
        const valText = document.createElementNS("http://www.w3.org/2000/svg", "text");
        valText.setAttribute("x", x + boxW / 2);
        valText.setAttribute("y", y + boxH / 2 + 6);
        valText.setAttribute("fill", textColor);
        valText.setAttribute("font-size", "18");
        valText.setAttribute("font-weight", "bold");
        valText.setAttribute("text-anchor", "middle");
        valText.textContent = typeof val === 'number' && val >= 65 && val <= 122 ? `${val} ('${String.fromCharCode(val)}')` : val;
        visualSvg.appendChild(valText);

        // Index
        const idxText = document.createElementNS("http://www.w3.org/2000/svg", "text");
        idxText.setAttribute("x", x + boxW / 2);
        idxText.setAttribute("y", y + boxH + 24);
        idxText.setAttribute("fill", "#64748b");
        idxText.setAttribute("font-size", "12");
        idxText.setAttribute("text-anchor", "middle");
        idxText.textContent = `[${i}]`;
        visualSvg.appendChild(idxText);

        // Pointers pointing to this index
        const ptrsHere = Object.entries(pointers).filter(([_, idx]) => idx === i).map(([name]) => name);
        if (ptrsHere.length > 0) {
            const ptrGroup = document.createElementNS("http://www.w3.org/2000/svg", "g");

            // Arrow
            const arrow = document.createElementNS("http://www.w3.org/2000/svg", "path");
            arrow.setAttribute("d", `M ${x + boxW / 2} ${y - 10} L ${x + boxW / 2} ${y - 2}`);
            arrow.setAttribute("stroke", "#fbbf24");
            arrow.setAttribute("stroke-width", "3");
            arrow.setAttribute("marker-end", "url(#arrowhead)");
            ptrGroup.appendChild(arrow);

            // Pointer text badge
            const pText = document.createElementNS("http://www.w3.org/2000/svg", "text");
            pText.setAttribute("x", x + boxW / 2);
            pText.setAttribute("y", y - 18);
            pText.setAttribute("fill", "#fbbf24");
            pText.setAttribute("font-size", "13");
            pText.setAttribute("font-weight", "bold");
            pText.setAttribute("text-anchor", "middle");
            pText.textContent = ptrsHere.join(", ");
            ptrGroup.appendChild(pText);

            visualSvg.appendChild(ptrGroup);
        }
    });
}

function renderLinkedListVisual(frame) {
    const nodes = frame.state.nodes || [];
    const pointers = frame.state.pointers || {};
    const headId = frame.state.headId;

    const nodeW = 80;
    const nodeH = 46;
    const gap = 60;
    const startX = 60;
    const startY = 180;

    nodes.forEach((n, idx) => {
        const x = startX + idx * (nodeW + gap);
        const y = startY;

        // Node box
        const rect = document.createElementNS("http://www.w3.org/2000/svg", "rect");
        rect.setAttribute("x", x);
        rect.setAttribute("y", y);
        rect.setAttribute("width", nodeW);
        rect.setAttribute("height", nodeH);
        rect.setAttribute("rx", 10);
        rect.setAttribute("fill", "rgba(16, 185, 129, 0.15)");
        rect.setAttribute("stroke", "#10b981");
        rect.setAttribute("stroke-width", "2");
        visualSvg.appendChild(rect);

        // Value
        const txt = document.createElementNS("http://www.w3.org/2000/svg", "text");
        txt.setAttribute("x", x + nodeW / 2);
        txt.setAttribute("y", y + nodeH / 2 + 5);
        txt.setAttribute("fill", "#34d399");
        txt.setAttribute("font-size", "16");
        txt.setAttribute("font-weight", "bold");
        txt.setAttribute("text-anchor", "middle");
        txt.textContent = `Val: ${n.val}`;
        visualSvg.appendChild(txt);

        // Arrow to next node
        if (n.nextId !== -1) {
            const line = document.createElementNS("http://www.w3.org/2000/svg", "line");
            line.setAttribute("x1", x + nodeW);
            line.setAttribute("y1", y + nodeH / 2);
            line.setAttribute("x2", x + nodeW + gap - 8);
            line.setAttribute("y2", y + nodeH / 2);
            line.setAttribute("stroke", "#38bdf8");
            line.setAttribute("stroke-width", "2.5");
            visualSvg.appendChild(line);
        } else {
            const nullTxt = document.createElementNS("http://www.w3.org/2000/svg", "text");
            nullTxt.setAttribute("x", x + nodeW + 28);
            nullTxt.setAttribute("y", y + nodeH / 2 + 5);
            nullTxt.setAttribute("fill", "#f43f5e");
            nullTxt.setAttribute("font-size", "13");
            nullTxt.setAttribute("font-weight", "bold");
            nullTxt.textContent = "NULL";
            visualSvg.appendChild(nullTxt);
        }

        // Pointers
        const ptrsHere = Object.entries(pointers).filter(([_, id]) => id === n.id).map(([name]) => name);
        if (ptrsHere.length > 0) {
            const pText = document.createElementNS("http://www.w3.org/2000/svg", "text");
            pText.setAttribute("x", x + nodeW / 2);
            pText.setAttribute("y", y - 14);
            pText.setAttribute("fill", "#fbbf24");
            pText.setAttribute("font-size", "13");
            pText.setAttribute("font-weight", "bold");
            pText.setAttribute("text-anchor", "middle");
            pText.textContent = ptrsHere.join(", ");
            visualSvg.appendChild(pText);
        }
    });
}

function renderTreeVisual(frame) {
    const nodes = frame.state.nodes || [];
    const rootId = frame.state.rootId;
    const activeNodeId = frame.state.activeNodeId;

    if (nodes.length === 0) return;

    // Node lookup
    const map = {};
    nodes.forEach(n => map[n.id] = n);

    // Coordinate positions
    const pos = {
        0: { x: 300, y: 60 },   // root (4)
        1: { x: 180, y: 150 },  // left (2)
        2: { x: 420, y: 150 },  // right (7)
        3: { x: 120, y: 240 },  // (1)
        4: { x: 240, y: 240 },  // (3)
        5: { x: 360, y: 240 },  // (6)
        6: { x: 480, y: 240 }   // (9)
    };

    // Draw branch lines
    nodes.forEach(n => {
        const p1 = pos[n.id];
        if (!p1) return;
        if (n.leftId !== -1 && pos[n.leftId]) {
            drawLine(p1.x, p1.y, pos[n.leftId].x, pos[n.leftId].y);
        }
        if (n.rightId !== -1 && pos[n.rightId]) {
            drawLine(p1.x, p1.y, pos[n.rightId].x, pos[n.rightId].y);
        }
    });

    // Draw nodes
    nodes.forEach(n => {
        const p = pos[n.id];
        if (!p) return;
        const isActive = activeNodeId === n.id;

        const circle = document.createElementNS("http://www.w3.org/2000/svg", "circle");
        circle.setAttribute("cx", p.x);
        circle.setAttribute("cy", p.y);
        circle.setAttribute("r", "22");
        circle.setAttribute("fill", isActive ? "rgba(16, 185, 129, 0.4)" : "#1e293b");
        circle.setAttribute("stroke", isActive ? "#10b981" : "#38bdf8");
        circle.setAttribute("stroke-width", isActive ? "3.5" : "2");
        visualSvg.appendChild(circle);

        const txt = document.createElementNS("http://www.w3.org/2000/svg", "text");
        txt.setAttribute("x", p.x);
        txt.setAttribute("y", p.y + 5);
        txt.setAttribute("fill", isActive ? "#34d399" : "#f8fafc");
        txt.setAttribute("font-size", "15");
        txt.setAttribute("font-weight", "bold");
        txt.setAttribute("text-anchor", "middle");
        txt.textContent = n.val;
        visualSvg.appendChild(txt);
    });

    function drawLine(x1, y1, x2, y2) {
        const line = document.createElementNS("http://www.w3.org/2000/svg", "line");
        line.setAttribute("x1", x1);
        line.setAttribute("y1", y1);
        line.setAttribute("x2", x2);
        line.setAttribute("y2", y2);
        line.setAttribute("stroke", "#334155");
        line.setAttribute("stroke-width", "2");
        visualSvg.appendChild(line);
    }
}

function renderDPTableVisual(frame) {
    const table = frame.state.table || [];
    const colLabels = frame.state.colLabels || [];
    const rowLabels = frame.state.rowLabels || [];
    const activeR = frame.state.activeRow;
    const activeC = frame.state.activeCol;
    const deps = frame.state.dependencies || [];

    const cellW = 55;
    const cellH = 45;
    const startX = 100;
    const startY = 120;

    // Col labels
    colLabels.forEach((cl, c) => {
        const txt = document.createElementNS("http://www.w3.org/2000/svg", "text");
        txt.setAttribute("x", startX + c * cellW + cellW / 2);
        txt.setAttribute("y", startY - 14);
        txt.setAttribute("fill", "#94a3b8");
        txt.setAttribute("font-size", "13");
        txt.setAttribute("font-weight", "bold");
        txt.setAttribute("text-anchor", "middle");
        txt.textContent = cl;
        visualSvg.appendChild(txt);
    });

    // Table rows
    table.forEach((row, r) => {
        // Row label
        const rTxt = document.createElementNS("http://www.w3.org/2000/svg", "text");
        rTxt.setAttribute("x", startX - 20);
        rTxt.setAttribute("y", startY + r * cellH + cellH / 2 + 5);
        rTxt.setAttribute("fill", "#94a3b8");
        rTxt.setAttribute("font-size", "13");
        rTxt.setAttribute("font-weight", "bold");
        rTxt.setAttribute("text-anchor", "end");
        rTxt.textContent = rowLabels[r] || `R${r}`;
        visualSvg.appendChild(rTxt);

        row.forEach((val, c) => {
            const x = startX + c * cellW;
            const y = startY + r * cellH;
            const isActive = activeR === r && activeC === c;
            const isDep = deps.some(d => d[0] === r && d[1] === c);

            let fill = "#1e293b";
            let stroke = "#334155";
            let textColor = "#f8fafc";

            if (isActive) {
                fill = "rgba(16, 185, 129, 0.35)";
                stroke = "#10b981";
                textColor = "#34d399";
            } else if (isDep) {
                fill = "rgba(251, 191, 36, 0.25)";
                stroke = "#fbbf24";
                textColor = "#fbbf24";
            }

            const rect = document.createElementNS("http://www.w3.org/2000/svg", "rect");
            rect.setAttribute("x", x);
            rect.setAttribute("y", y);
            rect.setAttribute("width", cellW);
            rect.setAttribute("height", cellH);
            rect.setAttribute("fill", fill);
            rect.setAttribute("stroke", stroke);
            rect.setAttribute("stroke-width", "1.5");
            visualSvg.appendChild(rect);

            const txt = document.createElementNS("http://www.w3.org/2000/svg", "text");
            txt.setAttribute("x", x + cellW / 2);
            txt.setAttribute("y", y + cellH / 2 + 5);
            txt.setAttribute("fill", textColor);
            txt.setAttribute("font-size", "14");
            txt.setAttribute("font-weight", "bold");
            txt.setAttribute("text-anchor", "middle");
            txt.textContent = val === 99 ? "∞" : val;
            visualSvg.appendChild(txt);
        });
    });
}

function renderGraphVisual(frame) {
    const nodes = frame.state.nodes || [];
    const queueOrStack = frame.state.queueOrStack || [];
    const active = frame.state.activeNodeId;

    const startX = 120;
    const startY = 80;
    const spacing = 110;

    nodes.forEach((n, idx) => {
        const row = Math.floor(idx / 3);
        const col = idx % 3;
        const x = startX + col * spacing;
        const y = startY + row * spacing;

        const isActive = n.id === active;
        const isVisited = n.state === "VISITED";

        const circle = document.createElementNS("http://www.w3.org/2000/svg", "circle");
        circle.setAttribute("cx", x);
        circle.setAttribute("cy", y);
        circle.setAttribute("r", "28");
        circle.setAttribute("fill", isActive ? "rgba(16, 185, 129, 0.4)" : (isVisited ? "rgba(56, 189, 248, 0.2)" : "#1e293b"));
        circle.setAttribute("stroke", isActive ? "#10b981" : (isVisited ? "#38bdf8" : "#334155"));
        circle.setAttribute("stroke-width", isActive ? "3" : "2");
        visualSvg.appendChild(circle);

        const txt = document.createElementNS("http://www.w3.org/2000/svg", "text");
        txt.setAttribute("x", x);
        txt.setAttribute("y", y + 4);
        txt.setAttribute("fill", "#f8fafc");
        txt.setAttribute("font-size", "12");
        txt.setAttribute("font-weight", "bold");
        txt.setAttribute("text-anchor", "middle");
        txt.textContent = n.label || n.id;
        visualSvg.appendChild(txt);
    });

    // Queue display
    if (queueOrStack.length > 0) {
        const qTxt = document.createElementNS("http://www.w3.org/2000/svg", "text");
        qTxt.setAttribute("x", startX);
        qTxt.setAttribute("y", startY + 3 * spacing);
        qTxt.setAttribute("fill", "#fbbf24");
        qTxt.setAttribute("font-size", "14");
        qTxt.setAttribute("font-weight", "bold");
        qTxt.textContent = `BFS Queue: [ ${queueOrStack.join(", ")} ]`;
        visualSvg.appendChild(qTxt);
    }
}
