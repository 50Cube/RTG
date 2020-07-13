package rtg.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import rtg.exceptions.FileOperationsException;
import rtg.model.Point;

import java.io.*;
import java.util.List;

public class FileOperations {

    private Gson gson;
    private final String FILE_NAME = "points.json";

    public FileOperations() {
        this.gson = new GsonBuilder().create();
    }

    public void savePointsToFile(List<Point> list) throws FileOperationsException {
        try {
            Writer writer = new FileWriter(FILE_NAME);
            gson.toJson(list, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new FileOperationsException();
        }
    }

    public List<Point> readPointsFromFile() throws FileOperationsException {
        try {
            JsonReader reader = new JsonReader(new FileReader(FILE_NAME));
            return new Gson().fromJson(reader, new TypeToken<List<Point>>() {}.getType());
        } catch (FileNotFoundException e) {
            throw new FileOperationsException();
        }
    }
}
