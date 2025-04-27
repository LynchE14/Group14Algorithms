import pyRAPL
import csv
import time
from datetime import datetime

pyRAPL.setup()

# Create a meter for measuring CPU package power
meter = pyRAPL.Measurement('cpu_power', 'package')

# Function to get current date and time
def get_current_datetime():
    now = datetime.now()
    date_str = now.strftime('%d.%m.%Y')
    time_str = now.strftime('%H:%M:%S.%f')[:-3]  # trim microseconds for readability
    return date_str, time_str

# Open CSV file in append mode
with open('cpu_power.csv', mode='a', newline='') as file:
    writer = csv.writer(file)

    # Write header if needed
    writer.writerow(["Date", "Time", "CPU Package Power [W]"])

    try:
        while True:
            # Measure CPU package power
            # Begin measurement
            meter.begin()

            # Sleep for 500 milliseconds (0.5 seconds)
            time.sleep(0.1)

            # End measurement
            meter.end()

            # Retrieve the energy consumption in microjoules and convert to joules
            energy = meter.result.pkg  # package energy in microjoules

            # Convert energy in microjoules to power in watts
            power = energy[0] / 0.1 / 1_000_000  # (Joules/s = Watts)

            # Get current date and time
            date_str, time_str = get_current_datetime()

            # Write a new row to the CSV file
            writer.writerow([date_str, time_str, power])

            file.flush()

    except KeyboardInterrupt:
        print("Measurement stopped.")
