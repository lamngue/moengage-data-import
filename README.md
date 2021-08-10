# moengage-data-import

## This is a small Java program that collects data from CSV, Excel or AWS Redshift and feed it into MoEngage via the Data API [here](https://docs.moengage.com/docs/data-import-apis)

To run the import, simply select the file to upload (either CSV or Excel) and click on *Import Data*. If none is selected, the program will automatically fetch data from AWS Redshift. 

The program supports CSV and Excel files with names contains "Loan_portfolio" and "CIF".
