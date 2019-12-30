# modbus-registry-scanner

I needed this so it's quick and dirty so please excuse the structure. Please use as you see fit for your own needs.

It was developed to query all known registers as found in Systemairs Variable list (PDF) for their SAVE ventilators. I have included a CSV-version of all registers found in the file.

This is a complete IntelliJ-project. If you are not familiar with Java, and/or IntelliJ I suggest you start with downloading the IntelliJ Community IDE and opening the project folder. Developed and tested with Java 8.

Code files are:
* src/main/java/ScanRegistersFromCsv.java
* src/main/java/ScanRegisterByRange.java


Usage:
1. Modify src/main/java/ScanRegistersFromCsv.java and/or src/main/java/ScanRegisterByRange.java with ip address and port
2. Run file in IntelliJ

Author: Stian Indal Haugseth
