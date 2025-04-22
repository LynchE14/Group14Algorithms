import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestingEnergy {

    static String energyFilePath = "energyreadings.csv";
    static File outputFile = new File("outputFile.csv");

    public static void main(String[] args) throws Exception {
        File[] files = makeFileArray("CSVFILES/BubbleSort/BestCase");
        int [] intArray = csvToIntArray(files[0], 25000);
        int [][] runs = copyToRunsArray(intArray, 400);
        BubbleSortArrays(runs, "BubbleSortBestCase25000");
    }

    // returns array of files to be iterated through and sorted
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

    public static int[] csvToIntArray(File file, int size) {
        int[] numbers = new int[size];
        int index = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                for (String value : values) {
                    if (index < size) {
                        numbers[index] = Integer.parseInt(value.trim());
                        index++;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Optional: check if fewer numbers were read than expected
        if (index < size) {
            throw new IllegalArgumentException("CSV file contains fewer numbers than expected size.");
        }

        return numbers;
    }

    // creates array of deep copies of array to be sorted
    public static int[][] copyToRunsArray(int[] array, int n) {
        // array of n arrays
        int[][] runs = new int[n][array.length];

        // copy over
        for (int[] run : runs) {
            System.arraycopy(array, 0, run, 0, array.length);
        }

        return runs;
    }

    // write data for each sort to csv file
    public static void outputData(double timeTaken, double energyConsumed, String nameOfRun, File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            // Format: timeTaken,energyConsumed,nameOfRun
            writer.write(timeTaken + "," + energyConsumed + "," + nameOfRun);
            writer.newLine(); // Move to the next line
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void BubbleSortArrays(int[][] arrays, String run) throws IOException, InterruptedException {
        // create instance of sort class
        BubbleSort b = new BubbleSort();
        // iterate through arrays
        int i = 0;
        for (int[] array : arrays) {
            // Format the current time as HH:mm:ss.SSS to match CSV format
            String startTimeStr = getCurrentTimeFormatted();
            // sort
            b.bubbleSort(array);
            // Record end time in same format
            String endTimeStr = getCurrentTimeFormatted();
            System.out.println("Array sorted for : " + run);

            // calculate energy consumed
            double energyConsumed = estimateEnergy(energyFilePath, startTimeStr, endTimeStr);
            // output data to output csv file
            outputData((timeStrToMillisSinceMidnight(endTimeStr) - timeStrToMillisSinceMidnight(startTimeStr)), energyConsumed, run + " - " + i, outputFile);
            i++;
            // Add a delay between tests to ensure distinct time ranges
            Thread.sleep(75);
        }
    }

    public static void MergeSortArrays(int[][] arrays, String run) throws IOException, InterruptedException {
        // create instance of sort class
        MergeSort m = new MergeSort();
        // iterate through arrays
        for (int[] array : arrays) {
            // Format the current time as HH:mm:ss.SSS to match CSV format
            String startTimeStr = getCurrentTimeFormatted();
            // sort
            m.mergeSort(array);
            // Record end time in same format
            String endTimeStr = getCurrentTimeFormatted();
            System.out.println("Array sorted: " + run);

            // calculate energy consumed
            double energyConsumed = estimateEnergy(energyFilePath, startTimeStr, endTimeStr);
            // output data to output csv file
            outputData((timeStrToMillisSinceMidnight(endTimeStr) - timeStrToMillisSinceMidnight(startTimeStr)), energyConsumed, run, outputFile);

            // Add a delay between tests to ensure distinct time ranges
            Thread.sleep(500);
        }
    }

    public static void QuickSortArrays(int[][] arrays, String run) throws IOException, InterruptedException {
        // create instance of sort class
        QuickSort q = new QuickSort();
        // iterate through arrays
        for (int[] array : arrays) {
            // Format the current time as HH:mm:ss.SSS to match CSV format
            String startTimeStr = getCurrentTimeFormatted();
            // sort
            q.quickSort(array, 0, array.length - 1);
            // Record end time in same format
            String endTimeStr = getCurrentTimeFormatted();
            System.out.println("Array sorted: " + run);

            // calculate energy consumed
            double energyConsumed = estimateEnergy(energyFilePath, startTimeStr, endTimeStr);
            // output data to output csv file
            outputData((timeStrToMillisSinceMidnight(endTimeStr) - timeStrToMillisSinceMidnight(startTimeStr)), energyConsumed, run, outputFile);

            // Add a delay between tests to ensure distinct time ranges
            Thread.sleep(500);
        }
    }

    public static void CountingSortArrays(int[][] arrays, String run, int k) throws IOException, InterruptedException {
        // create instance of sort class
        CountingSort c = new CountingSort();
        // iterate through arrays
        for (int[] array : arrays) {
            // Format the current time as HH:mm:ss.SSS to match CSV format
            String startTimeStr = getCurrentTimeFormatted();
            // sort
            c.CountingSort(array, k);
            // Record end time in same format
            String endTimeStr = getCurrentTimeFormatted();
            System.out.println("Array sorted: " + run);

            // calculate energy consumed
            double energyConsumed = estimateEnergy(energyFilePath, startTimeStr, endTimeStr);
            // output data to output csv file
            outputData((timeStrToMillisSinceMidnight(endTimeStr) - timeStrToMillisSinceMidnight(startTimeStr)), energyConsumed, run, outputFile);

            // Add a delay between tests to ensure distinct time ranges
            Thread.sleep(500);
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