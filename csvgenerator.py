import random
import csv
import os

# Set a single global output directory
output_path = r"/yourpath/"
os.makedirs(output_path, exist_ok=True)

def generate_bubble_sort_datasets():
    sizes = [25000, 50000, 75000, 100000, 200000, 300000, 400000, 500000] # List of dataset sizes for BubbleSort

    # Worst-case: Reverse sorted
    for size in sizes: # Loop over each size
        for _ in range(1): # Generate 1 CSV for each size
            data = list(range(size, 0, -1)) # Generate a reverse sorted list (worst case)
            save_to_csv("BubbleSort_worst_case(400)", data, size) # Save the dataset to a CSV file

    # Best Case: Already sorted
    for size in sizes:
        for _ in range(1):
            data = list(range(0, size)) # Generate 1 CSV for each size
            save_to_csv("BubbleSort_best_case(400)", data, size) # Generate an already sorted list (best case)

    # Random
    for size in sizes:
        for _ in range(10): # Generate 10 CSVs for each size
            data = [random.randint(-size, size) for _ in range(size)] # Generate a list of random integers
            save_to_csv("BubbleSort_random(40)", data, size)

def generate_counting_sort_datasets():
    sizes = [25000, 50000, 75000, 100000, 200000, 300000, 400000, 500000, 600000, 700000, 800000, 900000, 1000000] # List of dataset sizes for CountingSort
    
    # Worst-case: Randomly sorted, big k
    for size in sizes:
        for _ in range(1): # Generate 1 CSV for each size
            data = [random.randint(0, 100000000) for _ in range(size)] # Generate random integers with large values
            save_to_csv("CountingSort_worst_case(30)", data, size)

    # Best Case: Randomly sorted, small k
    for size in sizes:
        for _ in range(1): # Generate 1 CSV for each size
            data = [random.randint(0, 10) for _ in range(size)] # Generate random integers within a small range (0 to 10)
            save_to_csv("CountingSort_best_case(30)", data, size)

def generate_merge_sort_datasets():
    sizes = [25000, 50000, 75000, 100000, 200000, 300000, 400000, 500000, 600000, 700000, 800000, 900000, 1000000] # List of dataset sizes for MergeSort
 
    # Worst-case: Alternating elements
    for size in sizes:
        for _ in range(1): # Generate 1 CSV for each size
            data = [i if i % 2 == 0 else size - i for i in range(size)] # Generate alternating elements pattern
            save_to_csv("MergeSort_worst_case(30)", data, size)

    # Best Case: Already sorted
    for size in sizes:
        for _ in range(1): # Generate 1 CSV for each size
            data = list(range(0, size))
            save_to_csv("MergeSort_best_case(30)", data, size)

    # Random
    for size in sizes:
        for _ in range(10): # Generate 10 CSVs for each size
            data = [random.randint(-size, size) for _ in range(size)]
            save_to_csv("MergeSort_random(3)", data, size)

def generate_quick_sort_datasets():
    sizes = [25000, 50000, 75000, 100000, 125000, 150000, 175000, 200000] # List of dataset sizes for QuickSort worst-case

    # Worst-case: Reverse sorted
    for size in sizes:
        for _ in range(1): # Generate 1 CSV for each size
            data = list(range(size, 0, -1))
            save_to_csv("QuickSort_worst_case(30)", data, size)

    sizes = [25000, 50000, 75000, 100000, 200000, 300000, 400000, 500000, 600000, 700000, 800000, 900000, 1000000]

    # Best Case: Evenly partitioned
    for size in sizes:
        for _ in range(1): # Generate 1 CSV for each size
            data = sorted([random.randint(0, size) for _ in range(size)])
            save_to_csv("QuickSort_best_case(30)", data, size)
    
    # Random
    for size in sizes:
        for _ in range(10): # Generate 10 CSVs for each size
            data = [random.randint(-size, size) for _ in range(size)]
            save_to_csv("QuickSort_random(3)", data, size)

def save_to_csv(case_type, data, size):
    filename = f"{output_path}\\{case_type}_{size}.csv" # Define the filename using the case type and size
    with open(filename, 'w', newline='') as csvfile: # Open the CSV file for writing
        writer = csv.writer(csvfile) # Create a CSV writer object
        writer.writerow(data) # Write the data as a row in the CSV file

# Generate datasets for all sorts
generate_bubble_sort_datasets()
generate_counting_sort_datasets()
generate_merge_sort_datasets()
generate_quick_sort_datasets()