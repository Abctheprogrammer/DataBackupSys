
import java.io.*;
import java.util.*;

public class VersionManager {

    private static final String DIR = "versions/";

    static {
        new File(DIR).mkdirs();
    }

    public static void saveVersion(BackupData data) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(DIR + data.getBackupId() + ".dat"))) {
            oos.writeObject(data);
        }
    }

    public static List<BackupData> getVersions() throws Exception {

        List<BackupData> list = new ArrayList<>();
        File folder = new File(DIR);

        for (File f : folder.listFiles()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
                list.add((BackupData) ois.readObject());
            }
        }

        list.sort((a, b) -> b.getBackupTime().compareTo(a.getBackupTime()));
        return list;
    }
}