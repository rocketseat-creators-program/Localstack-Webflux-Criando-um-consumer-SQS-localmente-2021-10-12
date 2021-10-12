package com.rocketseat.expertsclub.orderconsumer.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.net.URI;

@Configuration
public class SqsConfiguration {

    private final AwsConfiguration awsConfiguration;

    public SqsConfiguration(AwsConfiguration awsConfiguration) {
        this.awsConfiguration = awsConfiguration;
    }


    @Bean
    public SqsAsyncClient sqsAsyncClient() {
        return SqsAsyncClient.builder()
                .region(Region.of(awsConfiguration.getRegion()))
                .endpointOverride(URI.create("http://localhost:4566"))
                .credentialsProvider(this.awsCredentialsProvider())
                .build();
    }


    private AwsCredentialsProvider awsCredentialsProvider() {
        return StaticCredentialsProvider.create(AwsBasicCredentials
                .create(awsConfiguration.getAccessKey(), awsConfiguration.getSecretKey()));


    }
}
