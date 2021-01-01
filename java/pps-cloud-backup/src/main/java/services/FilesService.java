package services;

import config.BasicConfigs;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class FilesService {

    private static final int NUMBER_OF_NEWEST_FILES =
            BasicConfigs.NUMBER_OF_FILES_TO_SYNC - BasicConfigs.NUMBER_OF_OLDEST_FILES;

    public static List<File> getRelevantLocalFiles(String path) {
        List<File> files = new ArrayList<>(Arrays.asList(new File(path).listFiles()));

        if (files.size() <= BasicConfigs.NUMBER_OF_FILES_TO_SYNC) {
            return files;
        }

        List<File> oldestFiles = findOldestRelevantFiles(files);

        files.subList(BasicConfigs.NUMBER_OF_FILES_TO_SYNC - oldestFiles.size(), files.size()).clear();
        files.addAll(oldestFiles);


        Collections.sort(files, Comparator.comparingLong(File::lastModified).reversed());
        return files;
    }

    private static List<File> findOldestRelevantFiles(List<File> files) {
        List<File> receivedFiles = new ArrayList<>(files);
        List<File> oldestFiles = receivedFiles.subList(0, receivedFiles.size() - NUMBER_OF_NEWEST_FILES);

        ArrayList<File> oldestRelevantFiles = oldestFiles.stream().filter(file -> file.lastModified() >
                new Date().getTime() - TimeUnit.DAYS.toMillis(BasicConfigs.OLDEST_FILE_IN_DAYS))
                .collect(Collectors.toCollection(ArrayList::new));

        if (oldestRelevantFiles.isEmpty()) {
            return new ArrayList<>();
        }

        if (oldestRelevantFiles.size() > BasicConfigs.NUMBER_OF_OLDEST_FILES) {
            oldestRelevantFiles.subList(BasicConfigs.NUMBER_OF_OLDEST_FILES, oldestRelevantFiles.size()).clear();
        }

        return oldestRelevantFiles;
    }
}
