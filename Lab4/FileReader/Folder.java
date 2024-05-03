package Lab4.FileReader;

import java.io.*;
import java.util.*;

public class Folder {
    private final List<Folder> subFolders;
    private final List<Document> documents;

    Folder(List<Folder> subFolders, List<Document> documents) {
        this.subFolders = subFolders;
        this.documents = documents;
    }

    public List<Folder> getSubFolders() {
        return subFolders;
    }

    public List<Document> getDocuments() {
        return documents;
    }
    
    public static Folder fromDirectory(File dir) throws IOException {
        List<Document> documents = new ArrayList<>();
        List<Folder> subFolders = new ArrayList<>();
        
        for (File entry : Objects.requireNonNull(dir.listFiles())) {
            if (entry.isDirectory()) {
                subFolders.add(Folder.fromDirectory(entry));
            } else {
                documents.add(Document.fromFile(entry));
            }
        }
        
        return new Folder(subFolders, documents);
    }
}