import java.nio.file.*;
import java.util.*;

public class FileUtils {

    public static boolean compare(String f1, String f2) throws Exception {
        return Arrays.equals(
                Files.readAllBytes(Paths.get(f1)),
                Files.readAllBytes(Paths.get(f2))
        );
    }

    public static void mergeCSV(String f1, String f2, String out) throws Exception {
        List<String> lines = new ArrayList<>();
        lines.addAll(Files.readAllLines(Paths.get(f1)));
        lines.addAll(Files.readAllLines(Paths.get(f2)));
        Files.write(Paths.get(out), lines);
    }
}