# DCN-2024-Fall-Lab6

# TCP Protocol Implementation Report

Student Name: Xu, Vincent 
NYU ID: yx2021
N-Number: N13337595
Course: DCN Fall 2024  
Section: 001  
Date: November 5, 2024

I hereby state that I have completed this assignment independently.
Vincent Xu

## Implementation Overview

This project implements a TCP Protocol state machine that models the connection establishment, data transfer, 
and connection termination phases of TCP. The implementation uses a Finite State Machine (FSM) framework to manage 
state transitions and handle various TCP events.

### Key Components

1. **States**
    - CLOSED: Initial state
    - LISTEN: Server waiting for connection
    - SYN_RCVD: Server received SYN
    - SYN_SENT: Client initiated connection
    - ESTABLISHED: Connection established
    - FIN_WAIT_1: Initiating connection termination
    - FIN_WAIT_2: First FIN acknowledged
    - CLOSING: Both sides initiating close
    - TIME_WAIT: Waiting for network to clear
    - CLOSE_WAIT: Remote side initiated close
    - LAST_ACK: Waiting for final ACK

2. **Events**
    - PASSIVE: Server initialization
    - ACTIVE: Client initialization
    - SYN: Synchronization request
    - SYNACK: Synchronization acknowledgment
    - ACK: Acknowledgment
    - RDATA: Receive data
    - SDATA: Send data
    - FIN: Close connection
    - CLOSE: Application close
    - TIMEOUT: Time wait timeout

### Design Details

1. **Class Structure**
    - `Main.java`: Entry point, handles input processing
    - `TcpProtocol.java`: Core FSM implementation
    - `TcpState.java`: State representation
    - `TcpEvent.java`: Event representation
    - `TcpAction.java`: Action handling

2. **State Machine Implementation**
    - Uses composition with the FSM framework
    - Implements all standard TCP state transitions
    - Handles data transfer in ESTABLISHED state
    - Maintains counters for sent and received data

3. **Error Handling**
    - Invalid events are rejected with error messages
    - FSM exceptions are caught and reported
    - Input validation for all events

### Test Cases

The implementation includes a test framework (`runTest.sh`) that verifies:
1. Basic connection establishment
2. Data transfer functionality
3. Connection termination sequences

### Key Features

1. **Robust Event Processing**
    - All TCP events are properly handled
    - State transitions follow RFC specifications
    - Data transfer tracking in ESTABLISHED state

2. **Error Management**
    - Comprehensive error handling
    - Invalid state transitions blocked
    - Clear error reporting

3. **Maintainability**
    - Clean code structure
    - Well-documented methods
    - Modular design

## Usage Instructions

1. **Compilation**
   ```bash
   javac -cp ./lib/Fsm.jar -d ./out ./src/*.java
   ```

2. **Running Tests**
   ```bash
   ./runTest.sh        # Run all tests
   ./runTest.sh 3      # Run specific test
   ```

3. **Input Format**
    - One event per line
    - Valid events: PASSIVE, ACTIVE, SYN, SYNACK, ACK, RDATA, SDATA, FIN, CLOSE, TIMEOUT

## Conclusion

This implementation successfully models a finite state machine that implements TCP protocol, providing a reliable 
framework for understanding and testing TCP connection management. The code is structured for maintainability and 
includes comprehensive error handling, making it suitable for educational and testing purposes.