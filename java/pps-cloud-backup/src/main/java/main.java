import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class main {

    public static final int NEWEST_FILE_INDEX = 0;
    public static final int NUMBER_OF_FILES_TO_SYNC = 15;
    public static final int NUMBER_OF_OLDEST_FILES = 2;
    public static final int OLDEST_FILE_IN_DAYS = 90;


    public static void main(String args[]) throws IOException {
        final String FILES_PATH = args[0];
        System.out.println("Synchronize files with AWS");

        //check if its first logging if yes than ask about credentials to AWS

        //get files
        //remain 15 files - two oldest but not oldest than 3Mo and rest the newest
        List<File> localRelevantFiles = getRelevantLocalFiles(FILES_PATH);
        showMessageAboutFilesToSync(localRelevantFiles);

        //synchronize files with AWS

    }

    private static List<File> getRelevantLocalFiles(String path) {
        List<File> files = new ArrayList<>(new File(path).listFiles());

        if (files.size() <= NUMBER_OF_FILES_TO_SYNC) {
            return files;
        }

        List<File> oldestFiles = findOldestRelevantFiles(files);


        return files;
    }

    private static List<File> findOldestRelevantFiles(List<File> files) {
        List<File> receivedFiles = new ArrayList<>(files);
        Collections.sort(receivedFiles, Comparator.comparingLong(File::lastModified).reversed());
        receivedFiles.subList(receivedFiles.size() - NUMBER_OF_FILES_TO_SYNC, receivedFiles.size()).clear();

        ArrayList<File> oldestFiles = receivedFiles.stream().filter(file -> file.lastModified() >
                new Date().getTime() - TimeUnit.DAYS.toMillis(OLDEST_FILE_IN_DAYS))
                .collect(Collectors.toCollection(ArrayList::new));


        if (oldestFiles.size() <= NUMBER_OF_FILES_TO_SYNC) {
            return new ArrayList<>();
        } else if (files.size() <= NUMBER_OF_FILES_TO_SYNC + 1) {
            return files.get(0).lastModified() < new Date().getTime() ?
        }

        return new File[0];
    }

    private static void showMessageAboutFilesToSync(List<File> files) {
        System.out.println("Files to synchronization:");
        int i = 0;
        for (File f : files) {
            i++;
            System.out.println(i + " - " + f.getName() + " - " + f.lastModified());
        }
    }
}

