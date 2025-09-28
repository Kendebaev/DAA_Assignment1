import java.util.Arrays;
import java.util.Random;

public class DeterSelect {

    // Helper method to find the median of a small array (size <= 5)
    private static double medianOfSmallArray(double[] arr, int low, int high) {
        // Simple Insertion Sort for the small group
        for (int i = low + 1; i <= high; i++) {
            double key = arr[i];
            int j = i - 1;
            while (j >= low && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
        // The median is at the middle index
        return arr[low + (high - low) / 2];
    }


    private static double findMoMPivot(double[] arr, int low, int high) {
        int n = high - low + 1; // size of the current sub-array
        if (n <= 5) {
            return medianOfSmallArray(arr, low, high);
        }

        // 1. Group by 5 and find the median of each group.
        int numGroups = (int) Math.ceil(n / 5.0);
        double[] medians = new double[numGroups];

        for (int i = 0; i < numGroups; i++) {
            int groupLow = low + i * 5;
            int groupHigh = Math.min(groupLow + 4, high);

            // Find the median of the current group
            medians[i] = medianOfSmallArray(arr, groupLow, groupHigh);
        }

        // 2. Recursively find the median of the array of medians (MoM).

        return select(medians, 0, medians.length - 1, medians.length / 2);
    }


    private static int partition(double[] arr, int low, int high, double pivot) {
        // Find the pivot element's location and move it to the end for the partition process.
        for (int i = low; i <= high; i++) {
            if (arr[i] == pivot) {
                swap(arr, i, high);
                break;
            }
        }

        // Standard in-place partition (Lomuto's or Hoare's, here is a simple version)
        double pivotValue =arr[high];
        int i = low; // Index of smaller element

        for (int j =low; j < high; j++) {
            if (arr[j] <=pivotValue) {
                swap(arr, i, j);
                i++;
            }
        }
        // Put the pivot back into its final sorted position
        swap(arr, i, high);
        return i;
    }


    public static double select(double[] arr, int low, int high, int k) {
        if (low == high) {
            return arr[low];
        }

        // 1. Find the guaranteed 'good' pivot (Median-of-Medians).
        double pivotValue = findMoMPivot(arr, low, high);

        // 2. Partition the array around the MoM pivot.
        int pivotIndex = partition(arr, low, high, pivotValue);

        // Calculate the rank of the pivot (number of elements less than or equal to pivot)
        int pivotRank = pivotIndex - low;

        // Recurse only into the needed side.
        if (k == pivotRank) {

            return arr[pivotIndex];
        } else if (k < pivotRank) {

            return select(arr, low, pivotIndex - 1, k);
        } else {

            return select(arr, pivotIndex + 1, high, k - pivotRank - 1);
        }
    }

    // swap two elements.
    private static void swap(double[] arr, int i, int j) {
        double temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) {
        double[] testArray = {80.5, 64.1, 65.3, 70.0, 50.9, 30.2, 99.8, 12.3, 45.6, 88.7, 10.1, 55.4, 76.9, 21.0, 33.3, 67.2, 90.5, 11.2};
        int arrayLength = testArray.length;


        int k = arrayLength / 2;


        double[] sortedCopy = Arrays.copyOf(testArray, arrayLength);
        Arrays.sort(sortedCopy);

        long startTime = System.nanoTime();
        double kThElement = select(testArray, 0, arrayLength - 1, k);
        long endTime = System.nanoTime();

        System.out.println("Original Array Size: " + arrayLength);
        System.out.println("Desired Rank (k): " + k);
        System.out.println("The " + (k + 1) + "-th Smallest Element (k=" + k + "): " + kThElement);
        System.out.println("Verification (Sorted Array[" + k + "]): " + sortedCopy[k]);
        System.out.println("\n--- Performance ---");
        System.out.println("Execution Time: " + (endTime - startTime) / 1_000_000.0 + " ms");
    }
}