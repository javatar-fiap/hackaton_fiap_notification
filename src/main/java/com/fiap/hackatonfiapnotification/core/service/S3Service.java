package com.fiap.hackatonfiapnotification.core.service;


import com.fiap.hackatonfiapnotification.application.exception.DownloadException;
import com.fiap.hackatonfiapnotification.core.domain.Video;
import com.fiap.hackatonfiapnotification.infraestructure.config.S3Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;

@Service
public class S3Service {

    private final S3Configuration s3Configuration;

    @Value("${aws.s3.bucketZip}")
    private String bucketZipName;

    public S3Service(S3Configuration s3Configuration) {
        this.s3Configuration = s3Configuration;
    }

    public File downloadFile(Video video) {
        try {
            var request = GetObjectRequest.builder()
                    .bucket(bucketZipName)
                    .key(video.getZipKeyS3())
                    .build();

            var tempFile = Files.createTempFile("video", ".mp4").toFile();
            try (InputStream inputStream = s3Configuration.getS3Client().getObject(request);
                 FileOutputStream outputStream = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            tempFile.deleteOnExit();
            return tempFile;
        } catch (Exception e) {
            throw new DownloadException("Error to download video from S3", e);
        }
    }
}
