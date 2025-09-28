import java.util.Arrays;

/**
 * Implements the MergeSort algorithm with optimizations for performance analysis.
 * Features:
 * - Small-n cutoff using Insertion Sort.
 * - Reusable buffer to avoid repeated memory allocation.
 * - Metric counters for comparisons and recursion depth.
 * - Time measurement using System.nanoTime().
 */
public class MergeSort {

    // A small-n cutoff value. Insertion sort is faster for very small arrays.
    private static final int CUTOFF = 7;

    // Static variables to track performance metrics across recursive calls.
    private static long comparisons = 0;
    private static int maxRecursionDepth = 0;

    /**
     * Public-facing method to start the MergeSort process.
     * Initializes the temporary buffer and resets metric counters.
     * @param arr The array to be sorted.
     */
    public static void sort(double[] arr) {
        double[] temp = new double[arr.length];
        // Reset counters for a new sort operation
        comparisons = 0;
        maxRecursionDepth = 0;
        mergeSort(arr, temp, 0, arr.length - 1, 0);
    }

    /**
     * The recursive helper method for the divide-and-conquer strategy.
     * @param arr The array to be sorted.
     * @param temp A temporary buffer for merging.
     * @param low The starting index of the sub-array.
     * @param high The ending index of the sub-array.
     * @param currentDepth The current recursion depth.
     */
    private static void mergeSort(double[] arr, double[] temp, int low, int high, int currentDepth) {
        // Track the maximum recursion depth reached.
        if (currentDepth > maxRecursionDepth) {
            maxRecursionDepth = currentDepth;
        }

        // Base case: use insertion sort for small sub-arrays to reduce overhead.
        if (high <= low + CUTOFF) {
            insertionSort(arr, low, high);
            return;
        }

        // Find the midpoint to divide the array.
        int mid = low + (high - low) / 2;

        // Recursively sort the left and right halves.
        mergeSort(arr, temp, low, mid, currentDepth + 1);
        mergeSort(arr, temp, mid + 1, high, currentDepth + 1);

        // If the two halves are already sorted relative to each other, skip the merge.
        // This is a common optimization for partially sorted data.
        if (arr[mid] <= arr[mid + 1]) {
            return;
        }

        // Merge the two sorted halves back into the original array.
        merge(arr, temp, low, mid, high);
    }

    /**
     * Merges two sorted sub-arrays into a single sorted array.
     * @param arr The original array.
     * @param temp A temporary buffer.
     * @param low The starting index of the sub-array.
     * @param mid The midpoint of the sub-array.
     * @param high The ending index of the sub-array.
     */
    private static void merge(double[] arr, double[] temp, int low, int mid, int high) {
        // Copy the sub-array to the temporary buffer.
        for (int k = low; k <= high; k++) {
            temp[k] = arr[k];
        }

        int i = low;      // Pointer for the left sub-array
        int j = mid + 1;  // Pointer for the right sub-array

        for (int k = low; k <= high; k++) {
            if (i > mid) {
                // Left half is exhausted, copy from the right.
                arr[k] = temp[j++];
            } else if (j > high) {
                // Right half is exhausted, copy from the left.
                arr[k] = temp[i++];
                comparisons++;
            } else if (temp[j] < temp[i]) {
                // Element from right half is smaller.
                arr[k] = temp[j++];
                comparisons++;
            } else {
                // Element from left half is smaller or equal.
                arr[k] = temp[i++];
                comparisons++;
            }
        }
    }

    /**
     * A simple Insertion Sort implementation for small arrays.
     * @param arr The array to be sorted.
     * @param low The starting index.
     * @param high The ending index.
     */
    private static void insertionSort(double[] arr, int low, int high) {
        for (int i = low + 1; i <= high; i++) {
            double key = arr[i];
            int j = i - 1;
            while (j >= low && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
                comparisons++;
            }
            arr[j + 1] = key;
        }
    }

    /**
     * Main method to test the MergeSort algorithm and print results.
     */
    public static void main(String[] args) {
        double[] testArray = {80.5, 64.1, 65.3, 70.0, 50.9, 30.2, 99.8, 12.3, 45.6, 88.7, 10.1, 55.4, 76.9, 21.0, 33.3, 67.2, 90.5, 11.2, 44.4, 25.5, 78.8};

        long startTime = System.nanoTime();
        sort(testArray);
        long endTime = System.nanoTime();

        System.out.println("Sorted Array: " + Arrays.toString(testArray));
        System.out.println("\n--- Performance Metrics ---");
        System.out.println("Execution Time: " + (endTime - startTime) / 1_000_000.0 + " ms");
        System.out.println("Total Comparisons: " + comparisons);
        System.out.println("Max Recursion Depth: " + maxRecursionDepth);
        System.out.println("Small-N Cutoff value: " + CUTOFF);
    }
}
