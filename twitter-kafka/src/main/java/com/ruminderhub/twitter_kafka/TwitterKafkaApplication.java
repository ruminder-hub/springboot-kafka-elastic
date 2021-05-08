package com.ruminderhub.twitter_kafka;

import com.ruminderhub.twitter_kafka.config.TwitterKafkaDataConfig;
import com.ruminderhub.twitter_kafka.init.IStreamInitializer;
import com.ruminderhub.twitter_kafka.runner.TwitterStreamRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;

@SpringBootApplication
@ComponentScan(basePackages = {"com.ruminderhub"})
public class TwitterKafkaApplication implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(TwitterKafkaApplication.class);
    @Autowired
    private IStreamInitializer twitterStreamInitializer;

    private final TwitterStreamRunner twitterStreamRunner;

    public TwitterKafkaApplication(TwitterStreamRunner twitterStreamRunner) {
        this.twitterStreamRunner = twitterStreamRunner;
    }

    public static void main(String []args) {
        SpringApplication.run(TwitterKafkaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("App started");
        twitterStreamInitializer.init();
        twitterStreamRunner.start();
    }
}
