package heuristics;

import java.text.Normalizer;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilteredCapitalizedWordHeuristic extends Heuristic {

    public FilteredCapitalizedWordHeuristic() {
        super("filtered-capitalized", "Extracts filtered capitalized words from the text.");
    }

    public List<String> extractCandidates(String text) {
        List<String> candidates = new ArrayList<>();

        text = text.replaceAll("[-+.^:,\"]", "");
        text = Normalizer.normalize(text, Normalizer.Form.NFD);
        text = text.replaceAll("\\p{M}", "");

        String wordsToFilter = "(\\bLes\\b|\\bLe\\b|\\bPara\\b|\\bTe\\b|\\bYo\\b|\\bEs\\b|\\bHay\\b|\\bSon\\b|\\bPor\\b|\\bSera\\b|\\bAca\\b|\\bSe\\b|\\bDe\\b|\\bEn\\b|\\bHubo\\b|\\bA\\b|\\bSegun\\b|\\bEsta\\b|\\bSi\\b|\\bNo\\b|\\bCon\\b|\\bComo\\b|\\bEllas\\b|\\bEl\\b|\\bLa\\b|\\bLas\\b|\\bLo\\b|\\bLos\\b|\\bUn\\b|\\bUna\\b|\\bUnas\\b|\\bUno\\b|\\bNosotros\\b|\\bVosotros\\b|\\bEllos\\b|\\bEllas\\b|\\bUstedes\\b|\\bMi\\b|\\bTu\\b|\\bSu\\b|\\bMis\\b|\\bTus\\b|\\bSus\\b|\\bNuestro\\b|\\bNuestros\\b|\\bNuestra\\b|\\bNuestras\\b)";
        // OPTIONAL: agregar a wordsToFilter todo lo que no sea un nombre basicamente.
        String capitalizedWordPattern = "(([A-Z]+[a-z]+)|([A-Z]{2,}[a-z]*))";
        String oneWordPattern = "(?!" + wordsToFilter + ")" + capitalizedWordPattern;
        String multiWordsPattern = capitalizedWordPattern + "(\\s" + capitalizedWordPattern + ")+";
        // System.err.println(oneWordPattern + "\n");
        // System.err.println(multiWordsPattern + "\n");
        Pattern pattern = Pattern.compile(oneWordPattern + "|" + multiWordsPattern);

        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            candidates.add(matcher.group());
        }
        return candidates;
    }
}
