import java.util.*;
import java.util.regex.*;

public class LexicalAnalyzer {

    // Define regex patterns for each token type
    private static final String KEYWORD_PATTERN = "\\b(def|if|else|for|while|return|import|print|and|or)\\b";
    private static final String IDENTIFIER_PATTERN = "\\b[A-Za-z_][A-Za-z0-9_]*\\b";
    private static final String NUMBER_PATTERN = "\\b\\d+\\b";
    private static final String OPERATOR_PATTERN = "[+\\-*/=]";
    private static final String PUNCTUATION_PATTERN = "[(){}.,]";

    private static final Pattern TOKEN_PATTERN = Pattern.compile(
            String.join("|", KEYWORD_PATTERN, IDENTIFIER_PATTERN, NUMBER_PATTERN, OPERATOR_PATTERN, PUNCTUATION_PATTERN)
    );

    public Map<String, Set<String>> tokenizeAndClassify(String line) {
        Map<String, Set<String>> classifiedTokens = new HashMap<>();
        classifiedTokens.put("Keywords", new HashSet<>());
        classifiedTokens.put("Identifiers", new HashSet<>());
        classifiedTokens.put("Numbers", new HashSet<>());
        classifiedTokens.put("Operators", new HashSet<>());
        classifiedTokens.put("Punctuation", new HashSet<>());

        Matcher matcher = TOKEN_PATTERN.matcher(line);

        while (matcher.find()) {
            String token = matcher.group();
            if (token.matches(KEYWORD_PATTERN)) {
                classifiedTokens.get("Keywords").add(token);
            } else if (token.matches(IDENTIFIER_PATTERN)) {
                classifiedTokens.get("Identifiers").add(token);
            } else if (token.matches(NUMBER_PATTERN)) {
                classifiedTokens.get("Numbers").add(token);
            } else if (token.matches(OPERATOR_PATTERN)) {
                classifiedTokens.get("Operators").add(token);
            } else if (token.matches(PUNCTUATION_PATTERN)) {
                classifiedTokens.get("Punctuation").add(token);
            }
        }

        return classifiedTokens;
    }
}