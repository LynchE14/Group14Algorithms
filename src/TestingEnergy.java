import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;

public class TestingEnergy {
    public static void main(String[] args) throws Exception {
        String logFilePath = "C:/Users/eoinl/Downloads/energyReadings.csv";

        // Define input sizes to test
        int[] inputSizes = {10000000, 20000000, 30000000, 40000000, 50000000};
        List<Double> energyResults = new ArrayList<>();

        // Run algorithm with different input sizes and measure energy
        for (int n : inputSizes) {
            // Format the current time as HH:mm:ss.SSS to match CSV format
            String startTimeStr = getCurrentTimeFormatted();
            System.out.println("Starting algorithm with size " + n + " at " + startTimeStr);

            // Run algorithm with the current input size
            runMyAlgorithm(n);

            // Record end time in same format
            String endTimeStr = getCurrentTimeFormatted();
            System.out.println("Finished algorithm at " + endTimeStr);

            double energyConsumed = estimateEnergy(logFilePath, startTimeStr, endTimeStr);
            energyResults.add(energyConsumed);
            System.out.printf("Input size %d: Estimated energy used: %.2f Joules%n", n, energyConsumed);

            // Add a delay between tests to ensure distinct time ranges
            Thread.sleep(1000);
        }
    }

    // Helper method to get current time in the same format as the CSV
    private static String getCurrentTimeFormatted() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        return sdf.format(new Date());
    }

    public static void runMyAlgorithm(int n) {
        // Simulate some work - but don't flood console with output
        long sum = 0;
        for (int i = 0; i < n; i++) {
            sum += i;
            for (int j = 0; j < 1000; j++) {
                // Simulate some computation
                sum += j;
            }

            // Only print occasionally to avoid console flooding
            if (i % 10000 == 0) {
                System.out.println("Progress: " + i + "/" + n);
            }
        }
        System.out.println("Algorithm completed. Sum: " + sum);
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
        // Basic sanity checks before we do anything else
        if (csvPath == null || csvPath.isEmpty()) {
            throw new IllegalArgumentException("CSV path cannot be null or empty");
        }

        System.out.println("Analyzing energy between " + startTimeStr + " and " + endTimeStr);

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

        System.out.println("Read " + rows.size() + " lines from CSV file");

        // Find the columns we care about from the header row
        System.out.println("Raw headers: " + rows.get(0));

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
                        System.out.println("Found first point in range: " + timeStr + " with power " + power + "W");

                        // If we have a point before the range, we can interpolate exactly at the start boundary
                        if (prevTimestamp != -1 && prevTimestamp < startMillis) {
                            // Simple linear interpolation between the previous point and current point
                            double ratio = (double) (startMillis - prevTimestamp) / (timestamp - prevTimestamp);
                            double interpolatedPower = prevPower + ratio * (power - prevPower);

                            // Use this interpolated value as our starting point
                            prevTimestamp = startMillis;
                            prevPower = interpolatedPower;
                            System.out.println("Interpolated power at start time: " + interpolatedPower + "W");
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
                            System.out.println("Adding energy segment: " + energySegment +
                                    "J (power: " + prevPower + "W, time: " + deltaTimeSec + "s)");
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
                            System.out.println("Adding final energy segment to boundary: " + energySegment +
                                    "J (power: " + prevPower + "W, time: " + deltaTimeSec + "s)");
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid power value at line " + i + ": " + rows.get(i)[powerIndex]);
                        }
                    }
                    System.out.println("Reached point after time range, stopping processing");
                    break; // No need to process more data beyond our range
                }
            } catch (Exception e) {
                System.err.println("Error processing line " + i + ": " + e.getMessage());
            }
        }

        System.out.println("Found " + pointsInRange + " data points in the specified time range");
        System.out.println("Total energy calculated: " + totalEnergy + " Joules");
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