package Lab4.FileReader;

import java.io.*;
import java.util.*;

public class Document {
    private final List<String> lines;
    public final String name;

    public Document(List<String> lines, String name) {
        this.lines = lines;
        this.name = name;
    }

    public List<String> getLines() {
        return lines;
    }
    
    public static Document fromFile(File file) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return new Document(lines, file.getName());
    }
}
