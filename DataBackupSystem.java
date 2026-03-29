import java.util.*;

public class DataBackupSystem {
    private static BackupManager backupManager = new BackupManager();
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("🎯 DATA BACKUP SYSTEM v1.0\n");
        
        while(true) {
            showMenu();
            int choice = getIntInput("Enter your choice: ");
            
            try {
                switch(choice) {
                    case 1: createNewBackup(); break;
                    case 2: restoreFromBinary(); break;
                    case 3: restoreFromCompressed(); break;
                    case 4: importFromCSV(); break;
                    case 5: listAllVersions(); break;
                    case 6: cleanupOldBackups(); break;
                    case 7: backupManager.printStatistics(); break;
                    case 8: 
                        System.out.println("\n👋 Thank you for using Data Backup System!");
                        scanner.close();
                        return;
                    default:
                        System.out.println("❌ Invalid choice. Please try again.\n");
                }
            } catch(Exception e) {
                System.out.println("❌ Error: " + e.getMessage() + "\n");
            }
            System.out.println();
        }
    }
    
    private static void showMenu() {
        System.out.println("\n" + "=".repeat(25));
        System.out.println("🎯 DATA BACKUP SYSTEM");
        System.out.println("=".repeat(25));
        System.out.println("1.  📦 Create New Backup");
        System.out.println("2.  🔄 Restore from Binary");
        System.out.println("3.  🔄 Restore from Compressed");
        System.out.println("4.  📥 Import from CSV");
        System.out.println("5.  📋 List All Versions");
        System.out.println("6.  🧹 Cleanup Old Backups");
        System.out.println("7.  📊 Backup Statistics");
        System.out.println("8.  🚪 Exit");
        System.out.println("=".repeat(25));
    }
    
    private static void createNewBackup() throws Exception {
        System.out.println("\n📦 === CREATE NEW BACKUP ===");
        String description = getStringInput("Enter backup description: ");
        
        Map<String, Object> data = new HashMap<>();
        System.out.println("\nEnter data items (key=value, type):");
        
        while(true) {
            String key = getStringInput("Enter key: ");
            String value = getStringInput("Enter value: ");
            String type = getStringInput("Enter type (String/Integer/Double/Boolean) [String]: ");
            
            Object typedValue = value;
            if(!type.equalsIgnoreCase("String")) {
                try {
                    switch(type.toLowerCase()) {
                        case "integer": typedValue = Integer.parseInt(value); break;
                        case "double": typedValue = Double.parseDouble(value); break;
                        case "boolean": typedValue = Boolean.parseBoolean(value); break;
                        default: typedValue = value;
                    }
                } catch(Exception e) {
                    System.out.println("⚠️  Invalid type, using String");
                    typedValue = value;
                }
            }
            
            data.put(key, typedValue);
            System.out.printf("✅ Added: %s = %s (%s)\n", key, typedValue, typedValue.getClass().getSimpleName());
            
            if(!getYesNo("Add more items? (y/n): ")) break;
            System.out.println();
        }
        
        String backupId = backupManager.createBackup(description, data);
        System.out.println("\n🎉 Backup ID: " + backupId);
        System.out.println();
    }
    
    private static void restoreFromBinary() throws Exception {
        String filePath = getStringInput("Enter binary file path (e.g., backups/backup_*.dat): ");
        if(!new java.io.File(filePath).exists()) {
            System.out.println("❌ File not found!");
            return;
        }
        BackupData backup = backupManager.restoreFromBinary(filePath);
        printBackupDetails(backup, "Binary");
    }
    
    private static void restoreFromCompressed() throws Exception {
        String filePath = getStringInput("Enter compressed file path (e.g., backups/backup_*.dat.gz): ");
        if(!new java.io.File(filePath).exists()) {
            System.out.println("❌ File not found!");
            return;
        }
        BackupData backup = backupManager.restoreFromCompressed(filePath);
        printBackupDetails(backup, "Compressed");
    }
    
    private static void importFromCSV() throws Exception {
        String filePath = getStringInput("Enter CSV file path (e.g., backups/backup_*.csv): ");
        if(!new java.io.File(filePath).exists()) {
            System.out.println("❌ File not found!");
            return;
        }
        Map<String, Object> data = backupManager.importFromCSV(filePath);
        System.out.println("\n✅ Imported " + data.size() + " items from CSV:");
        int i = 1;
        for(Map.Entry<String, Object> entry : data.entrySet()) {
            System.out.printf("  %d. %s = %s (%s)\n", i++, 
                entry.getKey(), entry.getValue(), 
                entry.getValue().getClass().getSimpleName());
        }
        System.out.println();
    }
    
    private static void listAllVersions() throws Exception {
        System.out.println("\n📋 === ALL BACKUP VERSIONS ===\n");
        List<BackupData> versions = backupManager.listAllVersions();
        
        if(versions.isEmpty()) {
            System.out.println("📭 No backup versions found. Create some backups first!\n");
            return;
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(int i = 0; i < versions.size(); i++) {
            BackupData backup = versions.get(i);
            String status = i == 0 ? "🆕 Most Recent" : String.format("📅 %d days ago", 
                (int)((System.currentTimeMillis() - backup.getBackupTime().getTime()) / (1000*60*60*24)));
            
            System.out.printf("Version %d: %s\n", i+1, status);
            System.out.printf("  🆔 ID: %s\n", backup.getBackupId().substring(0, 8) + "...");
            System.out.printf("  ⏰ Time: %s\n", sdf.format(backup.getBackupTime()));
            System.out.printf("  📝 Description: %s\n", backup.getDescription());
            System.out.printf("  📦 Items: %d\n\n", backup.getData().size());
        }
        System.out.printf("📊 Total Versions: %d\n\n", versions.size());
    }
    
    private static void cleanupOldBackups() {
        System.out.println("\n🧹 === CLEANUP OLD BACKUPS ===");
        int days = getIntInput("Enter days to keep backups (e.g., 7, 30): ");
        System.out.printf("\n🗑️  Cleaning up backups older than %d days...\n", days);
        backupManager.cleanupOldBackups(days);
    }
    
    private static void printBackupDetails(BackupData backup, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("\n✅ BACKUP RESTORED FROM " + format.toUpperCase() + "!");
        System.out.println("-".repeat(50));
        System.out.printf("🆔 Backup ID: %s\n", backup.getBackupId());
        System.out.printf("⏰ Time: %s\n", sdf.format(backup.getBackupTime()));
        System.out.printf("📝 Description: %s\n", backup.getDescription());
        System.out.printf("📦 Data Items: %d\n", backup.getData().size());
        System.out.println("-".repeat(50));
        
        System.out.println("📋 Data Contents:");
        List<Map.Entry<String, Object>> dataList = new ArrayList<>(backup.getData().entrySet());
        for(int i = 0; i < dataList.size(); i++) {
            Map.Entry<String, Object> entry = dataList.get(i);
            System.out.printf("  %d. %s = %s (%s)\n", i+1,
                entry.getKey(), entry.getValue(),
                entry.getValue().getClass().getSimpleName());
        }
        System.out.println();
    }
    
    // === UTILITY METHODS ===
    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    private static int getIntInput(String prompt) {
        while(true) {
            try {
                String input = getStringInput(prompt);
                return Integer.parseInt(input);
            } catch(NumberFormatException e) {
                System.out.println("❌ Please enter a valid number!");
            }
        }
    }
    
    private static boolean getYesNo(String prompt) {
        while(true) {
            String response = getStringInput(prompt).toLowerCase();
            if(response.startsWith("y") || response.startsWith("yes")) return true;
            if(response.startsWith("n") || response.startsWith("no")) return false;
            System.out.println("❌ Please enter 'y' or 'n'");
        }
    }
}