# Group14Algorithms
Repository for group 14 Algorithms project code

Run Instructions

The first step is creating the sample input data for the sorting algorithms. This was done using a Python script that generates the data and saves it to multiple CSV files in different folders.
There are various parameters in 
To generate the files, run `csvgenerator.py` and ensure the resulting four directories are in the CSVFILES directory.

Next, the energy monitoring must be set up.
This works using another Python script that accesses RAPL through the pyRAPL library, which can be installed using the command `pip install pyrapl`.
When running, this script appends the current CPU package power to a CSV file every 10 milliseconds. The CSV's location can be specified in the script.
It is important to terminate the script once testing is complete, as the .

To run the sorting algorithms, first modify the variable energyFilePath to point to the same file specified in `pyrapl.py`.
Run the `main` method in `TestingEnergy.java`. This may take several hours.
The energy used in each run of a sorting algorithm is saved to the file specified by `outputFile`. Each run is saved as a single row showing the energy used and other metadata.

The results were analyzed with a Python script, `analysisgraphs.csv`. Note that this script needs to be modified to produce specific graphs, and depends on slight modifications to the output file.
