import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.UUID;

class BackupData implements Serializable {
    private static final long serialVersionUID = 1L;
    private String backupId;
    private Date backupTime;
    private Map<String, Object> data;
    private String description;
    
    public BackupData(String description, Map<String, Object> data) {
        this.backupId = UUID.randomUUID().toString();
        this.backupTime = new Date();
        this.description = description;
        this.data = new HashMap<>(data);
    }
    
    public void saveToBinaryFile(String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filePath))) {
            oos.writeObject(this);
        }
    }
    
    public void saveToCompressedFile(String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new GZIPOutputStream(new FileOutputStream(filePath)))) {
            oos.writeObject(this);
        }
    }
    
    public void exportToCSV(String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Key,Value,Type\n");
            for(Map.Entry<String, Object> entry : data.entrySet()) {
                writer.write(String.format("\"%s\",\"%s\",\"%s\"\n",
                    entry.getKey(),
                    entry.getValue().toString(),
                    entry.getValue().getClass().getSimpleName()));
            }
        }
    }
    
    // Getters
    public String getBackupId() { return backupId; }
    public Date getBackupTime() { return backupTime; }
    public String getDescription() { return description; }
    public Map<String, Object> getData() { return new HashMap<>(data); }
    
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return String.format("Backup ID: %s\nTime: %s\nDescription: %s\nData Items: %d",
                           backupId, sdf.format(backupTime), description, data.size());
    }
}