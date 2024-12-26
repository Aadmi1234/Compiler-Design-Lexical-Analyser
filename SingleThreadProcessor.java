import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class SingleThreadProcessor {
    Map<String, Set<String>> globalClassifiedTokens ;
    SingleThreadProcessor(){
        globalClassifiedTokens = new HashMap<>();
        globalClassifiedTokens.put("Keywords", new HashSet<>());
        globalClassifiedTokens.put("Identifiers", new HashSet<>());
        globalClassifiedTokens.put("Numbers", new HashSet<>());
        globalClassifiedTokens.put("Operators", new HashSet<>());
        globalClassifiedTokens.put("Punctuation", new HashSet<>());
    }
    private final LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();

    public void processFile(String inputFilePath, String outputFilePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
             PrintWriter writer = new PrintWriter(new FileWriter(outputFilePath))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                // Tokenize and classify each line
                Map<String, Set<String>> classifiedTokens = lexicalAnalyzer.tokenizeAndClassify(line);
                mergeClassifications(globalClassifiedTokens, classifiedTokens);
            }

            writeClassifiedTokens(outputFilePath, globalClassifiedTokens);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeClassifiedTokens(String outputFilePath, Map<String, Set<String>> classifiedTokens) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFilePath))) {
            for (Map.Entry<String, Set<String>> entry : classifiedTokens.entrySet()) {
                writer.println(entry.getKey() + ": " + entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mergeClassifications(Map<String, Set<String>> global, Map<String, Set<String>> local) {
        for (Map.Entry<String, Set<String>> entry : local.entrySet()) {
            global.get(entry.getKey()).addAll(entry.getValue());
        }
    }
}