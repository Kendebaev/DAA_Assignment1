import java.util.Arrays;
import java.util.Comparator;

public class ClosestPP {

    // define a simple structure for a Point
    static class Point {
        double x, y;
        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }

    // class to hold the result (distance and the two points)
    static class Result {
        double minDistance;
        Point p1, p2;

        public Result(double dist, Point p1, Point p2) {
            this.minDistance = dist;
            this.p1 = p1;
            this.p2 = p2;
        }
    }

    // comparator to sort points by X-coordinate
    private static final Comparator<Point> X_ORDER = Comparator.comparingDouble(p -> p.x);
    // comparator to sort points by Y-coordinate
    private static final Comparator<Point> Y_ORDER = Comparator.comparingDouble(p -> p.y);


    private static double distance(Point p1, Point p2) {
        double dx = p1.x - p2.x;
        double dy = p1.y - p2.y;
        return Math.sqrt(dx * dx + dy * dy);
    }


    private static Result closestPair(Point[] pX, Point[] pY) {
        int n = pX.length;

        // base Case (Brute Force for small n, e.g., n <= 3)
        if (n <= 3) {
            double minDist = Double.POSITIVE_INFINITY;
            Point p1 = null, p2 = null;
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    double dist = distance(pX[i], pX[j]);
                    if (dist < minDist) {
                        minDist = dist;
                        p1 = pX[i];
                        p2 = pX[j];
                    }
                }
            }
            return new Result(minDist, p1, p2);
        }

        //divide: Split P into two halves, Q and R.
        int mid = n / 2;
        Point[] qX = Arrays.copyOfRange(pX, 0, mid);
        Point[] rX = Arrays.copyOfRange(pX, mid, n);


        Point medianPoint = pX[mid - 1];


        Point[] qY = new Point[mid];
        Point[] rY = new Point[n - mid];
        int qYidx = 0, rYidx = 0;

        for (Point p : pY) {

            if (p.x <= medianPoint.x && qYidx < mid) {
                qY[qYidx++] = p;
            } else {
                rY[rYidx++] = p;
            }
        }


        // conquer: Recursively find the closest pair in Q and R.
        Result leftResult = closestPair(qX, qY);
        Result rightResult = closestPair(rX, rY);

        // Current minimum distance found so far (delta)
        Result bestResult = (leftResult.minDistance < rightResult.minDistance) ? leftResult : rightResult;
        double delta = bestResult.minDistance;

        // combine (Strip Check): Find the closest pair spanning the dividing line.

        Point[] strip = new Point[n];
        int stripSize = 0;

        double midX = pX[mid].x; // X-coordinate of the dividing line

        for (Point p : pY) {
            if (Math.abs(p.x - midX) < delta) {
                strip[stripSize++] = p;
            }
        }


        Result stripResult = closestStripPair(strip, stripSize, delta);

        // Final result: the minimum of the three possibilities
        return (bestResult.minDistance < stripResult.minDistance) ? bestResult : stripResult;
    }


    private static Result closestStripPair(Point[] strip, int stripSize, double delta) {
        double minDist = delta;
        Point p1 = null, p2 = null;

        // The key optimization: For each point, only check the next 7-8 neighbors
        // because points further away cannot possibly be closer than 'delta'.
        for (int i = 0; i < stripSize; i++) {
            // j starts at i+1 and stops after 7 comparisons or when the strip ends
            for (int j = i + 1; j < stripSize && (strip[j].y - strip[i].y) < minDist; j++) {
                double dist = distance(strip[i], strip[j]);
                if (dist < minDist) {
                    minDist = dist;
                    p1 = strip[i];
                    p2 = strip[j];
                }
            }
        }
        return new Result(minDist, p1, p2);
    }


    public static Result findClosestPair(Point[] points) {
        // 1. Initial Sort: O(n log n)
        Point[] pX = Arrays.copyOf(points, points.length);
        Point[] pY = Arrays.copyOf(points, points.length);

        Arrays.sort(pX, X_ORDER);
        Arrays.sort(pY, Y_ORDER);

        return closestPair(pX, pY);
    }

    public static void main(String[] args) {
        Point[] points = {
                new Point(2, 3), new Point(12, 30), new Point(40, 50), new Point(5, 1),
                new Point(12, 10), new Point(3, 4), new Point(8, 6), new Point(1, 1)
        };

        long startTime = System.nanoTime();
        Result result = findClosestPair(points);
        long endTime = System.nanoTime();

        System.out.println("--- Closest Pair of Points ---");
        System.out.println("Total Points: " + points.length);
        System.out.println("Closest Pair: " + result.p1 + " and " + result.p2);
        System.out.println("Min Distance: " + result.minDistance);
        System.out.println("\n--- Performance ---");
        System.out.println("Running Time: O(n log n)");
        System.out.println("Execution Time: " + (endTime - startTime) / 1_000_000.0 + " ms");
    }
}