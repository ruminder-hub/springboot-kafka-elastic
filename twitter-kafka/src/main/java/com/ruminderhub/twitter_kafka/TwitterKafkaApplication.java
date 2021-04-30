package com.ruminderhub.twitter_kafka;

import com.ruminderhub.twitter_kafka.config.TwitterKafkaDataConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class TwitterKafkaApplication implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(TwitterKafkaApplication.class);
    private final TwitterKafkaDataConfig twitterKafkaDataConfig;

    public TwitterKafkaApplication(TwitterKafkaDataConfig twitterKafkaDataConfig) {
        this.twitterKafkaDataConfig = twitterKafkaDataConfig;
    }

    public static void main(String []args) {
        SpringApplication.run(TwitterKafkaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("App starts " + twitterKafkaDataConfig.getWelcomeMessage());
        log.info(Arrays.toString(twitterKafkaDataConfig.getTwitterKeywords().toArray(new String[0])));
    }
}