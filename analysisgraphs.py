import pandas as pd
import matplotlib.pyplot as plt
from sklearn.linear_model import LinearRegression
import numpy as np

plt.rcParams['font.family'] = 'Times New Roman'
plt.rcParams['font.size'] = 14

# Load the CSV data
data = pd.read_csv('outputFileApril.csv', header=0)

# Process the data
data['base_case'] = data['case'].str.replace(r' - \d+$', '', regex=True)
data = data[data['energy'] != 0]

shapes = {"Best Case": "o", "Worst Case": "s", "Random Case": "v"}

# Calculate mean energy for each run number, base_name, and case
averaged_data = data.groupby(['run number', 'name', 'base_case']).agg({'energy': 'mean'}).reset_index()

# Create a scatter plot with a line for each unique base_name and cases
for name, group in averaged_data.groupby('name'):
    if name == "Quicksort":
        plt.figure(figsize=(10, 6))
        for case, case_group in group.groupby('base_case'):
            if case == "Worst Case":
                case_group = case_group.sort_values('run number')  # Sort by 'run number' so the lines connect the points in order
                # plt.scatter(case_group['run number']*np.log10(case_group['run number']), case_group['energy'], label=f'{case}', marker=shapes[case])
                # plt.plot(case_group['run number']*np.log10(case_group['run number']), case_group['energy'], linestyle='-')  # Line connecting the points
                #
                # # Prepare data for linear regression
                # X = (case_group['run number']*np.log10(case_group['run number'])).values.reshape(-1, 1)  # Input Size Squared
                # y = case_group['energy'].values


                plt.scatter(case_group['run number']**2, case_group['energy'], label=f'{case}', marker=shapes[case], color='#2b9f3c')
                plt.plot(case_group['run number']**2, case_group['energy'], linestyle='-', color='#2b9f3c')  # Line connecting the points

                # Prepare data for linear regression
                X = (case_group['run number']**2).values.reshape(-1, 1)  # Input Size Squared
                y = case_group['energy'].values

                # Perform linear regression
                model = LinearRegression()
                model.fit(X, y)
                r_squared = model.score(X, y)  # Get the R^2 value

                # Print the R^2 value for each case
                print(f'{name} - {case} R^2: {r_squared:.4f}')

        plt.title(f'Average Energy for {name}')
        plt.xlabel('Input Size Squared (n²)')
        plt.ylabel('Average Energy (J)')
        plt.legend()
        plt.tight_layout()
        plt.savefig(f'algopics/{name}Worst.png')
        plt.close()

print("Scatter charts with lines have been saved as PNG files.")
