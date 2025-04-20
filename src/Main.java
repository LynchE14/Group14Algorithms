import java.util.Arrays;
//import com.opencsv.CSVReader;
//import java.io.FileReader;
//import java.io.IOException;

public class Main {



    public static void main(String[] args) {
        BubbleSort bubble = new BubbleSort();
        CountingSort counting = new CountingSort();
        MergeSort merge = new MergeSort();
        QuickSort quick = new QuickSort();

        int[] bubbleArray = {5, 60, 347, 1, 53, 22, 95, 44};
        int[] countingArray = {6, 3, 7, 1, 5, 9, 4, 8};
        int[] mergeArray = {5, 60, 347, 1, 53, 22, 95, 44};
        int[] quickArray = {6, 3, 0, 1, 5, 9, 4, 7};

        bubble.bubbleSort(bubbleArray);
        counting.CountingSort(countingArray, 9);
        merge.mergeSort(mergeArray);
        quick.quickSort(quickArray, 0, quickArray.length-1);

        System.out.println(Arrays.toString(quickArray));
        System.out.println(Arrays.toString(bubbleArray));
        System.out.println(Arrays.toString(countingArray));
        System.out.println(Arrays.toString(mergeArray));
    }
}