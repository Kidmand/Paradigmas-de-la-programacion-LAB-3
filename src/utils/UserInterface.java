package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserInterface {

    private HashMap<String, String> optionDict;

    private List<Option> options;

    public UserInterface() {
        options = new ArrayList<Option>();
        options.add(new Option("-h", "--help", 0));
        options.add(new Option("-f", "--feed", 1));
        options.add(new Option("-ne", "--named-entity", 1));
        options.add(new Option("-pf", "--print-feed", 0));
        options.add(new Option("-sf", "--stats-format", 1));

        optionDict = new HashMap<String, String>();
    }

    public Config handleInput(String[] args) {
        boolean isAnyOption = false;
        for (Integer i = 0; i < args.length; i++) {
            isAnyOption = false;
            for (Option option : options) {
                if (option.getName().equals(args[i]) || option.getLongName().equals(args[i])) {
                    isAnyOption = isAnyOption || true;
                    if (option.getNumValues() == 0) {
                        optionDict.put(option.getName(), null);
                    } else {
                        if (i + 1 < args.length && !isOption(args[i + 1])) {
                            optionDict.put(option.getName(), args[i + 1]);
                            i++;
                        } else {
                            UserInterface.printInvalidParams("Invalid inputs for '" + args[i] + "', need parameter");
                            System.exit(1);
                        }
                    }
                }
            }
            if (!isAnyOption) {
                UserInterface.printInvalidParams("Invalid arguments");
                System.exit(1);
            }
        }

        Boolean printFeed = optionDict.containsKey("-pf");
        Boolean printHelp = optionDict.containsKey("-h");

        String feedKey = optionDict.get("-f");
        String namedEntityKey = optionDict.get("-ne");
        String statsFormatKey = optionDict.get("-sf");

        return new Config(printFeed, printHelp, feedKey, namedEntityKey, statsFormatKey);
    }

    private Boolean isOption(String s) {
        Boolean res = false;
        for (Option option : options) {
            res = res || (option.getName().equals(s) || option.getLongName().equals(s));
        }

        return res;
    }

    public static void printInvalidParams(String message) {
        System.out.println("\n" + message + ". Please use -h or --help for help.");
    }

    public static void printHelp(List<FeedsData> feedsDataArray, List<String> heuristicsInfo) {
        System.out.println("Usage: make run ARGS=\"[OPTION]\"");
        System.out.println("Options:");
        System.out.println("  -h, --help: Show this help message and exit");
        System.out.println("  -f, --feed <feedKey>:                Fetch and process the feed with");
        System.out.println("                                       the specified key");
        System.out.println("                                       Available feed keys are: ");
        for (FeedsData feedData : feedsDataArray) {
            System.out.println("                                       " + feedData.getLabel());
        }
        System.out.println("  -ne, --named-entity <heuristicName>: Use the specified heuristic to extract");
        System.out.println("                                       named entities");
        System.out.println("                                       Available heuristic names are: ");
        System.out.println("                                       <name>: <description>");
        for (String heuristicInfo : heuristicsInfo) {
            System.out.println("                                       " + heuristicInfo);
        }
        System.out.println("  -pf, --print-feed:                   Print the fetched feed");
        System.out.println("  -sf, --stats-format <format>:        Print the stats in the specified format");
        System.out.println("                                       Available formats are: ");
        System.out.println("                                       cat: Category-wise stats");
        System.out.println("                                       topic: Topic-wise stats");
        System.out.println(
                "                                       rep: Repeated named entities in order (least to most)");
    }
}
