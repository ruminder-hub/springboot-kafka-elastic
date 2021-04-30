package com.ruminderhub.twitter_kafka.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Data
@ConfigurationProperties(prefix = "twitter-service")
public class TwitterKafkaDataConfig {
    List<String> twitterKeywords;
    private String welcomeMessage;
}
