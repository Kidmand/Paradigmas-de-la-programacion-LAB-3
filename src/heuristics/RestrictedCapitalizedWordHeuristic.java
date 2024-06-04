package heuristics;

import java.text.Normalizer;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RestrictedCapitalizedWordHeuristic extends Heuristic {

    public RestrictedCapitalizedWordHeuristic() {
        super("restrict-capitalized",
                "Extracts capitalized words that are not immediately preceded by . ? ; ! from the text.");
    }

    public List<String> extractCandidates(String text) {
        List<String> candidates = new ArrayList<>();

        text = text.replaceAll("[-+^,\"]", "");
        text = Normalizer.normalize(text, Normalizer.Form.NFD);
        text = text.replaceAll("\\p{M}", "");

        Pattern pattern = Pattern.compile("(?![:.?!;]\\s*)\\b[A-Z][a-z]+(?:\\s[A-Z][a-z]+)*");

        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            candidates.add(matcher.group());
        }
        return candidates;
    }
}
