import java.io.*;
import java.util.*;

public class FlowLogParser {

    private static final Map<String, String> protocolMap = new HashMap<>();

    static {
        protocolMap.put("0", "hopopt");
        protocolMap.put("1", "icmp");
        protocolMap.put("2", "igmp");
        protocolMap.put("3", "ggp");
        protocolMap.put("4", "ipv4");
        protocolMap.put("5", "st");
        protocolMap.put("6", "tcp");
        protocolMap.put("7", "cbt");
        protocolMap.put("8", "egp");
        protocolMap.put("9", "igp");
        protocolMap.put("10", "bbn-rcc-mon");
        protocolMap.put("11", "nvp-ii");
        protocolMap.put("12", "pup");
        protocolMap.put("13", "argus");
        protocolMap.put("14", "emcon");
        protocolMap.put("15", "xnet");
        protocolMap.put("16", "chaos");
        protocolMap.put("17", "udp");
        protocolMap.put("18", "mux");
        protocolMap.put("19", "dcn-meas");
        protocolMap.put("20", "hmp");
        protocolMap.put("21", "prm");
        protocolMap.put("22", "xns-idp");
        protocolMap.put("23", "trunk-1");
        protocolMap.put("24", "trunk-2");
        protocolMap.put("25", "leaf-1");
        protocolMap.put("26", "leaf-2");
        protocolMap.put("27", "rdp");
        protocolMap.put("28", "irtp");
        protocolMap.put("29", "iso-tp4");
        protocolMap.put("30", "netblt");
        protocolMap.put("31", "mfe-nsp");
        protocolMap.put("32", "merit-inp");
        protocolMap.put("33", "dccp");
        protocolMap.put("34", "3pc");
        protocolMap.put("35", "idpr");
        protocolMap.put("36", "xtp");
        protocolMap.put("37", "ddp");
        protocolMap.put("38", "idpr-cmtp");
        protocolMap.put("39", "tp++");
        protocolMap.put("40", "il");
        protocolMap.put("41", "ipv6");
        protocolMap.put("42", "sdrp");
        protocolMap.put("43", "ipv6-route");
        protocolMap.put("44", "ipv6-frag");
        protocolMap.put("45", "idrp");
        protocolMap.put("46", "rsvp");
        protocolMap.put("47", "gre");
        protocolMap.put("48", "dsr");
        protocolMap.put("49", "bna");
        protocolMap.put("50", "esp");
        protocolMap.put("51", "ah");
        protocolMap.put("52", "i-nlsp");
        protocolMap.put("53", "swipe");
        protocolMap.put("54", "narp");
        protocolMap.put("55", "min-ipv4");
        protocolMap.put("56", "tlsp");
        protocolMap.put("57", "skip");
        protocolMap.put("58", "ipv6-icmp");
        protocolMap.put("59", "ipv6-nonxt");
        protocolMap.put("60", "ipv6-opts");
        protocolMap.put("61", "any host internal protocol");
        protocolMap.put("62", "cftp");
        protocolMap.put("63", "any local network");
        protocolMap.put("64", "sat-expak");
        protocolMap.put("65", "kryptolan");
        protocolMap.put("66", "rvd");
        protocolMap.put("67", "ippc");
        protocolMap.put("68", "any distributed file system");
        protocolMap.put("69", "sat-mon");
        protocolMap.put("70", "visa");
        protocolMap.put("71", "ipcv");
        protocolMap.put("72", "cpnx");
        protocolMap.put("73", "cphb");
        protocolMap.put("74", "wsn");
        protocolMap.put("75", "pvp");
        protocolMap.put("76", "br-sat-mon");
        protocolMap.put("77", "sun-nd");
        protocolMap.put("78", "wb-mon");
        protocolMap.put("79", "wb-expak");
        protocolMap.put("80", "iso-ip");
        protocolMap.put("81", "vmtp");
        protocolMap.put("82", "secure-vmtp");
        protocolMap.put("83", "vines");
        protocolMap.put("84", "iptm");
        protocolMap.put("85", "nsfnet-igp");
        protocolMap.put("86", "dgp");
        protocolMap.put("87", "tcf");
        protocolMap.put("88", "eigrp");
        protocolMap.put("89", "ospfigp");
        protocolMap.put("90", "sprite-rpc");
        protocolMap.put("91", "larp");
        protocolMap.put("92", "mtp");
        protocolMap.put("93", "ax.25");
        protocolMap.put("94", "ipip");
        protocolMap.put("95", "micp");
        protocolMap.put("96", "scc-sp");
        protocolMap.put("97", "etherip");
        protocolMap.put("98", "encap");
        protocolMap.put("99", "any private encryption scheme");
        protocolMap.put("100", "gmtp");
        protocolMap.put("101", "ifmp");
        protocolMap.put("102", "pnni");
        protocolMap.put("103", "pim");
        protocolMap.put("104", "aris");
        protocolMap.put("105", "scps");
        protocolMap.put("106", "qnx");
        protocolMap.put("107", "a/n");
        protocolMap.put("108", "ipcomp");
        protocolMap.put("109", "snp");
        protocolMap.put("110", "compaq-peer");
        protocolMap.put("111", "ipx-in-ip");
        protocolMap.put("112", "vrrp");
        protocolMap.put("113", "pgm");
        protocolMap.put("114", "any 0-hop protocol");
        protocolMap.put("115", "l2tp");
        protocolMap.put("116", "ddx");
        protocolMap.put("117", "iatp");
        protocolMap.put("118", "stp");
        protocolMap.put("119", "srp");
        protocolMap.put("120", "uti");
        protocolMap.put("121", "smp");
        protocolMap.put("122", "sm");
        protocolMap.put("123", "ptp");
        protocolMap.put("124", "isis over ipv4");
        protocolMap.put("125", "fire");
        protocolMap.put("126", "crtp");
        protocolMap.put("127", "crudp");
        protocolMap.put("128", "sscopmce");
        protocolMap.put("129", "iplt");
        protocolMap.put("130", "sps");
        protocolMap.put("131", "pipe");
        protocolMap.put("132", "sctp");
        protocolMap.put("133", "fc");
        protocolMap.put("134", "rsvp-e2e-ignore");
        protocolMap.put("135", "mobility header");
        protocolMap.put("136", "udplite");
        protocolMap.put("137", "mpls-in-ip");
        protocolMap.put("138", "manet");
        protocolMap.put("139", "hip");
        protocolMap.put("140", "shim6");
        protocolMap.put("141", "wesp");
        protocolMap.put("142", "rohc");
        protocolMap.put("143", "ethernet");
        protocolMap.put("144", "aggfrag");
        protocolMap.put("145", "nsh");
        protocolMap.put("253", "use for experimentation and testing");
        protocolMap.put("254", "use for experimentation and testing");
        protocolMap.put("255", "reserved");
    }

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

//    private static String getProtocolFromCode(String protocolCode) {
//        switch (protocolCode) {
//            case "6":
//                return "tcp";
//            case "17":
//                return "udp";
//            default:
//                return "icmp";
//        }
//    }

    private static String getProtocolFromCode(String protocolCodeStr) {
        return protocolMap.getOrDefault(protocolCodeStr, "unknown protocol");
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