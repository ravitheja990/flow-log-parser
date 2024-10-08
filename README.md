Flow Log Parser

This project is a flow log parser that maps each row from a flow log file to a corresponding tag based on a lookup table. The lookup table contains destination port and protocol combinations, which are used to assign tags to each flow log entry. The output includes counts of matches for each tag and port/protocol combination.

Assumptions

The program only supports the default log format (as shown in the sample flow logs provided in the prompt).
Version 2 of the flow log is the only version supported.
The log parsing is case-insensitive when matching protocol types (tcp, udp, etc.).
The lookup table can map multiple tags to different port/protocol combinations.
Non-matching log entries are categorized as "Untagged."


Directory Structure
bash

flow-log-parser
│
├── .idea
│
├── out
│   └── production
│       └── flow-log-parser
│           ├── FlowLogParser
│           ├── FlowLog
│           └── LookupEntry
│
├── src
│   └── FlowLogParser
│
├── tests
│   └── test1
│       ├── flowlog.txt
│       ├── lookup.csv
│       └── output.txt
│
├── .gitignore
├── flow-log-parser.iml
│
└── External Libraries
    └── Scratches and Consoles


How to Compile and Run

Requirements
Java 8 or later should be installed on your system.
The project is intended to be run using IntelliJ IDEA or the command line.

Compiling
To compile the program, follow these steps:

Open the project in IntelliJ IDEA, or navigate to the src/ directory from the command line.
Compile the FlowLogTagger.java file either through IntelliJ or using the command line:
bash
Copy code
javac FlowLogTagger.java

Running
To run the program, you need to pass three command-line arguments: the path to the lookup table (lookup.csv), the flow log file (flowlog.txt), and the output file (output.txt).

Command to run:
(run inside flow-log-parser repo/dir after compiling the code)
structure --
java -classpath <path-to-compiled-class-in-out-folder> FlowLogParser tests/test3/lookup.csv tests/test3/flowlog.txt tests/test3/output.txt

My Example:
java -classpath /Users/ravitheja/Documents/Projects/flow-log-parser/out/production/flow-log-parser FlowLogParser tests/test3/lookup.csv tests/test3/flowlog.txt tests/test3/output.txt


Running in IntelliJ
Open the Run/Debug Configurations in IntelliJ.
Set the Program Arguments as follows:
bash
Copy code
src/lookup.csv src/flowlog.txt output.txt
Run the program from IntelliJ.
Running from the Command Line
Navigate to the project directory where the compiled FlowLogTagger.class file is located.
Run the program:
bash
Copy code
java FlowLogTagger src/lookup.csv src/flowlog.txt output.txt
Output
The program generates an output.txt file containing the following sections:

Tag Counts: Count of occurrences for each tag found.
Port/Protocol Combination Counts: Count of occurrences for each unique port/protocol combination.
Example
bash
Copy code
Tag Counts:
Tag,Count
sv_P2,1
sv_P1,2
email,3
Untagged,8

Port/Protocol Combination Counts:
Port,Protocol,Count
23,tcp,1
993,tcp,1
49321,tcp,1
80,tcp,1
25,tcp,1
443,tcp,1
110,tcp,1
49153,tcp,1
143,tcp,1
1030,tcp,1
56000,tcp,1
49152,tcp,1
49154,tcp,1
1024,tcp,1

Tests Performed

Basic Functionality Test:
Tested with the provided flow log and lookup table to ensure correct tag mapping and counting.
Verified output for tag counts and port/protocol combination counts.

Untagged Entries:
Tested logs that do not have corresponding entries in the lookup table to ensure they are counted as "Untagged."

Edge Case Handling:

Logs with unsupported protocols (e.g., ICMP).
Logs where the lookup table does not include specific ports.

Code Analysis:
The program reads the lookup table and flow log in a line-by-line manner, ensuring memory efficiency for large files (up to 10MB as specified).
The logic is simple and modular, with clear separation between file reading, log parsing, and result generation.
Case insensitivity is maintained for protocol matching (tcp, udp).
Untagged entries are handled separately, ensuring comprehensive coverage of all log entries.
No external libraries are used, adhering to the requirement of using only Java standard libraries.

Possible Future Enhancements:
Custom Log Format Support: Add flexibility to support different log versions or custom formats.
Protocol Handling: Extend the program to handle more protocols beyond TCP/UDP (like ICMP, SCTP, etc.).
Multithreading: For larger datasets, introduce multithreading to improve performance during file parsing.
