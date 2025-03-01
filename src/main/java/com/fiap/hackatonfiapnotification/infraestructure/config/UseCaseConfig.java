package com.fiap.hackatonfiapnotification.infraestructure.config;

import com.fiap.hackatonfiapnotification.application.usecases.CompletedEmailUseCase;
import com.fiap.hackatonfiapnotification.application.usecases.ErrorEmailUseCase;
import com.fiap.hackatonfiapnotification.core.service.EmailService;
import com.fiap.hackatonfiapnotification.core.service.S3Service;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CompletedEmailUseCase completedEmailUseCase(S3Service s3Service, EmailService emailService) {
        return new CompletedEmailUseCase(s3Service, emailService);
    }

    @Bean
    public ErrorEmailUseCase errorEmailUseCase(EmailService emailService) {
        return new ErrorEmailUseCase(emailService);
    }
}
