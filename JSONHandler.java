
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;

public class JSONHandler {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void writeJSON(BackupData data, String path) throws IOException {
        try (FileWriter writer = new FileWriter(path)) {
            gson.toJson(data, writer);
        }
    }

    public static BackupData readJSON(String path) throws IOException {
        try (FileReader reader = new FileReader(path)) {
            return gson.fromJson(reader, BackupData.class);
        }
    }
}