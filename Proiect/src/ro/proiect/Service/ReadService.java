package ro.proiect.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ReadService {
    private static ReadService instance = null;

    private ReadService() {

    }

    public static ReadService getInstance() {
        if (instance == null)
            instance = new ReadService();
        return instance;
    }

    public ArrayList<ArrayList<String>> read(String path) throws IOException {
        ArrayList<ArrayList<String>> objects = new ArrayList<>();
        File file = new File(path);
        Scanner myReader = new Scanner(file);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            objects.add(new ArrayList<>(Arrays.asList(data.split(","))));
        }
        return new ArrayList<>(objects);
    }
}
