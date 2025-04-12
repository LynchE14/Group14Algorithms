public class MergeSort {

    public static void mergeSort(int[] arr) {
        if (arr.length < 2) {
            return; // No need to sort if the array has < 2 elements
        }
        // Finding the middle point to divide the array
        int mid = arr.length / 2;

        // Creating two subarrays
        int[] left = new int[mid];
        int[] right = new int[arr.length - mid];

        // Copying integers to left and right
        System.arraycopy(arr, 0, left, 0, mid);
        System.arraycopy(arr, mid, right, 0, arr.length - mid);

        // Recursively sorting the two subarrays
        mergeSort(left);
        mergeSort(right);

        // Merging the sorted subarrays
        merge(arr, left, right);
    }

    // To merge the two subarrays into the original array
    private static void merge(int[] arr, int[] left, int[] right) {
        int i = 0, j = 0, k = 0;

        // Merging left and right into arr
        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) {
                arr[k++] = left[i++];
            } else {
                arr[k++] = right[j++];
            }
        }

        // Copying remaining left elements if any
        while (i < left.length) {
            arr[k++] = left[i++];
        }

        // Copying remaining right elements if any
        while (j < right.length) {
            arr[k++] = right[j++];
        }
    }
}
