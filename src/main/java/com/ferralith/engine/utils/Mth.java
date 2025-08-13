package com.ferralith.engine.utils;

import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.cos;

public class Mth {
    private static final double DEG_TO_RAD = Math.PI/180;
    private static final double RAD_TO_DEG = 180/Math.PI;

    static int[][] MARCHING_SQUARES = {
            {},       // 0
            {4, 3},   // 1
            {3, 2},   // 2
            {4, 2},   // 3
            {1, 2},   // 4
            {1, 4, 3, 2}, // 5
            {1, 3},   // 6
            {1, 4},   // 7
            {1, 4},   // 8
            {1, 3},   // 9
            {1, 2, 3, 4}, // 10
            {1, 2},   // 11
            {4, 2},   // 12
            {3, 2},   // 13
            {4, 3},   // 14
            {}        // 15
    };

    public static Vector2f rotate(Vector2f point, Vector2f pivot, float angleInDegrees) {
        float angleInRagians = (float) (angleInDegrees * DEG_TO_RAD);

        Vector2f p = point.sub(pivot);

        float s = (float) Math.sin(angleInRagians);
        float c = (float) cos(angleInRagians);

        float xnew = p.x * c - p.y * s;
        float ynew = p.x * s + p.y * c;

        point.x = xnew + pivot.x;
        point.y = ynew + pivot.y;

        return point;
    }

    public static List<Vector2f> marchingSquares(boolean[][] pixels) {
        Map<Vector2f, List<Vector2f>> result1 = new HashMap<>();

        //List<Vector2f> result = new ArrayList<>();        //  commented code is old one, it works, but traceContour needs to de done

        for (int y = 0; y < pixels.length; y++) {
            for (int x = 0; x < pixels[0].length; x++) {
                int p1 = pixels[y][x]     ? 1 : 0; // top-left
                int p2 = 0;                         // top-right
                if (x + 1 < pixels[0].length) p2 = pixels[y][x+1] ? 1 : 0;
                int p3 = 0; // bottom-right
                if (x + 1 < pixels[0].length && y + 1 < pixels.length) p3 = pixels[y+1][x+1] ? 1 : 0;
                int p4 = 0; // bottom-left
                if (y + 1 < pixels.length) p4 = pixels[y+1][x]   ? 1 : 0;

                int code = (p1 << 3) | (p2 << 2) | (p3 << 1) | p4;

                int[] edges = MARCHING_SQUARES[code];
                for (int i = 0; i < edges.length; i+=2) {
                    Vector2f a = edgeToPoint(edges[i], x, y);
                    Vector2f b = edgeToPoint(edges[i + 1], x, y);

                    result1.computeIfAbsent(a, k -> new ArrayList<>()).add(b);
                    result1.computeIfAbsent(b, k -> new ArrayList<>()).add(a);
                }
//                for (int edge : edges) {
//                    switch (edge) {
//                        case 1: result.add(new Vector2f(x + 0.5f, y)); break;
//                        case 2: result.add(new Vector2f(x + 1,     y + 0.5f)); break;
//                        case 3: result.add(new Vector2f(x + 0.5f, y + 1)); break;
//                        case 4: result.add(new Vector2f(x,         y + 0.5f)); break;
//                    }
//                }
            }
        }


        return traceContour(result1);
    }

    public static List<Vector2f> old_marchingSquares(boolean[][] pixels) {
        List<Vector2f> result = new ArrayList<>();

        for (int y = 0; y < pixels.length; y++) {
            for (int x = 0; x < pixels[0].length; x++) {
                int p1 = pixels[y][x]     ? 1 : 0; // top-left
                int p2 = 0;                         // top-right
                if (x + 1 < pixels[0].length) p2 = pixels[y][x+1] ? 1 : 0;
                int p3 = 0; // bottom-right
                if (x + 1 < pixels[0].length && y + 1 < pixels.length) p3 = pixels[y+1][x+1] ? 1 : 0;
                int p4 = 0; // bottom-left
                if (y + 1 < pixels.length) p4 = pixels[y+1][x]   ? 1 : 0;

                int code = (p1 << 3) | (p2 << 2) | (p3 << 1) | p4;

                int[] edges = MARCHING_SQUARES[code];
                for (int edge : edges) {
                    switch (edge) {
                        case 1: result.add(new Vector2f(x + 0.5f, y)); break;
                        case 2: result.add(new Vector2f(x + 1,     y + 0.5f)); break;
                        case 3: result.add(new Vector2f(x + 0.5f, y + 1)); break;
                        case 4: result.add(new Vector2f(x,         y + 0.5f)); break;
                    }
                }
            }
        }


        return result;
    }

