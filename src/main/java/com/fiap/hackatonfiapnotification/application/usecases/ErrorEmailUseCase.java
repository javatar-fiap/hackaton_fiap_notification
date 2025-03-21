package com.fiap.hackatonfiapnotification.application.usecases;

import com.fiap.hackatonfiapnotification.core.domain.Video;
import com.fiap.hackatonfiapnotification.core.service.EmailService;
import org.springframework.stereotype.Service;

@Service
public class ErrorEmailUseCase {

    private final EmailService emailService;

    public ErrorEmailUseCase(EmailService emailService) {
        this.emailService = emailService;
    }

    public void execute(Video video) {
        String subject = "Error ao processar imagens do vídeo";
        String body = """
                Olá %s,
                
                Houve um erro ao processar as imagens do vídeo: %s
                
                Tente novamente enviando um vídeo válido.
                """.formatted(video.getUser(), video.getVideoKeyS3());

        emailService.sendEmail(video.getEmail(), subject, body, null, null);
    }
}

