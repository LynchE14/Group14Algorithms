public class CountingSort {

    public void CountingSort(int[] input, int k) {
        // Creating a count array to store the count of each unique value
        int[] count = new int[k + 1];

        // Storing the count of each number in the input array
        for (int i = 0; i < input.length; i++) {
            count[input[i]]++;
        }

        // Adding the previous counts to find the position of each number
        for (int i = 1; i <= k; i++) {
            count[i] += count[i - 1];
        }

        // Creating an output array to store the sorted values
        int[] output = new int[input.length];

        // Build the output array by placing each number at its correct position
        for (int i = input.length - 1; i >= 0; i--) {
            // Placing current integer in correct place in output array according to count array
            output[count[input[i]] - 1] = input[i];
            // Decrementing count to account for duplicate values
            count[input[i]]--;
        }

        // Copying the sorted values back into the original array
        System.arraycopy(output, 0, input, 0, input.length);
    }
}
