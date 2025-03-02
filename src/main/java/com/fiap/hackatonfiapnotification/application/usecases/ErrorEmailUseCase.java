package com.fiap.hackatonfiapnotification.application.usecases;

import com.fiap.hackatonfiapnotification.core.domain.VideoMessage;
import com.fiap.hackatonfiapnotification.core.service.EmailService;
import org.springframework.stereotype.Service;

@Service
public class ErrorEmailUseCase {

    private final EmailService emailService;

    public ErrorEmailUseCase(EmailService emailService) {
        this.emailService = emailService;
    }

    public void execute(VideoMessage videoMessage) {
        String subject = "Erro ao processar vídeo";
        String body = """
                Olá %s,
                
                Houve um erro ao processar o vídeo: %s
                
                Tente novamente enviando um vídeo válido.
                """.formatted(videoMessage.getUser(), videoMessage.getVideoKeyS3());

        emailService.sendEmail(videoMessage.getEmail(), subject, body, null, null);
    }
}

