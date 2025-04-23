public class QuickSort {

    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            // Finding pivot element
            int pivot = partition(arr, low, high);

            // Recursively sorting elements before and after partition
            quickSort(arr, low, pivot - 1);
            quickSort(arr, pivot + 1, high);
        }
    }

    // Function to partition the array and return pivot index
    private static int partition(int[] arr, int low, int high) {
        // Pivot is chosen as the last element of the array
        int pivot = arr[low + (int)(Math.random() * (high - low + 1))];
        int i = low - 1; // Index of smaller element

        // Traversing through array and moving elements smaller than pivot to the left
        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                // Swapping arr[i] and arr[j]
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        // Swapping pivot element with element at i+1 so pivot is in correct position
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1; // Returning pivot index
    }
}
