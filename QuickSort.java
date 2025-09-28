import java.util.Arrays;
import java.util.Random;





public class QuickSort {

    private static final Random RANDOM = new Random();


    private static final int CUTOFF = 1;

    public static void sort(double[] arr) {

        shuffle(arr);
        quickSort(arr, 0, arr.length - 1);
    }


    private static void shuffle(double[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int r = RANDOM.nextInt(i + 1);
            swap(arr, i, r);
        }
    }


    private static void quickSort(double[] arr, int low, int high) {

        while (low < high) {

            if (high - low < CUTOFF) {

                return;
            }


            int pivotIndex = partition(arr, low, high);


            if (pivotIndex - low < high - pivotIndex) {

                quickSort(arr, low, pivotIndex - 1);
                low = pivotIndex + 1;
            } else {

                quickSort(arr, pivotIndex + 1, high);
                high = pivotIndex - 1; // Tail call eliminated by setting new high index.
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

    /**
     swap two elements in an array.
     */
    private static void swap(double[] arr, int i, int j) {
        double temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) {
        double[] testArray = {80.5, 64.1, 65.3, 70.0, 50.9, 30.2, 99.8, 12.3, 45.6, 88.7};

        System.out.println("Original Array: " + Arrays.toString(testArray));

        long startTime = System.nanoTime();
        sort(testArray);
        long endTime = System.nanoTime();

        System.out.println("Sorted Array:   " + Arrays.toString(testArray));
        System.out.println("\n--- Performance ---");
        System.out.println("Execution Time: " + (endTime - startTime) / 1_000_000.0 + " ms");
    }
}