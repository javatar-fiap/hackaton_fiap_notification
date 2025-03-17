package com.fiap.hackatonfiapnotification.application.usecases;

import com.fiap.hackatonfiapnotification.core.service.EmailService;
import com.fiap.hackatonfiapnotification.core.utils.FileToByteArray;
import com.fiap.hackatonfiapnotification.core.service.S3Service;
import com.fiap.hackatonfiapnotification.core.domain.Video;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class CompletedEmailUseCase {

    private final S3Service s3Service;
    private final EmailService emailService;


    public CompletedEmailUseCase(S3Service s3Service, EmailService emailService) {
        this.s3Service = s3Service;
        this.emailService = emailService;
    }

    public void executeZipFileAndSendEmail(Video video) throws IOException {
        File zipFile = s3Service.downloadFile(video);
        byte[] attachment = FileToByteArray.convertFileToBytes(zipFile);
        String subject = "Seu vídeo processado com sucesso";
        String body = """
                Olá %s,

                O frames do seu vídeo já estão gerados e você pode baixá-los no anexo deste email.
                
                Ou acessar a aba de download informando o nome do vídeo: %s
                """.formatted(video.getUser(), video.getVideoKeyS3());

        emailService.sendEmail(video.getEmail(), subject, body, attachment, video.getZipKeyS3());
    }
}
