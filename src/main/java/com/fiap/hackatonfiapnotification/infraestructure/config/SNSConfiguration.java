package com.fiap.hackatonfiapnotification.infraestructure.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;

@Setter
@Configuration
public class SNSConfiguration {

    @Value("${aws.accessKeyId}")
    private String accessKeyId;

    @Value("${aws.secretAccessKey}")
    private String secretAccessKey;

    @Value("${aws.token}")
    private String token;

    private final String regionName = Region.US_EAST_1.toString();

    @Bean
    public AmazonSNS snsClient() {
        var credentials = new BasicSessionCredentials(accessKeyId, secretAccessKey, token);

        return AmazonSNSClientBuilder.standard()
                .withRegion(regionName)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }

}
