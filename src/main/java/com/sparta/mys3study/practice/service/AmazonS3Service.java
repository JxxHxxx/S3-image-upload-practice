package com.sparta.mys3study.practice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.*;
import java.net.URL;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmazonS3Service {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public String putS3Object(MultipartFile multipartFile) throws IOException {

        String objectKey = createObjectKey(multipartFile);

        try {
            PutObjectRequest putObjectRequest = createPutObjectRequest(multipartFile, objectKey);

            InputStream inputStream = multipartFile.getInputStream();
            PutObjectResponse response = s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, multipartFile.getSize()));

            inputStream.close();
            return objectKey;

        } catch (S3Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        return "";
    }

    public void deleteBucketObject(String objectKey) {

        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);


        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public void getURL(String objectKey) {

        try {
            GetUrlRequest request = GetUrlRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();

            URL url = s3Client.utilities().getUrl(request);
            System.out.println("The URL for  " + objectKey + " is " + url);

        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    private String createObjectKey(MultipartFile multipartFile) {
        String uuidStoreFileName = createUUID();

        String ext = extractExt(multipartFile.getOriginalFilename());

        return uuidStoreFileName + ext;
    }

    private PutObjectRequest createPutObjectRequest(MultipartFile multipartFile, String objectKey) {

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .contentType(multipartFile.getContentType()) // META-DATA Content-Type 설정
                .acl(ObjectCannedACL.PUBLIC_READ) // 읽기 권한
                .build();

        return putObjectRequest;
    }


    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos);
    }

    private String createUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }

    // Return a byte array.
    private byte[] getObjectFile(String filePath) {

        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;

        try {
            File file = new File(filePath);
            bytesArray = new byte[(int) file.length()];
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return bytesArray;
    }
}
