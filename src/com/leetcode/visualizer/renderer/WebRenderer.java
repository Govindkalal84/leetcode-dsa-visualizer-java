package com.leetcode.visualizer.renderer;

import com.leetcode.generator.ProblemCatalog;
import com.leetcode.solutions.BaseSolution;
import com.leetcode.visualizer.core.Timeline;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

/**
 * Zero-dependency embedded web server that hosts the interactive browser visualizer.
 */
public class WebRenderer {
    private final int port;
    private final File webRootDir;

    public WebRenderer(int port, File webRootDir) {
        this.port = port;
        this.webRootDir = webRootDir;
    }

    public void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // Static files
        server.createContext("/", new StaticFileHandler(webRootDir));

        // API: List problems
        server.createContext("/api/problems", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                    sendCors(exchange);
                    return;
                }
                String json = ProblemCatalog.getProblemsJson();
                sendResponse(exchange, 200, "application/json; charset=UTF-8", json);
            }
        });

        // API: Run algorithm & return timeline JSON
        server.createContext("/api/timeline", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                    sendCors(exchange);
                    return;
                }

                try {
                    String query = exchange.getRequestURI().getQuery();
                    int problemId = 1; // default Two Sum
                    if (query != null && query.contains("id=")) {
                        try {
                            String idStr = query.split("id=")[1].split("&")[0];
                            problemId = Integer.parseInt(idStr);
                        } catch (Exception ignored) {}
                    }

                    BaseSolution solution = ProblemCatalog.getSolutionInstance(problemId);
                    if (solution == null) {
                        sendResponse(exchange, 404, "application/json", "{\"error\":\"Problem not found\"}");
                        return;
                    }

                    Timeline timeline = solution.run();
                    String json = timeline.toJson();
                    sendResponse(exchange, 200, "application/json; charset=UTF-8", json);
                } catch (Throwable t) {
                    t.printStackTrace();
                    sendResponse(exchange, 500, "application/json", "{\"error\":\"" + t.getMessage() + "\"}");
                }
            }
        });

        server.setExecutor(null);
        server.start();
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🚀 Web Visualizer is live at: http://localhost:" + port);
        System.out.println("   Open this URL in your browser to interact with the visualizations!");
        System.out.println("=".repeat(60) + "\n");
    }

    private static void sendCors(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
        exchange.sendResponseHeaders(204, -1);
    }

    private static void sendResponse(HttpExchange exchange, int status, String contentType, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    static class StaticFileHandler implements HttpHandler {
        private final File baseDir;

        public StaticFileHandler(File baseDir) {
            this.baseDir = baseDir;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            File file = new File(baseDir, path.replace("/", File.separator));
            if (path.equals("/") || path.isEmpty()) {
                File animFile = new File(baseDir, "animation_vs_dsa.html");
                if (animFile.exists()) {
                    file = animFile;
                    path = "/animation_vs_dsa.html";
                } else {
                    file = new File(baseDir, "index.html");
                    path = "/index.html";
                }
            }

            if (!file.exists() || file.isDirectory()) {
                sendResponse(exchange, 404, "text/plain", "404 Not Found");
                return;
            }

            String contentType = "text/plain";
            if (path.endsWith(".html")) contentType = "text/html; charset=UTF-8";
            else if (path.endsWith(".css")) contentType = "text/css; charset=UTF-8";
            else if (path.endsWith(".js")) contentType = "application/javascript; charset=UTF-8";
            else if (path.endsWith(".svg")) contentType = "image/svg+xml";

            byte[] fileBytes = Files.readAllBytes(file.toPath());
            exchange.getResponseHeaders().set("Content-Type", contentType);
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().set("Cache-Control", "no-cache, no-store, must-revalidate, max-age=0");
            exchange.getResponseHeaders().set("Pragma", "no-cache");
            exchange.getResponseHeaders().set("Expires", "0");
            exchange.sendResponseHeaders(200, fileBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(fileBytes);
            }
        }
    }
}
