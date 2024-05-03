package Lab4.Task4;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import Lab4.FileReader.*;

class DocumentSearchTask extends RecursiveTask<List<Document>> {
    private final Folder folder;
    private final Set<String> keywords;

    DocumentSearchTask(Folder folder, Set<String> keywords) {
        this.folder = folder;
        this.keywords = keywords;
    }

    @Override
    protected List<Document> compute() {
        List<Document> matchingDocuments = new ArrayList<>();

        for (Document document : folder.getDocuments()) {
            if (containsKeywords(document)) {
                matchingDocuments.add(document);
            }
        }

        List<RecursiveTask<List<Document>>> tasks = new ArrayList<>();
        for (Folder subFolder : folder.getSubFolders()) {
            DocumentSearchTask task = new DocumentSearchTask(subFolder, keywords);
            tasks.add(task);
            task.fork();
        }

        for (RecursiveTask<List<Document>> task : tasks) {
            matchingDocuments.addAll(task.join());
        }

        return matchingDocuments;
    }

    private boolean containsKeywords(Document document) {
        for (String line : document.getLines()) {
            for (String keyword : keywords) {
                if (line.contains(keyword)) {
                    return true;
                }
            }
        }
        return false;
    }
}

class ITDocumentSearch {
    private final ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

    public List<Document> findDocuments(Folder folder, Set<String> keywords) {
        DocumentSearchTask task = new DocumentSearchTask(folder, keywords);
        return forkJoinPool.invoke(task);
    }

    public static void main(String[] args) throws IOException {
        File directory = new File("./Lab4/texts/");
        Folder folder = Folder.fromDirectory(directory);

        Set<String> keywords = new HashSet<>();
        keywords.add("programming");
        keywords.add("software");
        keywords.add("technology");

        ITDocumentSearch searcher = new ITDocumentSearch();
        List<Document> matchingDocuments = searcher.findDocuments(folder, keywords);

        System.out.println("Matching documents found: " + matchingDocuments.size());
        for (Document document : matchingDocuments) {
            System.out.println("Document: " + document.name);
        }
    }
}
