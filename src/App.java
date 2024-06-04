import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.List;

import feed.Article;
import feed.FeedParser;

import heuristics.Heuristic;
import heuristics.HeuristicsTools;

import namedEntities.NamedEntityStorage;
import namedEntities.dictionary.DictionaryStorage;

import utils.*;

public class App {

    public static void main(String[] args) {

        List<FeedsData> feedsDataArray = new ArrayList<>();
        try {
            feedsDataArray = JSONParser.parseJsonFeedsData("src/data/feeds.json");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        UserInterface ui = new UserInterface();
        Config config = ui.handleInput(args);

        List<Heuristic> heuristics = HeuristicsTools.getHeuristics();

        run(config, feedsDataArray, heuristics);
    }

    private static void run(Config config, List<FeedsData> feedsDataArray, List<Heuristic> heuristics) {

        // Print help
        if (config.getPrintHelp()) {
            UserInterface.printHelp(feedsDataArray, HeuristicsTools.getHeuristicsInfo(heuristics));
            return;
        }

        // Check if there are feeds data
        if (feedsDataArray == null || feedsDataArray.size() == 0) {
            System.out.println("No feeds data found");
            return;
        }

        // Check if the feed key is valid and get the selected feed data
        FeedsData selectedFeedData = null;
        if (config.getFeedKey()) {

            for (FeedsData feedData : feedsDataArray) {
                if (feedData.getLabel().equals(config.getFeedKeyParam())) {
                    selectedFeedData = feedData;
                    break;
                }
            }

            if (selectedFeedData == null) {
                UserInterface.printInvalidParams("Invalid feed option");
                return;
            }
        }

        // Fetch the feeds
        List<Article> allArticles = null;
        if (!(config.getPrintHelp() && !config.getPrintFeed() && !config.getNamedEntityKey())) {
            allArticles = listArticles(selectedFeedData, feedsDataArray);
            if (allArticles == null) {
                System.out.println("Error fetching feed");
                return;
            }
        }

        // Print feed
        if (config.getPrintFeed() || (!config.getNamedEntityKey() && !config.getPrintHelp())) {
            printLine();
            System.out.println("Printing feed(s) ");

            for (Article article : allArticles) {
                article.print();
                printLine();
            }
        }

        // Named entities
        if (config.getNamedEntityKey()) {
            // Message with the selected heuristic name
            System.out.println("Computing named entities using '" + config.getNamedEntityKeyParam() + "' heuristic.");

            // Load dictionary
            DictionaryStorage dictionary = new DictionaryStorage("src/data/dictionary.json");

            // Select heuristic
            Heuristic heuristic = selectHeuristic(config.getNamedEntityKeyParam(), heuristics);
            if (heuristic == null) {
                UserInterface.printInvalidParams("Invalid named entity heuristic option");
                return;
            }

            // Extract candidates
            List<String> candidates = extractCandidatesFromAllArticles(heuristic, allArticles);

            // Create named entity storage
            NamedEntityStorage namedEntityStorage = new NamedEntityStorage(dictionary);
            for (String namedEntity : candidates) {
                namedEntityStorage.addElement(namedEntity);
            }

            // ------------------------------------------------------------------------------------------------

            // Print stats
            System.out.println("\nStats: ");
            Statistics statistics = new Statistics(namedEntityStorage);
            if (config.getStatsFormatKey()) {
                if (config.getStatsFormatKeyParam().equals("cat")) {
                    statistics.printAnalysisCategories();
                } else if (config.getStatsFormatKeyParam().equals("topic")) {
                    statistics.printAnalysisTopics();
                } else if (config.getStatsFormatKeyParam().equals("rep")) {
                    statistics.printAnalysisMoreRepeatedInOrder();
                } else {
                    UserInterface.printInvalidParams("Invalid stats format option");
                }
            } else {
                // Default stats format is "cat".
                statistics.printAnalysisCategories();
            }

            // Print line
            printLine();
        } else if (!config.getNamedEntityKey() && config.getStatsFormatKey()) {
            UserInterface.printInvalidParams("Invalid, for use stats format option, use named entity option too");
        }
    }

    private static List<Article> listArticles(FeedsData selected, List<FeedsData> feedsDataArray) {
        List<Article> allArticles = new ArrayList<>();
        try {
            if (selected != null) {
                String feedData = FeedParser.fetchFeed(selected.getUrl());
                if (selected.getType().equals("xml")) {
                    allArticles = FeedParser.parseXML(feedData);
                } else {
                    UserInterface.printInvalidParams("Invalid feed type format, not supported yet");
                    return null;
                }
            } else { // If no feed is selected, fetch all feeds
                for (FeedsData feedData : feedsDataArray) {
                    String feedDataString = FeedParser.fetchFeed(feedData.getUrl());
                    if (feedData.getType().equals("xml")) {
                        allArticles.addAll(FeedParser.parseXML(feedDataString));
                    } else {
                        UserInterface.printInvalidParams("Invalid feed type format, not supported yet");
                        return null;
                    }
                }
            }
        } catch (MalformedInputException e) {
            System.out.println("Error fetching feed, malformed input");
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            System.out.println("Error fetching feed, IO exception");
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            System.out.println("Error fetching feed, exception");
            e.printStackTrace();
            return null;
        }

        return allArticles;
    }

    private static Heuristic selectHeuristic(String heuristicName, List<Heuristic> heuristics) {
        if (heuristicName == null) {
            return null;
        }
        for (Heuristic heuristic : heuristics) {
            if (heuristic.getName().equals(heuristicName)) {
                return heuristic;
            }
        }
        return null;
    }

    private static List<String> extractCandidatesFromAllArticles(Heuristic heuristic, List<Article> allArticles) {
        List<String> candidates = new ArrayList<>();
        for (Article article : allArticles) {
            candidates.addAll(heuristic.extractCandidates(article.getTitle()));
            candidates.addAll(heuristic.extractCandidates(article.getDescription()));
        }
        return candidates;
    }

    // NOTE: Add this because for some versions of Java the previous version of code
    // that did this did not work.
    static private void printLine() {
        for (int i = 0; i < 80; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

}
