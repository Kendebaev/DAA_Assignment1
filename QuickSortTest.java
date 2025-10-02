import java.util.Arrays;
import java.util.Random;

/**
 * --- QuickSort Class **/

class QuickSort {

    private static final Random RANDOM = new Random();
    private static final int CUTOFF = 1;

    // --- METRIC: Made public static for external testing access ---
    public static int maxRecursionDepth = 0;

    public static void sort(double[] arr) {
        maxRecursionDepth = 0; // Reset counter for each sort
        shuffle(arr);
        quickSort(arr, 0, arr.length - 1, 0); // Start depth tracking at 0
    }

    private static void shuffle(double[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int r = RANDOM.nextInt(i + 1);
            swap(arr, i, r);
        }
    }

    // MODIFIED quickSort to track depth
    private static void quickSort(double[] arr, int low, int high, int currentDepth) {
        // Update max depth
        if (currentDepth > maxRecursionDepth) {
            maxRecursionDepth = currentDepth;
        }

       
        while (low < high) {
            if (high - low < CUTOFF) {
                return;
            }

            int pivotIndex = partition(arr, low, high);

            // Recurse on the smaller partition, iterate on the larger one
            if (pivotIndex - low < high - pivotIndex) {
                // Recurse Left (smaller)
                quickSort(arr, low, pivotIndex - 1, currentDepth + 1);
                low = pivotIndex + 1; // Iterate Right (larger)
            } else {
                // Recurse Right (smaller)
                quickSort(arr, pivotIndex + 1, high, currentDepth + 1);
                high = pivotIndex - 1; // Iterate Left (larger)
            }
        }
    }

    private static int partition(double[] arr, int low, int high) {
        int randomPivotIndex = low + RANDOM.nextInt(high - low + 1);
        swap(arr, randomPivotIndex, high);

        double pivot = arr[high];
        int i = low;

        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                swap(arr, i, j);
                i++;
            }
        }

        swap(arr, i, high);
        return i;
    }

    // Utility method to swap two elements in an array.
     
    private static void swap(double[] arr, int i, int j) {
        double temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}


// Test
public class QuickSortTest {

    private static final Random RANDOM = new Random();
    private static int totalTests = 0;
    private static int failedTests = 0;

    /**
     * Generates a worst-case array (already sorted/reverse sorted).
     */
    private static double[] generateAdversarialArray(int size, boolean reverse) {
        double[] arr = new double[size];
        for (int i = 0; i < size; i++) {
            arr[i] = reverse ? (size - i) : i;
        }
        return arr;
    }

    /**
     * Checks if the array is sorted correctly.
     */
    private static boolean isSorted(double[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] > arr[i + 1]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Runs the test, verifies correctness, and checks the recursion depth bound.
     */
    private static void runDepthTest(String name, double[] input, double boundFactor) {
        totalTests++;
        int N = input.length;

        // --- Execution ---
        QuickSort.sort(input);
        int maxDepth = QuickSort.maxRecursionDepth;

        // --- Verification ---
        boolean correctnessPassed = isSorted(input);

        // Theoretical balanced depth for O(log n)
        double theoreticalBalancedDepth = Math.ceil(Math.log(N) / Math.log(2));

        // Max allowed depth check: ~2 * log2(N)
        double maxAllowedDepth = boundFactor * theoreticalBalancedDepth;

        boolean depthPassed = (double)maxDepth <= maxAllowedDepth;

        // --- Output ---
        System.out.println("--------------------------------------------------");
        System.out.printf("Test: %s (N=%d)\n", name, N);
        System.out.printf("Correctness Check: %s\n", correctnessPassed ? "PASSED" : "FAILED");

        System.out.println("\n--- Recursion Depth Validation ---");
        System.out.printf("  Max Recursion Depth Observed: %d\n", maxDepth);
        System.out.printf("  Theoretical Balanced Depth (log₂N): %.0f\n", theoreticalBalancedDepth);
        System.out.printf("  Max Allowed Depth (%.1f * log₂N): %.0f\n", boundFactor, maxAllowedDepth);
        System.out.printf("  Depth Bound Check: %s\n", depthPassed ? "PASSED" : "FAILED");

        if (!correctnessPassed || !depthPassed) {
            failedTests++;
        }
    }

    public static void main(String[] args) {
        System.out.println("--- QuickSort Robustness Tests ---");

        // Use a bound factor (e.g., 2.5) to test against 2 * log2(N) + O(1)
        final double BOUND_FACTOR = 2.5;

        // 1. Adversarial Test: Already Sorted Array (N=20000)
        double[] sortedArray = generateAdversarialArray(20000, false);
        runDepthTest("Adversarial: Already Sorted Array", sortedArray, BOUND_FACTOR);

        // 2. Adversarial Test: Reverse Sorted Array (N=20000)
        double[] reverseArray = generateAdversarialArray(20000, true);
        runDepthTest("Adversarial: Reverse Sorted Array", reverseArray, BOUND_FACTOR);

        // 3. Large Random Array (Average Case) (N=50000)
        double[] randomArray = new double[50000];
        for (int i = 0; i < randomArray.length; i++) randomArray[i] = RANDOM.nextDouble() * 10000;
        runDepthTest("Average Case: Large Random Array", randomArray, BOUND_FACTOR);

        System.out.println("--------------------------------------------------");
        System.out.printf("TEST SUMMARY: %d Total Tests, %d Failed.\n", totalTests, failedTests);
        if (failedTests == 0) {
            System.out.println("Conclusion: QuickSort is correct and its recursion depth is safely bounded.");
        }
    }
}
