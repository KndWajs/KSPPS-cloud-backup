import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public class main {

    public static final int NEWEST_FILE_INDEX = 0;

    public static void main(String args[]) throws IOException {
        final String FILES_PATH = args[0];
        System.out.println("Synchronize files with AWS");

        //check if its first logging if yes than ask about credentials to AWS
        //get files
        //remain last 15 files + two oldest but not oldest than 3Mo
        //synchronize files with AWS

    }

    public static void  trash(String args[]) {
        final String FILES_PATH = args[0];
        File[] files = new File(FILES_PATH).listFiles();
        Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
        int i = 0;
        for (File f : files) {
            i++;
            System.out.println(i + " - " + f.getName());
            System.out.println(i + " - " + f.lastModified());
        }


//        AWS
        System.setProperty("aws.accessKeyId", "*");
        System.setProperty("aws.secretAccessKey", "*");
        S3Client s3client = S3Client.builder().region(Region.EU_CENTRAL_1).build();

        String bucketName = "kspps-bucket";

        HeadBucketRequest headBucketRequest = HeadBucketRequest.builder()
                .bucket(bucketName)
                .build();
        try {
            s3client.headBucket(headBucketRequest);
            System.out.println("jest taki bucket");
        } catch (NoSuchBucketException e) {
            s3client.createBucket(CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build());
            System.out.println("stworzony nowy bucket");
        }

        String key = files[NEWEST_FILE_INDEX].getName();
        File to = new File(args[0] + key);

        // Put Object
        s3client.putObject(PutObjectRequest.builder().bucket(bucketName).key(key)
                .build(), RequestBody.fromFile(to));

        ListObjectsV2Response objectListing = s3client.listObjectsV2(ListObjectsV2Request.builder().bucket(bucketName).build());

        for(S3Object object : objectListing.contents()) {
            System.out.println(object.key());
            System.out.println(object.lastModified());
        }
    }


    public static void copyFile(String args[]) throws IOException {

        System.out.println(System.getProperty("user.dir"));
        System.out.println(new File(".").getAbsolutePath());

        File[] files = new File(args[0]).listFiles();
        Arrays.sort(files, Comparator.comparingLong(File::lastModified));
        int i = 0;
        for (File f : files) {
            i++;
            System.out.println(i + " - " + f.getName());
            System.out.println(i + " - " + f.lastModified());
        }
        Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
        for (File f : files) {
            System.out.println(i + " - " + f.getName());
            System.out.println(i + " - " + f.lastModified());
            i--;
        }

        for (String arg : args) {
            System.out.println(arg);
        }


        System.out.println("Copying file into another location using Java 7 Files.copy");
//        copyFile();


//        String path = "C:/destination/";


//        Path src = Paths.get("target/test_text.txt");
//        File file = Files.list(Paths.get("target"));


        File to = new File("java7.txt");
//        Files.copy(src, to.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

}

