public class BubbleSort {

    public void bubbleSort(int[] arr) {
        // Traverse through array
        for (int i = 0; i < arr.length - 1; i++) {
            // Inner loop
            for (int j = 0; j < arr.length - i - 1; j++) {
                // Swap if the integer is greater than the next
                if (arr[j] > arr[j + 1]) {
                    // Swapping
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }
}