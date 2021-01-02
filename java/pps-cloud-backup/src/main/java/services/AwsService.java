package services;

import config.AwsConfig;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class AwsService {
    public static void checkCredentials() {
        //TODO check if its first logging if yes than ask about credentials to AWS
    }

    public static void synchronizeFiles(List<File> files) {
        AwsConfig.setAwsCredentials();
        S3Client s3client = S3Client.builder().region(Region.EU_CENTRAL_1).build();
        String bucketName = AwsConfig.BUCKET_NAME;

        createNewBucketIfNotExists(s3client, bucketName);

        System.out.println("Files on aws:");
        List<String> listOfFilesOnAws = getListOfFilesOnAws(s3client, bucketName);

        files.stream().filter(file -> !listOfFilesOnAws.contains(file.getName()))
                .forEach(file -> uploadFile(s3client, bucketName, file));

    }

    private static void uploadFile(S3Client s3client, String bucketName, File file) {
        s3client.putObject(PutObjectRequest.builder().bucket(bucketName).key(file.getName())
                .build(), RequestBody.fromFile(file));
        System.out.println("file " + file.getName() + " uploaded");
    }

    private static List<String> getListOfFilesOnAws(S3Client s3client, String bucketName) {
        ListObjectsV2Response objectListing =
                s3client.listObjectsV2(ListObjectsV2Request.builder().bucket(bucketName).build());
        for (S3Object object : objectListing.contents()) {
            System.out.println(object.key() + " " + object.lastModified());
        }

        return objectListing.contents().stream().map(S3Object::key).collect(Collectors.toList());
    }

    private static void createNewBucketIfNotExists(S3Client s3client, String bucketName) {
        HeadBucketRequest headBucketRequest = HeadBucketRequest.builder().bucket(bucketName).build();
        try {
            s3client.headBucket(headBucketRequest);
        } catch (NoSuchBucketException e) {
            s3client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
            System.out.println("new Bucket was created on AWS");
        }
    }
}
