import services.AwsService;
import services.FilesService;
import services.MessageService;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class main {


    public static void main(String args[]) throws IOException {
        final String FILES_PATH = args[0];

        System.out.println("Synchronize files with AWS");

        AwsService.checkCredentials();

        //get 15 files - two oldest, but not oldest than 3Mo, and rest - the newest
        List<File> localRelevantFiles = FilesService.getRelevantLocalFiles(FILES_PATH);
        if (!localRelevantFiles.isEmpty()) {
            MessageService.showMessageAboutFilesToSync(localRelevantFiles);
            AwsService.synchronizeFiles(localRelevantFiles);
        }
    }


}

