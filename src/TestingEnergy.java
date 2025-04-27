import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestingEnergy {

    // path to csv being written to by HWiNFO
    static String energyFilePath = "energyreadings.csv";
    // path to file to output data from sorting to
    static File outputFile = new File("outputFile.csv");

    public static void main(String[] args) throws Exception {
        // BubbleSort
        // Best case
        int[][] BsBCRuns = makeRunSuite("CSVFILES/BubbleSort/BestCase");
        for (int[] bsBCRun : BsBCRuns) {
            // 10 runs of each csv
            BubbleSortArrays(bsBCRun, 10, "BubbleSortBestCase");
        }
        // Worst case
        int[][] BsWCRuns = makeRunSuite("CSVFILES/BubbleSort/WorstCase");
        for (int[] bsWCRun : BsWCRuns) {
            // 10 runs of each csv
            BubbleSortArrays(bsWCRun, 10, "BubbleSortWorstCase");
        }
        // Random
        int[][] BsRCRuns = makeRunSuite("CSVFILES/BubbleSort/Random");
        for (int[] bsRCRun : BsRCRuns) {
            // 5 runs of each csv
            BubbleSortArrays(bsRCRun, 5, "BubbleSortRandomCase");
        }

        // MergeSort
        // Best Case
        int[][] MsBCRuns = makeRunSuite("CSVFILES/MergeSort/BestCase");
        for (int[] msBCRun : MsBCRuns) {
            // 5 runs of each csv
            MergeSortArrays(msBCRun, 5, "MergeSortBestCase");
        }
        // Worst Case
        int[][] MsWCRuns = makeRunSuite("CSVFILES/MergeSort/WorstCase");
        for (int[] msWCRun : MsWCRuns) {
            // 5 runs of each csv
            MergeSortArrays(msWCRun, 5, "MergeSortWorstCase");
        }
        // Random
        int[][] MsRCRuns = makeRunSuite("CSVFILES/MergeSort/Random");
        for (int[] msRCRun : MsRCRuns) {
            // 5 runs of each csv
            MergeSortArrays(msRCRun, 3, "MergeSortRandomCase");
        }

        // CountingSort
        // Best Case
        int[][] CsBCRuns = makeRunSuite("CSVFILES/CountingSort/BestCase");
        for (int[] csBCRun : CsBCRuns) {
            // 5 runs of each csv
            // k = 10
            CountingSortArrays(csBCRun, 5, "CountingSortBestCase", 10);
        }
        // Worst Case
        int[][] CsWCRuns = makeRunSuite("CSVFILES/CountingSort/WorstCase");
        for (int[] csWCRun : CsWCRuns) {
            // 5 runs of each csv
            // k = 10 million
            CountingSortArrays(csWCRun, 5, "CountingSortWorstCase", 100000000);
        }

        // QuickSort
        // Best Case
        int[][] QsBCRuns = makeRunSuite("CSVFILES/QuickSort/BestCase");
        for (int[] qsBCRun : QsBCRuns) {
            // 5 runs of each csv
            QuickSortArrays(qsBCRun, 5, "QuickSortBestCase");
        }
        // Worst Case
        int[][] QsWCRuns = makeRunSuite("CSVFILES/QuickSort/WorstCase");
        for (int[] qsWCRun : QsWCRuns) {
            // 5 runs of each csv
            QuickSortArrays(qsWCRun, 5, "QuickSortWorstCase");
        }
        // Random Case
        int[][] QsRCRuns = makeRunSuite("CSVFILES/QuickSort/Random");
        for (int[] qsRCRun : QsRCRuns) {
            // 5 runs of each csv
            QuickSortArrays(qsRCRun, 3, "QuickSortRandomCase");
        }
    }

    // method combining below methods to create 3D int array to iterate through with sorting algorithm methods
    public static int[][] makeRunSuite(String folderPath) {
        // file array from chosen csv folder
        File[] files = makeFileArray(folderPath);
        // 3D int array to return, contains every csv -> int array
        int[][] intArrays = new int[files.length][];
        for (int i = 0; i < files.length; i++) {
            // convert csv file to int array
            intArrays[i] = csvToIntArray(files[i]);
        }

        return intArrays;
    }

    // returns array of files to be iterated through
    public static File[] makeFileArray(String folderPath) {
        // find folder from given path
        File folder = new File(folderPath);

        // Check if the folder exists and is a directory
        if (folder.exists() && folder.isDirectory()) {
            // return file array
            return folder.listFiles((_, name) -> name.endsWith(".csv"));
        } else {
            System.out.println("Folder not found!");
            return null;
        }
    }

    // method to convert comma seperated csv file full of ints to int array
    public static int[] csvToIntArray(File file) {
        // using list so number of ints in csv file not needed to be known
        List<Integer> numbersList = new ArrayList<>();

        // open csv file for reading using BufferedReader
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            // read each line from the file as a Stream
            br.lines()
                    // split each line into an array, then flatten to stream of values
                    .flatMap(line -> Arrays.stream(line.split(",")))
                    // remove any whitespace from value
                    .map(String::trim)
                    // convert each string to integer
                    .mapToInt(Integer::parseInt)
                    // add each integer numbersList
                    .forEach(numbersList::add);
        } catch (IOException e) {
            e.printStackTrace();
        }


        // return list converted to int array
        return numbersList.stream().mapToInt(Integer::intValue).toArray();
    }

    // write data for each sort run to csv file
    public static void outputData(double timeTaken, double energyConsumed, int size, String nameOfRun, File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            // output time taken, energy consumed, info about run
            writer.write(timeTaken + "," + energyConsumed + "," + size + "," + nameOfRun);
            writer.newLine(); // Move to the next line
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void endOfSort(int i, int[] arrayToBeSorted, String startTimeStr, String run) throws InterruptedException, IOException {
        // Record end time in same format
        String endTimeStr = getCurrentTimeFormatted();
        System.out.println("Array [" + i + "] sorted for size: " + arrayToBeSorted.length);

        // calculate energy consumed
        double energyConsumed = estimateEnergy(energyFilePath, startTimeStr, endTimeStr);
        // output data to output csv file
        outputData((timeStrToMillisSinceMidnight(endTimeStr) - timeStrToMillisSinceMidnight(startTimeStr)), energyConsumed, arrayToBeSorted.length, run + " - " + i, outputFile);
        // Add a delay between tests to ensure distinct time ranges
        Thread.sleep(75);
    }

    public static void BubbleSortArrays(int[] array, int runs, String run) throws IOException, InterruptedException {
        // create instance of sort class
        BubbleSort b = new BubbleSort();
        // iterate through arrays
        for (int i = 0; i < runs; i++) {
            // create array to be sorted
            int[] arrayToBeSorted = new int[array.length];
            // create deep copy
            System.arraycopy(array, 0, arrayToBeSorted, 0, array.length);
            // Format the current time as HH:mm:ss.SSS to match CSV format
            String startTimeStr = getCurrentTimeFormatted();
            // sort
            b.bubbleSort(arrayToBeSorted);
            // Record end time in same format
            endOfSort(i, arrayToBeSorted, startTimeStr, run);
        }
    }

    public static void MergeSortArrays(int[] array, int runs, String run) throws IOException, InterruptedException {
        // iterate through arrays
        for (int i = 0; i < runs; i++) {
            // create array to be sorted
            int[] arrayToBeSorted = new int[array.length];
            // create deep copy
            System.arraycopy(array, 0, arrayToBeSorted, 0, array.length);
            // Format the current time as HH:mm:ss.SSS to match CSV format
            String startTimeStr = getCurrentTimeFormatted();
            // sort
            MergeSort.mergeSort(arrayToBeSorted);
            // Record end time in same format
            endOfSort(i, arrayToBeSorted, startTimeStr, run);
        }
    }

    public static void QuickSortArrays(int[] array, int runs, String run) throws IOException, InterruptedException {

        // iterate through arrays
        for (int i = 0; i < runs; i++) {
            // create array to be sorted
            int[] arrayToBeSorted = new int[array.length];
            // create deep copy
            System.arraycopy(array, 0, arrayToBeSorted, 0, array.length);
            // Format the current time as HH:mm:ss.SSS to match CSV format
            String startTimeStr = getCurrentTimeFormatted();
            // sort
            QuickSort.quickSort(arrayToBeSorted, 0, array.length - 1);
            // Record end time in same format
            endOfSort(i, arrayToBeSorted, startTimeStr, run);
        }
    }

    public static void CountingSortArrays(int[] array, int runs, String run, int k) throws IOException, InterruptedException {
        // create instance of sort class
        CountingSort c = new CountingSort();
        // iterate through arrays
        for (int i = 0; i < runs; i++) {
            // create array to be sorted
            int[] arrayToBeSorted = new int[array.length];
            // create deep copy
            System.arraycopy(array, 0, arrayToBeSorted, 0, array.length);
            // Format the current time as HH:mm:ss.SSS to match CSV format
            String startTimeStr = getCurrentTimeFormatted();
            // sort
            c.CountingSort(arrayToBeSorted, k);
            // Record end time in same format
            endOfSort(i, arrayToBeSorted, startTimeStr, run);
        }
    }

    // Helper method to get current time in the same format as the CSV
    private static String getCurrentTimeFormatted() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        return sdf.format(new Date());
    }

    // Parse CSV file into list of string arrays
    public static List<String[]> parseCsvWithQuotes(String filePath) {
        // List to hold all rows
        List<String[]> rows = new ArrayList<>();

        // Open file for reading using BufferedReader
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split line into columns, handling commas inside quotes
                String[] columns = line.split("(?<!\"),");
                for (int i = 0; i < columns.length; i++) {
                    // Remove surrounding quotes from each field
                    columns[i] = columns[i].replaceAll("^\"|\"$", "");
                }
                // Add processed row to list
                rows.add(columns);
            }
        } catch (IOException e) {
            // Print stack trace on error
            e.printStackTrace();
        }

        // Return list of rows
        return rows;
    }

    // Modified to take time strings instead of milliseconds
    public static double estimateEnergy(String csvPath, String startTimeStr, String endTimeStr) throws IOException {
        // null checks
        if (csvPath == null || csvPath.isEmpty()) {
            throw new IllegalArgumentException("CSV path cannot be null or empty");
        }

        // convert start and end times to milliseconds
        long startMillis = timeStrToMillisSinceMidnight(startTimeStr);
        long endMillis = timeStrToMillisSinceMidnight(endTimeStr);

        // time range invalid
        if (endMillis < startMillis) {
            throw new IllegalArgumentException("End time must be after start time");
        }

        // convert energyreadings.csv to list of strings representing rows
        List<String[]> rows = parseCsvWithQuotes(csvPath);

        // empty csv file
        if (rows.isEmpty()) {
            throw new IOException("CSV file is empty");
        }

        // time is 2nd column in csv
        int timeIndex = 1;
        // power is 3rd column in csv
        int powerIndex = 2;

        // accumulative energy counter
        double totalEnergy = 0;
        // timestamp kept in memory for interpolation
        long prevTimestamp = -1;
        // power kept in memory for interpolation
        double prevPower = 0;
        boolean foundFirstPointInRange = false;

        // Process each data row (skipping header)
        for (int i = 1; i < rows.size(); i++) {
            try {
                // Extract timestamp string from row
                String timeStr = rows.get(i)[timeIndex];
                // Skip row if timestamp is empty
                if (timeStr.isEmpty()) {
                    System.err.println("Skipping line " + i + ": empty timestamp");
                    continue;
                }

                // Convert timestamp string to milliseconds since midnight
                long timestamp = timeStrToMillisSinceMidnight(timeStr);

                // Three possible cases: before range, in range, after range
                if (timestamp < startMillis) {
                    // Before start of target range - store value for possible interpolation
                    prevTimestamp = timestamp;
                    try {
                        // Parse power value for interpolation
                        prevPower = Double.parseDouble(rows.get(i)[powerIndex]);
                    } catch (NumberFormatException e) {
                        // Handle invalid power value
                        prevPower = 0;
                        System.err.println("Invalid power value at line " + i + ": " + rows.get(i)[powerIndex]);
                    }

                } else if (timestamp <= endMillis) {
                    // Timestamp within target range - process data
                    double power;
                    try {
                        // Parse power value
                        power = Double.parseDouble(rows.get(i)[powerIndex]);
                    } catch (NumberFormatException e) {
                        // Skip invalid data
                        System.err.println("Skipping line " + i + ": invalid power value: " + rows.get(i)[powerIndex]);
                        continue;
                    }

                    // Handle first data point in range
                    if (!foundFirstPointInRange) {
                        foundFirstPointInRange = true;

                        // If previous point exists, interpolate at start boundary
                        if (prevTimestamp != -1 && prevTimestamp < startMillis) {
                            // Linear interpolation of power at startMillis
                            double ratio = (double) (startMillis - prevTimestamp) / (timestamp - prevTimestamp);
                            double interpolatedPower = prevPower + ratio * (power - prevPower);
                            // Use interpolated value as starting point
                            prevTimestamp = startMillis;
                            prevPower = interpolatedPower;
                        } else {
                            // No previous point, use current point as starting reference
                            prevTimestamp = timestamp;
                            prevPower = power;
                        }
                    } else {
                        // Calculate energy between previous and current point
                        double deltaTimeSec = (timestamp - prevTimestamp) / 1000.0;
                        if (deltaTimeSec > 0) {
                            // Energy = Power × Time
                            double energySegment = prevPower * deltaTimeSec;
                            totalEnergy += energySegment;
                            // Update previous point values
                            prevTimestamp = timestamp;
                            prevPower = power;
                        } else {
                            // Handle non-increasing timestamp (invalid data)
                            System.err.println("Warning: Non-increasing timestamps at line " + i);
                        }
                    }

                } else {
                    // After target range - finalize calculation and stop processing
                    if (prevTimestamp != -1 && prevTimestamp <= endMillis) {
                        try {
                            // Linear interpolation of power at endMillis
                            // Calculate energy from last point to end boundary
                            double deltaTimeSec = (endMillis - prevTimestamp) / 1000.0;
                            double energySegment = prevPower * deltaTimeSec;
                            totalEnergy += energySegment;
                        } catch (NumberFormatException e) {
                            // Handle invalid power value
                            System.err.println("Invalid power value at line " + i + ": " + rows.get(i)[powerIndex]);
                        }
                    }
                    // Exit loop after range
                    break;
                }
            } catch (Exception e) {
                // Catch and report unexpected errors
                System.err.println("Error processing line " + i + ": " + e.getMessage());
            }
        }
// Return total energy calculated within range (in Joules)
        return totalEnergy;
    }

    /**
     * Convert timestamp string (HH:mm:ss.SSS) to milliseconds since midnight
     */
    private static long timeStrToMillisSinceMidnight(String timeStr) throws NumberFormatException {
        // Split string into hours, minutes, seconds, milliseconds
        String[] parts = timeStr.split("[:.]");
        if (parts.length != 4) {
            throw new NumberFormatException("Invalid time format: " + timeStr + ", expected HH:mm:ss.fff");
        }

        try {
            // Parse string parts into integers
            int h = Integer.parseInt(parts[0]);
            int m = Integer.parseInt(parts[1]);
            int s = Integer.parseInt(parts[2]);
            int ms = Integer.parseInt(parts[3]);

            // Validate value ranges
            if (h < 0 || h > 23 || m < 0 || m > 59 || s < 0 || s > 59 || ms < 0 || ms > 999) {
                throw new NumberFormatException("Time values out of range: " + timeStr);
            }

            // Return total milliseconds since midnight
            return ((h * 60L + m) * 60L + s) * 1000L + ms;
        } catch (NumberFormatException e) {
            // Rethrow with original input for context
            throw new NumberFormatException("Invalid time format: " + timeStr + " - " + e.getMessage());
        }
    }
}
