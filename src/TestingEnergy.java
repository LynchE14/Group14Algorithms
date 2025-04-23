import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestingEnergy {

    static String energyFilePath = "energyreadings.csv";
    static File outputFile = new File("outputFile.csv");

    public static void main(String[] args) throws Exception {
        int[][] BsBCRuns = makeRunSuite("CSVFILES/BubbleSort/BestCase");
        for (int i = 0; i < BsBCRuns.length; i++) {
            BubbleSortArrays(BsBCRuns[i], 50, "BubbleSortBestCase");
        }
        int[][] BsWCRuns = makeRunSuite("CSVFILES/BubbleSort/WorstCase");
        for (int i = 0; i < BsWCRuns.length; i++) {
            BubbleSortArrays(BsWCRuns[i], 50, "BubbleSortWorstCase");
        }
        int[][] BsRCRuns = makeRunSuite("CSVFILES/BubbleSort/Random");
        for (int i = 0; i < BsRCRuns.length; i++) {
            BubbleSortArrays(BsRCRuns[i], 5, "BubbleSortRandomCase");
        }

        int[][] MsBCRuns = makeRunSuite("CSVFILES/MergeSort/BestCase");
        for (int i = 0; i < MsBCRuns.length; i++) {
            MergeSortArrays(MsBCRuns[i], 5, "MergeSortBestCase");
        }
        int[][] MsWCRuns = makeRunSuite("CSVFILES/MergeSort/WorstCase");
        for (int i = 0; i < MsWCRuns.length; i++) {
            MergeSortArrays(MsWCRuns[i], 5, "MergeSortWorstCase");
        }
        int[][] MsRCRuns = makeRunSuite("CSVFILES/MergeSort/Random");
        for (int i = 0; i < MsRCRuns.length; i++) {
            MergeSortArrays(MsRCRuns[i], 3, "MergeSortRandomCase");
        }

        int[][] CsBCRuns = makeRunSuite("CSVFILES/CountingSort/BestCase");
        for (int i = 0; i < CsBCRuns.length; i++) {
            CountingSortArrays(CsBCRuns[i], 5, "CountingSortBestCase", 10);
        }
        int[][] CsWCRuns = makeRunSuite("CSVFILES/CountingSort/WorstCase");
        for (int i = 0; i < CsWCRuns.length; i++) {
            CountingSortArrays(CsWCRuns[i], 5, "CountingSortWorstCase", 100000000);
        }

        int[][] QsBCRuns = makeRunSuite("CSVFILES/QuickSort/BestCase");
        for (int i = 0; i < QsBCRuns.length; i++) {
            QuickSortArrays(QsBCRuns[i], 5, "QuickSortBestCase");
        }
        int[][] QsWCRuns = makeRunSuite("CSVFILES/QuickSort/WorstCase");
        for (int i = 0; i < QsWCRuns.length; i++) {
            QuickSortArrays(QsWCRuns[i], 5, "QuickSortWorstCase");
        }
        int[][] QsRCRuns = makeRunSuite("CSVFILES/QuickSort/Random");
        for (int i = 0; i < QsRCRuns.length; i++) {
            QuickSortArrays(QsRCRuns[i], 3, "QuickSortRandomCase");
        }
    }

    public static int[][] makeRunSuite(String folderPath){
        File[] files = makeFileArray(folderPath);
        int [][] intArrays = new int[files.length][];
        for (int i = 0; i < files.length; i++) {
            intArrays[i] = csvToIntArray(files[i]);
        }

        return intArrays;
    }

    // returns array of files to be iterated through
    public static File[] makeFileArray(String folderPath) {
        File folder = new File(folderPath);

        // Check if the folder exists and is a directory
        if (folder.exists() && folder.isDirectory()) {
            // return file array
            return folder.listFiles((dir, name) -> name.endsWith(".csv"));
        } else {
            System.out.println("Folder not found!");
            return null;
        }
    }

    public static int[] csvToIntArray(File file) {
        List<Integer> numbersList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.lines()
                    .flatMap(line -> Arrays.stream(line.split(",")))
                    .map(String::trim)
                    .mapToInt(Integer::parseInt)
                    .forEach(numbersList::add);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return numbersList.stream().mapToInt(Integer::intValue).toArray();
    }

    // write data for each sort to csv file
    public static void outputData(double timeTaken, double energyConsumed,int size, String nameOfRun, File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            // Format: timeTaken,energyConsumed,nameOfRun
            writer.write(timeTaken + "," + energyConsumed + "," + size + "," + nameOfRun);
            writer.newLine(); // Move to the next line
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void BubbleSortArrays(int[] array, int runs, String run) throws IOException, InterruptedException {
        // create instance of sort class
        BubbleSort b = new BubbleSort();
        // iterate through arrays
        for (int i = 0; i < runs; i++) {
            int[] arrayToBeSorted = new int[array.length];
            System.arraycopy(array, 0, arrayToBeSorted, 0, array.length);
            // Format the current time as HH:mm:ss.SSS to match CSV format
            String startTimeStr = getCurrentTimeFormatted();
            // sort
            b.bubbleSort(arrayToBeSorted);
            // Record end time in same format
            String endTimeStr = getCurrentTimeFormatted();
            System.out.println("Array [" + i + "] sorted for size: " + arrayToBeSorted.length);

            // calculate energy consumed
            double energyConsumed = estimateEnergy(energyFilePath, startTimeStr, endTimeStr);
            // output data to output csv file
            outputData((timeStrToMillisSinceMidnight(endTimeStr) - timeStrToMillisSinceMidnight(startTimeStr)), energyConsumed, array.length, run + " - " + i, outputFile);
            // Add a delay between tests to ensure distinct time ranges
            Thread.sleep(75);
        }
    }

    public static void MergeSortArrays(int[] array, int runs, String run) throws IOException, InterruptedException {
        // create instance of sort class
        MergeSort m = new MergeSort();
        // iterate through arrays
        for (int i = 0; i < runs; i++) {
            int[] arrayToBeSorted = new int[array.length];
            System.arraycopy(array, 0, arrayToBeSorted, 0, array.length);
            // Format the current time as HH:mm:ss.SSS to match CSV format
            String startTimeStr = getCurrentTimeFormatted();
            // sort
            m.mergeSort(arrayToBeSorted);
            // Record end time in same format
            String endTimeStr = getCurrentTimeFormatted();
            System.out.println("Array [" + i + "] sorted: " + run);

            // calculate energy consumed
            double energyConsumed = estimateEnergy(energyFilePath, startTimeStr, endTimeStr);
            // output data to output csv file
            outputData((timeStrToMillisSinceMidnight(endTimeStr) - timeStrToMillisSinceMidnight(startTimeStr)), energyConsumed, array.length, run + " - " + i, outputFile);

            // Add a delay between tests to ensure distinct time ranges
            Thread.sleep(75);
        }
    }

    public static void QuickSortArrays(int[] array, int runs, String run) throws IOException, InterruptedException {
        // create instance of sort class
        QuickSort q = new QuickSort();
        // iterate through arrays
        for (int i = 0; i < runs; i++) {
            int[] arrayToBeSorted = new int[array.length];
            System.arraycopy(array, 0, arrayToBeSorted, 0, array.length);
            // Format the current time as HH:mm:ss.SSS to match CSV format
            String startTimeStr = getCurrentTimeFormatted();
            // sort
            q.quickSort(arrayToBeSorted, 0, array.length - 1);
            // Record end time in same format
            String endTimeStr = getCurrentTimeFormatted();
            System.out.println("Array [" + i + "] sorted: " + run);

            // calculate energy consumed
            double energyConsumed = estimateEnergy(energyFilePath, startTimeStr, endTimeStr);
            // output data to output csv file
            outputData((timeStrToMillisSinceMidnight(endTimeStr) - timeStrToMillisSinceMidnight(startTimeStr)), energyConsumed, array.length, run + " - " + i, outputFile);

            // Add a delay between tests to ensure distinct time ranges
            Thread.sleep(75);
        }
    }

    public static void CountingSortArrays(int[] array, int runs, String run, int k) throws IOException, InterruptedException {
        // create instance of sort class
        CountingSort c = new CountingSort();
        // iterate through arrays
        for (int i = 0; i < runs; i++) {
            int[] arrayToBeSorted = new int[array.length];
            System.arraycopy(array, 0, arrayToBeSorted, 0, array.length);
            // Format the current time as HH:mm:ss.SSS to match CSV format
            String startTimeStr = getCurrentTimeFormatted();
            // sort
            c.CountingSort(arrayToBeSorted, k);
            // Record end time in same format
            String endTimeStr = getCurrentTimeFormatted();
            System.out.println("Array [" + i + "] sorted: " + run);

            // calculate energy consumed
            double energyConsumed = estimateEnergy(energyFilePath, startTimeStr, endTimeStr);
            // output data to output csv file
            outputData((timeStrToMillisSinceMidnight(endTimeStr) - timeStrToMillisSinceMidnight(startTimeStr)), energyConsumed, array.length, run + " - " + i, outputFile);

            // Add a delay between tests to ensure distinct time ranges
            Thread.sleep(75);
        }
    }

    // Helper method to get current time in the same format as the CSV
    private static String getCurrentTimeFormatted() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        return sdf.format(new Date());
    }

    public static List<String[]> parseCsvWithQuotes(String filePath) {
        List<String[]> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Use regular expression to handle commas inside quoted fields
                String[] columns = line.split("(?<!\"),");
                for (int i = 0; i < columns.length; i++) {
                    // Remove quotes around the fields
                    columns[i] = columns[i].replaceAll("^\"|\"$", "");
                }
                rows.add(columns);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rows;
    }

    // Modified to take time strings instead of milliseconds
    public static double estimateEnergy(String csvPath, String startTimeStr, String endTimeStr) throws IOException {
        // null checks
        if (csvPath == null || csvPath.isEmpty()) {
            throw new IllegalArgumentException("CSV path cannot be null or empty");
        }

        // Convert input times to milliseconds since midnight for comparison
        long startMillis = timeStrToMillisSinceMidnight(startTimeStr);
        long endMillis = timeStrToMillisSinceMidnight(endTimeStr);

        // No point processing if time range is invalid
        if (endMillis < startMillis) {
            throw new IllegalArgumentException("End time must be after start time");
        }

        List<String[]> rows = parseCsvWithQuotes(csvPath);

        if (rows.isEmpty()) {
            throw new IOException("CSV file is empty");
        }

        int timeIndex = 1;
        int powerIndex = 2;

        double totalEnergy = 0;
        long prevTimestamp = -1;
        double prevPower = 0;
        boolean foundFirstPointInRange = false;
        int pointsInRange = 0;

        // Process each data row (skipping header)
        for (int i = 1; i < rows.size(); i++) {
            try {
                String timeStr = rows.get(i)[timeIndex];
                if (timeStr.isEmpty()) {
                    System.err.println("Skipping line " + i + ": empty timestamp");
                    continue;
                }

                // Convert timestamp string to milliseconds since midnight
                long timestamp = timeStrToMillisSinceMidnight(timeStr);

                // Three possible cases: before range, in range, after range
                if (timestamp < startMillis) {
                    // Before our range - just remember this point for possible interpolation
                    prevTimestamp = timestamp;
                    try {
                        prevPower = Double.parseDouble(rows.get(i)[powerIndex]);
                    } catch (NumberFormatException e) {
                        prevPower = 0;
                        System.err.println("Invalid power value at line " + i + ": " + rows.get(i)[powerIndex]);
                    }
                } else if (timestamp <= endMillis) {
                    // Within our range - this is data we care about
                    pointsInRange++;
                    double power;
                    try {
                        power = Double.parseDouble(rows.get(i)[powerIndex]);
                    } catch (NumberFormatException e) {
                        System.err.println("Skipping line " + i + ": invalid power value: " + rows.get(i)[powerIndex]);
                        continue;
                    }

                    // Special handling for the first point we encounter within our range
                    if (!foundFirstPointInRange) {
                        foundFirstPointInRange = true;

                        // If we have a point before the range, we can interpolate exactly at the start boundary
                        if (prevTimestamp != -1 && prevTimestamp < startMillis) {
                            // Simple linear interpolation between the previous point and current point
                            double ratio = (double) (startMillis - prevTimestamp) / (timestamp - prevTimestamp);
                            double interpolatedPower = prevPower + ratio * (power - prevPower);

                            // Use this interpolated value as our starting point
                            prevTimestamp = startMillis;
                            prevPower = interpolatedPower;
                        } else {
                            // No previous point to interpolate with, just use this one
                            prevTimestamp = timestamp;
                            prevPower = power;
                        }
                    } else {
                        // Not the first point - calculate energy between previous point and this one
                        double deltaTimeSec = (timestamp - prevTimestamp) / 1000.0;
                        if (deltaTimeSec > 0) {  // Make sure time isn't going backwards (bad data)
                            // Energy = Power × Time (constant power approximation)
                            double energySegment = prevPower * deltaTimeSec;
                            totalEnergy += energySegment;
                            prevTimestamp = timestamp;
                            prevPower = power;
                        } else {
                            System.err.println("Warning: Non-increasing timestamps at line " + i);
                        }
                    }
                } else {
                    // After our range - we can stop processing, but first handle the boundary
                    // If we have a previous point and it's within our range, calculate energy to the end boundary
                    if (prevTimestamp != -1 && prevTimestamp <= endMillis) {
                        try {
                            double power = Double.parseDouble(rows.get(i)[powerIndex]);
                            // Interpolate power at end boundary
                            double ratio = (double) (endMillis - prevTimestamp) / (timestamp - prevTimestamp);
                            double interpolatedPower = prevPower + ratio * (power - prevPower);

                            // Calculate energy from last point to end boundary
                            double deltaTimeSec = (endMillis - prevTimestamp) / 1000.0;
                            double energySegment = prevPower * deltaTimeSec;
                            totalEnergy += energySegment;
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid power value at line " + i + ": " + rows.get(i)[powerIndex]);
                        }
                    }
                    break; // No need to process more data beyond our range
                }
            } catch (Exception e) {
                System.err.println("Error processing line " + i + ": " + e.getMessage());
            }
        }
        return totalEnergy; // in Joules
    }

    // Convert HH:mm:ss.SSS to milliseconds since midnight
    private static long timeStrToMillisSinceMidnight(String timeStr) throws NumberFormatException {
        // Break the time string into hours, minutes, seconds, and milliseconds
        String[] parts = timeStr.split("[:\\.]");
        if (parts.length != 4) {
            throw new NumberFormatException("Invalid time format: " + timeStr + ", expected HH:mm:ss.fff");
        }

        try {
            // Convert each part to an integer
            int h = Integer.parseInt(parts[0]);
            int m = Integer.parseInt(parts[1]);
            int s = Integer.parseInt(parts[2]);
            int ms = Integer.parseInt(parts[3]);

            // Make sure values are in reasonable ranges
            if (h < 0 || h > 23 || m < 0 || m > 59 || s < 0 || s > 59 || ms < 0 || ms > 999) {
                throw new NumberFormatException("Time values out of range: " + timeStr);
            }

            // Convert to milliseconds since midnight
            return ((h * 60L + m) * 60L + s) * 1000L + ms;
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid time format: " + timeStr + " - " + e.getMessage());
        }
    }
}