import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class MultiThreadProcessor {

    private final int numThreads;
    private final LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();

    public MultiThreadProcessor(int numThreads) {
        this.numThreads = numThreads;
    }

    public void processFile(String inputFilePath, String outputFilePath) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int chunkSize = (lines.size() + numThreads - 1) / numThreads;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Future<Map<String, Set<String>>>> futures = new ArrayList<>();

        for (int i = 0; i < numThreads; i++) {
            final int start = i * chunkSize;
            final int end = Math.min(start + chunkSize, lines.size());
            List<String> chunk = lines.subList(start, end);
            futures.add(executor.submit(() -> processChunk(chunk)));
        }
        executor.shutdown();

        Map<String, Set<String>> globalClassifiedTokens = new HashMap<>();
        initializeMap(globalClassifiedTokens);

        try {
            for (Future<Map<String, Set<String>>> future : futures) {
                Map<String, Set<String>> classifiedTokens = future.get();
                mergeClassifications(globalClassifiedTokens, classifiedTokens);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        writeClassifiedTokens(outputFilePath, globalClassifiedTokens);
    }

    private Map<String, Set<String>> processChunk(List<String> chunk) {
        Map<String, Set<String>> localClassifiedTokens = new HashMap<>();
        initializeMap(localClassifiedTokens);

        for (String line : chunk) {
            Map<String, Set<String>> classifiedTokens = lexicalAnalyzer.tokenizeAndClassify(line);
            mergeClassifications(localClassifiedTokens, classifiedTokens);
        }

        return localClassifiedTokens;
    }

    private void initializeMap(Map<String, Set<String>> map) {
        map.put("Keywords", new HashSet<>());
        map.put("Identifiers", new HashSet<>());
        map.put("Numbers", new HashSet<>());
        map.put("Operators", new HashSet<>());
        map.put("Punctuation", new HashSet<>());
    }

    private void mergeClassifications(Map<String, Set<String>> global, Map<String, Set<String>> local) {
        for (Map.Entry<String, Set<String>> entry : local.entrySet()) {
            global.get(entry.getKey()).addAll(entry.getValue());
        }
    }

    private void writeClassifiedTokens(String outputFilePath, Map<String, Set<String>> classifiedTokens) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFilePath))) {
            for (Map.Entry<String, Set<String>> entry : classifiedTokens.entrySet()) {
                writer.println(entry.getKey() + ": " + entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
