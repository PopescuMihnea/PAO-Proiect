package ro.proiect.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class WriteService {

    private static WriteService instance = null;

    private WriteService() {

    }

    public static WriteService getInstance() {
        if (instance == null)
            instance = new WriteService();
        return instance;
    }

    public void clear(String path) throws IOException {
        FileWriter file = new FileWriter(path);
        BufferedWriter write = new BufferedWriter(file);
        write.close();
    }

    public void write(String path, String message) throws IOException {
        FileWriter file = new FileWriter(path, true);
        BufferedWriter write = new BufferedWriter(file);
        write.write(message);
        write.close();
    }
}
