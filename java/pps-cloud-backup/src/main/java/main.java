import config.AwsConfig;
import services.FilesService;
import services.MessageService;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class main {




    public static void main(String args[]) throws IOException {
        final String FILES_PATH = args[0];

        System.out.println("Synchronize files with AWS");

        AwsConfig.checkCredentials();

        //get 15 files - two oldest, but not oldest than 3Mo, and rest - the newest
        List<File> localRelevantFiles = FilesService.getRelevantLocalFiles(FILES_PATH);
        MessageService.showMessageAboutFilesToSync(localRelevantFiles);

        //synchronize files with AWS
    }


}

