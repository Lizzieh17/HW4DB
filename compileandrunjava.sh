#!/bin/bash
set -e -v

echo "Creating and filling tables from HW3 solution..."
./hw3_soln.sh

echo "Compiling..."
javac *.java

echo "Running..."
java -cp .:mysql-connector-java-5.1.40-bin.jar jdbc_example
