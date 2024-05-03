package Lab4.Task1;

import java.io.*;
import java.util.concurrent.*;
import java.util.*;

import Lab4.FileReader.*;

public class WordLengths {
    private final ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

    class DocumentLengthsTask extends RecursiveTask<List<Integer>> {
        private final Document document;
        
        DocumentLengthsTask(Document document) {
            this.document = document;
        }
        
        @Override
        protected List<Integer> compute() {
            return lengthCount(document);
        }
        
        private List<Integer> lengthCount(Document document) {
            List<Integer> wordLengths = new ArrayList<>();
            for (String line : document.getLines()) {
                String[] words = line.split("\\s+");
                for (String word : words) {
                    int length = word.length();
                    wordLengths.add(length);
                }
            }
            return wordLengths;
        }
    }
    
    class FolderLengthsTask extends RecursiveTask<List<Integer>> {
        private final Folder folder;
    
        FolderLengthsTask(Folder folder) {
            this.folder = folder;
        }
    
        @Override
        protected List<Integer> compute() {
            List<Integer> allLengths = new ArrayList<>();
            List<RecursiveTask<List<Integer>>> tasks = new LinkedList<>();
    
            for (Document document : folder.getDocuments()) {
                DocumentLengthsTask task = new DocumentLengthsTask(document);
                tasks.add(task);
                task.fork();
            }
    
            for (RecursiveTask<List<Integer>> task : tasks) {
                allLengths.addAll(task.join());
            }
    
            return allLengths;
        }
    }

    public List<Integer> analyzeDocuments(Folder folder) {
        FolderLengthsTask task = new FolderLengthsTask(folder);
        return forkJoinPool.invoke(task);
    }

    public static void main(String[] args) throws IOException {
        File directory = new File("./Lab4/texts/");
        Folder folder = Folder.fromDirectory(directory);
        
        WordLengths analyzer = new WordLengths();
        List<Integer> lengths = analyzer.analyzeDocuments(folder);
        
        int totalCharacters = 0;
        int totalWords = lengths.size();
        int minWordLength = Integer.MAX_VALUE;
        int maxWordLength = Integer.MIN_VALUE;
        for (int length : lengths) {
            totalCharacters += length;
            minWordLength = Math.min(minWordLength, length);
            maxWordLength = Math.max(maxWordLength, length);
        }
        double averageWordLength = (double) totalCharacters / totalWords;
        
        System.out.println("Total characters: " + totalCharacters);
        System.out.println("Total words: " + totalWords);
        System.out.println("Average word length: " + averageWordLength);
        System.out.println("Minimum word length: " + minWordLength);
        System.out.println("Maximum word length: " + maxWordLength);
    }
}