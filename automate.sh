#!/bin/bash

# 1. Generate dummy code

echo "Step 1 : Generating a dummy file..."
read -p "Give a number whose multiple 40 lines of dummy code should be generated: " number
read -p "Enter the dummy_code file name: " input_file

if ! [[ "$number" =~ ^[0-9]+$ ]]; then
    echo "Error: Please enter a valid number."
    exit 1
fi

start_time=$(date +%s%3N) # Record the start time

python3 dummy_code_generator.py "$number" "$input_file"

end_time=$(date +%s%3N) # Record the end time
elapsed_time=$((end_time - start_time))
lines_generated=$(expr 40 \* $number)
echo "Time taken to generate the dummy code file of ${lines_generated} lines : ${elapsed_time} ms."

# 2. Compiling all the java classes

# Clean up old class files
rm -f *.class 2>/dev/null
echo "Step 2: Compiling all the Java classes..."

# Compile Java files
javac *.java
if [[ $? -eq 0 ]]; then
    echo "Java files compiled successfully."
else
    echo "Error: Failed to compile Java files."
    exit 1
fi

echo "All Java files compiled successfully."

# 3. Lexical Analysis

# 3.1 Take inputs from the user
echo "Input file taken as $input_file"
read -p "Enter the output file name: " output_file
read -p "Enter the number of threads: " num_threads

# Validate inputs
if [[ -z "$input_file" || -z "$output_file" || -z "$num_threads" ]]; then
    echo "Error: All inputs (input file, output file, number of threads) are required."
    exit 1
fi
if ! [[ "$num_threads" =~ ^[0-9]+$ ]]; then
    echo "Error: Number of threads must be a valid number."
    exit 1
fi

java Driver "$input_file" "$output_file" "$num_threads"
rm -f *.class 2>/dev/null
