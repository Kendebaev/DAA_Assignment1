import java.util.Arrays;
import java.util.Random;

/** MergeSort Class (Modified for Testing)  **/
class MergeSort {


    public static final int CUTOFF = 7;


    public static long comparisons = 0;
    public static int maxRecursionDepth = 0;

    /**
     * Public-facing method to start the MergeSort process.*/
    public static void sort(double[] arr) {
        double[] temp = new double[arr.length];
        comparisons = 0;
        maxRecursionDepth = 0;
        mergeSort(arr, temp, 0, arr.length - 1, 0);
    }

    private static void mergeSort(double[] arr, double[] temp, int low, int high, int currentDepth) {
        if (currentDepth > maxRecursionDepth) {
            maxRecursionDepth = currentDepth;
        }

        if (high <= low + CUTOFF) {
            insertionSort(arr, low, high);
            return;
        }

        int mid = low + (high - low) / 2;

        mergeSort(arr, temp, low, mid, currentDepth + 1);
        mergeSort(arr, temp, mid + 1, high, currentDepth + 1);

        // Optimization: skip merge if already sorted
        if (arr[mid] <= arr[mid + 1]) {
            return;
        }

        merge(arr, temp, low, mid, high);
    }

    private static void merge(double[] arr, double[] temp, int low, int mid, int high) {
        for (int k = low; k <= high; k++) {
            temp[k] = arr[k];
        }

        int i = low;
        int j = mid + 1;

        for (int k = low; k <= high; k++) {
            if (i > mid) {
                arr[k] = temp[j++];
            } else if (j > high) {
                arr[k] = temp[i++];
                comparisons++;
            } else if (temp[j] < temp[i]) {
                arr[k] = temp[j++];
                comparisons++;
            } else {
                arr[k] = temp[i++];
                comparisons++;
            }
        }
    }

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
}



public class MergeSortTest {

    private static final Random RANDOM = new Random();
    private static int totalTests = 0;
    private static int failedTests = 0;

    /** Generates a random array of the specified size **/
    private static double[] generateRandomArray(int size) {
        double[] arr = new double[size];
        for (int i = 0; i < size; i++) {
            arr[i] = RANDOM.nextDouble() * 1000;
        }
        return arr;
    }

    /**
     * Checks if the array is sorted correctly.*/

    private static boolean isSorted(double[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] > arr[i + 1]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Compares the sorted array against a known correct array (the reference).
     */
    private static boolean arraysEqual(double[] actual, double[] expected) {
        return Arrays.equals(actual, expected);
    }

    /**
     * Main test runner method.
     */
    private static void runTest(String name, double[] input) {
        totalTests++;
        int N = input.length;
        double[] expected = Arrays.copyOf(input, N);

        // Use Java's Arrays.sort as the reference implementation
        Arrays.sort(expected);

        // Run the custom MergeSort
        MergeSort.sort(input);

        System.out.println("--------------------------------------------------");
        System.out.printf("Running Test: %s (N=%d)\n", name, N);

        boolean passed = isSorted(input);
        if (passed) {
            System.out.println("Result: PASSED (Array is correctly sorted)");
        } else {
            failedTests++;
            System.out.println("Result: FAILED (Array is not sorted correctly)");
        }

        // Print Metrics
        System.out.printf("  Metrics:\n");
        System.out.printf("    Total Comparisons: %d\n", MergeSort.comparisons);
        System.out.printf("    Max Recursion Depth: %d\n", MergeSort.maxRecursionDepth);

        // Calculate expected theoretical depth for a balanced split
        if (N > 0) {
            double theoreticalDepth = Math.floor(Math.log(N / (MergeSort.CUTOFF + 1)) / Math.log(2)) + 1;
            System.out.printf("    Theoretical Min Depth (approx.): %.2f\n", theoreticalDepth);
        }
    }

    public static void main(String[] args) {
        System.out.println("--- MergeSort Unit Tests ---");

        // 1. Correctness on large random array
        double[] randomArray = generateRandomArray(10000);
        runTest("Random Array (N=10000)", randomArray);

        // 2. Correctness on adversarial array (Already Sorted - Best Case Comparisons)
        double[] sortedArray = new double[5000];
        for (int i = 0; i < sortedArray.length; i++) sortedArray[i] = i;
        runTest("Adversarial: Already Sorted (N=5000)", sortedArray);

        // 3. Correctness on adversarial array (Reverse Sorted - Worst Case Comparisons)
        double[] reverseArray = new double[5000];
        for (int i = 0; i < reverseArray.length; i++) reverseArray[i] = 5000 - i;
        runTest("Adversarial: Reverse Sorted (N=5000)", reverseArray);

        // 4. Correctness on array with duplicates and small N
        double[] duplicateArray = {5.0, 2.0, 8.0, 2.0, 5.0, 1.0, 8.0, 3.0};
        runTest("Small Array with Duplicates (N=8)", duplicateArray);

        System.out.println("--------------------------------------------------");
        System.out.printf("TEST SUMMARY: %d Total Tests, %d Failed.\n", totalTests, failedTests);
        if (failedTests == 0) {
            System.out.println("All tests passed! The MergeSort implementation appears correct.");
        }
    }
}
