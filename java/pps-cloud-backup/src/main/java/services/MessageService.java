package services;

import java.io.File;
import java.util.Date;
import java.util.List;

public class MessageService {
    public static void showMessageAboutFilesToSync(List<File> files) {
        System.out.println("Files to synchronization:");
        int i = 0;
        for (File f : files) {
            i++;
            System.out.println(i + " - " + f.getName() + " - " + new Date(f.lastModified()));
        }
    }

    public static void emptyPathMessage() {
        System.out.println("There is no files to synchronisation");
    }
}
