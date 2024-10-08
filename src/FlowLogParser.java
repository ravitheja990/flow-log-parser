import java.io.*;
import java.util.*;

public class FlowLogParser {

    static class FlowLog {
        String destinationPort;
        String protocol;

        FlowLog(String destinationPort, String protocol) {
            this.destinationPort = destinationPort;
            this.protocol = protocol;
        }

        @Override
        public String toString() {
            return "FlowLog{" +
                    "destinationPort='" + destinationPort + '\'' +
                    ", protocol='" + protocol + '\'' +
                    '}';
        }
    }

    static class LookupEntry {
        String destinationPort;
        String protocol;
        String tag;

        LookupEntry(String destinationPort, String protocol, String tag) {
            this.destinationPort = destinationPort;
            this.protocol = protocol;
            this.tag = tag;
        }

        @Override
        public String toString() {
            return "LookupEntry{" +
                    "destinationPort='" + destinationPort + '\'' +
                    ", protocol='" + protocol + '\'' +
                    ", tag='" + tag + '\'' +
                    '}';
        }
    }

    private static List<LookupEntry> loadLookupTable(String lookupFilePath) throws IOException {
        List<LookupEntry> lookupEntries = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(lookupFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("dstport")) {
                    continue; // skip the header
                }
                String[] tokens = line.split(",");
                if (tokens.length != 3) {
                    // If we don't have exactly 3 columns, something is wrong—skip it
                    continue;
                }
                // Add a new LookupEntry with port, protocol (converted to lowercase), and tag
                lookupEntries.add(new LookupEntry(tokens[0], tokens[1].toLowerCase(), tokens[2]));
            }
        }
        return lookupEntries;
    }

    private static String findTagForFlow(FlowLog log, List<LookupEntry> lookupEntries) {
        for (LookupEntry entry : lookupEntries) {
            if (entry.destinationPort.equals(log.destinationPort) && entry.protocol.equals(log.protocol)) {
                return entry.tag;
            }
        }
        return "Untagged"; // If no match is found
    }

    private static void processFlowLogs(String flowLogFilePath, List<LookupEntry> lookupEntries, String outputFilePath) throws IOException {
        Map<String, Integer> tagCounts = new HashMap<>();
        Map<String, Integer> portProtocolCounts = new HashMap<>();

        // Read through the flow log file line by line
        try (BufferedReader reader = new BufferedReader(new FileReader(flowLogFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(" ");
                if (tokens.length < 8) {
                    // This doesn't seem like a valid log entry, so we'll just skip it
                    continue;
                }

                String destinationPort = tokens[5];
                String protocol = getProtocolFromCode(tokens[7]);

                FlowLog log = new FlowLog(destinationPort, protocol);
                String tag = findTagForFlow(log, lookupEntries);

                // Update tag counts
                tagCounts.put(tag, tagCounts.getOrDefault(tag, 0) + 1);

                // Update port/protocol combination counts
                String portProtocolKey = destinationPort + "," + protocol;
                portProtocolCounts.put(portProtocolKey, portProtocolCounts.getOrDefault(portProtocolKey, 0) + 1);
            }
        }

        // Write the results to the output file
        writeResultsToFile(tagCounts, portProtocolCounts, outputFilePath);
    }

    private static String getProtocolFromCode(String protocolCode) {
        switch (protocolCode) {
            case "6":
                return "tcp";
            case "17":
                return "udp";
            default:
                return "icmp";
        }
    }

    private static void writeResultsToFile(Map<String, Integer> tagCounts, Map<String, Integer> portProtocolCounts, String outputFilePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            // Writing tag counts
            writer.write("Tag Counts:\n");
            writer.write("Tag,Count\n");
            for (Map.Entry<String, Integer> entry : tagCounts.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }

            // Writing port/protocol combination counts
            writer.write("\nPort/Protocol Combination Counts:\n");
            writer.write("Port,Protocol,Count\n");
            for (Map.Entry<String, Integer> entry : portProtocolCounts.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
        }
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Please provide: <lookupFilePath> <flowLogFilePath> <outputFilePath>");
            return;
        }

        String lookupFilePath = args[0];
        String flowLogFilePath = args[1];
        String outputFilePath = args[2];

        System.out.println("Starting flow log processing...");

        try {
            List<LookupEntry> lookupEntries = loadLookupTable(lookupFilePath);
            processFlowLogs(flowLogFilePath, lookupEntries, outputFilePath);
            System.out.println("Processing complete! Results saved to: " + outputFilePath);
        } catch (IOException e) {
            System.err.println("Error: Could not process files. " + e.getMessage());
        }
    }
}