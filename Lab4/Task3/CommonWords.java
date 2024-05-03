package Lab4.Task3;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import Lab4.FileReader.*;

class CommonWords {
    private final ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

    static class DocumentWordsTask extends RecursiveTask<List<String>> {
        private final Document document;

        DocumentWordsTask(Document document) {
            this.document = document;
        }

        @Override
        protected List<String> compute() {
            List<String> uniqueWords = new ArrayList<>();
            for (String line : document.getLines()) {
                String[] words = line.split("\\s+");
                for (String word : words) {
                    if (!uniqueWords.contains(word)) {
                        uniqueWords.add(word);
                    }
                }
            }
            return uniqueWords;
        }
    }

    static class FolderWordsTask extends RecursiveTask<List<String>> {
        private final Folder folder;

        FolderWordsTask(Folder folder) {
            this.folder = folder;
        }

        @Override
        protected List<String> compute() {
            List<RecursiveTask<List<String>>> tasks = new ArrayList<>();
            for (Document document : folder.getDocuments()) {
                DocumentWordsTask task = new DocumentWordsTask(document);
                tasks.add(task);
                task.fork();
            }

            List<String> allWords = new ArrayList<>();
            for (RecursiveTask<List<String>> task : tasks) {
                allWords.addAll(task.join());
            }

            return allWords;
        }
    }

    public List<String> findCommonWords(Folder folder) {
        FolderWordsTask task = new FolderWordsTask(folder);
        List<String> allWords = forkJoinPool.invoke(task);

        Set<String> commonWordsSet = new HashSet<>();
        List<String> commonWords = new ArrayList<>();

        for (int i = 0; i < allWords.size(); i++) {
            String word = allWords.get(i);
            if (!commonWordsSet.add(word)) {
                if (!commonWords.contains(word)) {
                    commonWords.add(word);
                }
            }
        }

        return commonWords;
    }

    public static void main(String[] args) throws IOException {
        File directory = new File("./Lab4/texts/");
        Folder folder = Folder.fromDirectory(directory);

        CommonWords finder = new CommonWords();
        List<String> commonWords = finder.findCommonWords(folder);

        System.out.println("Common words found in multiple documents: " + commonWords);
    }
}