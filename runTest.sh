#!/bin/bash

# Function to run a single test
run_test() {
    local test_num=$1
    echo "Running test${test_num}..."

    # Ensure output directory exists
    mkdir -p test/output

    # Run the test and capture the output
    java -cp ./out:./lib/Fsm.jar Main < ./test/input/test${test_num}.in > ./test/output/test${test_num}.out

    # Compare with expected output
    if diff ./test/output/test${test_num}.out ./test/expected/test${test_num}.out > /dev/null; then
        echo "Test ${test_num} passed ✅"
    else
        echo "Test ${test_num} failed ❌"
        echo "Differences found:"
        diff ./test/output/test${test_num}.out ./test/expected/test${test_num}.out
    fi
    echo "----------------------------------------"
}

# Compile the Java code first
echo "Compiling Java code..."
javac -cp ./lib/Fsm.jar -d ./out ./src/*.java

# Check if compilation was successful
if [ $? -ne 0 ]; then
    echo "Compilation failed! ❌"
    exit 1
fi

# Run specific test if provided as argument
if [ $# -eq 1 ]; then
    run_test $1
    exit 0
fi

# Run all tests if no specific test number provided
echo "Running all tests..."
for i in {1..3}; do
    run_test $i
done