    private static Vector2f edgeToPoint(int edge, int x, int y) {
        return switch (edge) {
            case 1 -> new Vector2f(x + 0.5f, y);            // top
            case 2 -> new Vector2f(x + 1, y + 0.5f);     // right
            case 3 -> new Vector2f(x + 0.5f, y + 1);     // bottom
            case 4 -> new Vector2f(x, y + 0.5f);            // left
            default -> null;
        };
    }

    public static List<Vector2f> traceContour(Map<Vector2f, List<Vector2f>> graph) {
        List<Vector2f> contour = new ArrayList<>();
        if (graph.isEmpty()) return contour;

        Vector2f start = graph.keySet().iterator().next();
        Vector2f current = start;
        Vector2f prev = null;

        int safetyCounter = 0;
        int maxSteps = 10000;

        do {
            contour.add(current);
            List<Vector2f> neighbors = graph.get(current);
            if (neighbors == null || neighbors.isEmpty()) break;

            Vector2f next = null;
            for (Vector2f neighbor : neighbors) {
                if (prev == null || !neighbor.equals(prev)) {
                    next = neighbor;
                    break;
                }
            }

            if (next == null) break;

            prev = current;
            current = next;

            safetyCounter++;
            if (safetyCounter > maxSteps) {
                System.err.println("Contour tracing stopped by safety counter");
                break;
            }
        } while (!isVectorEqual(current, start));

        return contour;
    }

    private static boolean isVectorEqual(Vector2f a, Vector2f b) {
        float epsilon = 0.001f;
        return Math.abs(a.x - b.x) < epsilon && Math.abs(a.y - b.y) < epsilon;
    }

    public static double angle (double x1, double y1, double x2, double y2) {
        double xdiff = x1 - x2;
        double ydiff = y1 - y2;
        //double tan = xdiff / ydiff;
        double atan = Math.atan2(ydiff, xdiff);
        return atan;

    }


    // O(N^2)
    public static List<Vector2f> DouglasPeucker(List<Vector2f> points, double epsilon) {
        if (points.size() < 3) return new ArrayList<>(points);

        double dmax = 0;
        int index = 0;
        int end = points.size();

        for (int i = 1; i < end - 1; i++) {
            double d = perpendicularDistance(points.get(i), points.get(0), points.get(end - 1));
            if (d > dmax) {
                index = i;
                dmax = d;
            }
        }

        List<Vector2f> result = new ArrayList<>();

        if (dmax > epsilon) {
            List<Vector2f> recResult1 = DouglasPeucker(points.subList(0, index + 1), epsilon);
            List<Vector2f> recResult2 = DouglasPeucker(points.subList(index, end), epsilon);

            result.addAll(recResult1);
            // removing point duplicate
            result.remove(result.size() - 1);
            result.addAll(recResult2);
        } else {
            result.add(points.get(0));
            result.add(points.get(end - 1));
        }

        return result;
    }


    private static double perpendicularDistance(Vector2f point, Vector2f line_start, Vector2f line_end) {
        double A = point.x - line_start.x;
        double B = point.y - line_start.y;
        double C = line_end.x - line_start.x;
        double D = line_end.y - line_start.y;

        double dot = A * C + B * D;
        double len_sq = C * C + D * D;
        double param = -1;
        if (len_sq != 0) {
            param = dot/len_sq;
        }

        double xx, yy;

        if (param < 0) {
            xx = line_start.x;
            yy = line_start.y;
        } else if (param > 1) {
            xx = line_end.x;
            yy = line_end.y;
        } else {
            xx = line_start.x + param * C;
            yy = line_start.y + param * D;
        }

        double dx = point.x - xx;
        double dy = point.y - yy;
        return Math.sqrt(dx * dx + dy * dy);
    }


}
