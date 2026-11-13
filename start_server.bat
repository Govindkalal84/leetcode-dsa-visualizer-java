@echo off
cd /d "%~dp0"
title LeetCode DSA Visualizer Server
echo ============================================================
echo Starting LeetCode DSA Visualizer Server on port 8080...
echo ============================================================
timeout /t 1 >nul
start "" "http://localhost:8080/animation_vs_dsa.html"
java -cp bin com.leetcode.Main --port 8080
pause
