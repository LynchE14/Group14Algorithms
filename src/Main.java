import java.util.Arrays;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // Assuming CSV contains a single line of comma-separated integers
        String csvFile = "csv/data.csv";

        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            List<String[]> lines = reader.readAll();

            if (!lines.isEmpty()) {
                String[] firstLine = lines.get(0);
                int[] dataArray = new int[firstLine.length];

                for (int i = 0; i < firstLine.length; i++) {
                    dataArray[i] = Integer.parseInt(firstLine[i]);
                }

                System.out.println(Arrays.toString(dataArray));

                // You can now use dataArray with your sorting classes
                BubbleSort bubble = new BubbleSort();
                CountingSort counting = new CountingSort();
                MergeSort merge = new MergeSort();
                QuickSort quick = new QuickSort();

                bubble.bubbleSort(dataArray); // Cloning to preserve original
                counting.CountingSort(dataArray.clone(), 9);
                merge.mergeSort(dataArray.clone());
                quick.quickSort(dataArray.clone(), 0, dataArray.length - 1);

                System.out.println(Arrays.toString(dataArray));
            } else {
                System.out.println("CSV file is empty or not found.");
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }
}