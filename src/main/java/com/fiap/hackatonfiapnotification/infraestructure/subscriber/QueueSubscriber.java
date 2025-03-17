package com.fiap.hackatonfiapnotification.infraestructure.subscriber;

import com.fiap.hackatonfiapnotification.application.enums.NotificationStatus;
import com.fiap.hackatonfiapnotification.application.usecases.CompletedEmailUseCase;
import com.fiap.hackatonfiapnotification.application.usecases.ErrorEmailUseCase;
import com.fiap.hackatonfiapnotification.core.domain.Video;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class QueueSubscriber {

    private final CompletedEmailUseCase completedEmailUseCase;
    private final ErrorEmailUseCase errorEmailUseCase;

    @Autowired
    public QueueSubscriber(CompletedEmailUseCase completedEmailUseCase, ErrorEmailUseCase errorEmailUseCase) {
        this.completedEmailUseCase = completedEmailUseCase;
        this.errorEmailUseCase = errorEmailUseCase;
    }

    @io.awspring.cloud.sqs.annotation.SqsListener("${spring.cloud.aws.sqs.queue-name}")
    public void receiveMessage(Message<String> message) {
        String content = message.getPayload();

        //TODO Ver se é necessário ter essa validação ou se o try resolve
        if (Objects.isNull(content == null)) {
            log.error("Received null content from Queue");
            return;
        }

        try {
            var queueMessage = new JSONObject(content);
            var messageContent = queueMessage.getString("Message");
            var videoMessageJson = new JSONObject(messageContent);

            var video = new Video();
            video.setZipKeyS3(videoMessageJson.getString("zipKeyS3"));
            video.setVideoKeyS3(videoMessageJson.getString("videoKeyS3"));
            video.setVideoUrlS3(videoMessageJson.getString("videoUrlS3"));
            video.setId(videoMessageJson.getString("id"));
            video.setUser(videoMessageJson.getString("user"));
            video.setEmail(videoMessageJson.getString("email"));
            video.setStatus(videoMessageJson.getString("status"));

            log.info("Video message converted to object: {}", video);

            if (NotificationStatus.COMPLETED.toString().equals(video.getStatus())) {
                completedEmailUseCase.executeZipFileAndSendEmail(video);
            } else if (NotificationStatus.PROCESSING_ERROR.toString().equals(video.getStatus())) {
                errorEmailUseCase.execute(video);
            }

        } catch (Exception e) {
            log.error("Error to processing message: {}", e.getMessage(), e);
        }
    }
}