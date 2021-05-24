package com.ruminderhub.twitter_kafka.runner;

import com.ruminderhub.twitter_kafka.config.TwitterKafkaDataConfig;
import com.ruminderhub.twitter_kafka.listener.TwitterStatusListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import twitter4j.FilterQuery;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import javax.annotation.PreDestroy;

@Component
public class TwitterStreamRunner implements IStreamRunner{

    private final TwitterKafkaDataConfig twitterKafkaDataConfig;
    private final TwitterStatusListener twitterStatusListener;
    private TwitterStream twitterStream;

    private static final Logger log = LoggerFactory.getLogger(TwitterStreamRunner.class);

    TwitterStreamRunner(TwitterKafkaDataConfig dataConfig, TwitterStatusListener statusListener) {
        this.twitterKafkaDataConfig = dataConfig;
        this.twitterStatusListener = statusListener;
    }

    @Override
    public void start() {
        twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(twitterStatusListener);
        filterTwitterStream();
    }

    @PreDestroy
    public void shutdown() {
        if (twitterStream != null) {
            log.info("Closing twitter stream");
            twitterStream.shutdown();
        }
    }

    private void filterTwitterStream() {
        String []keywords = twitterKafkaDataConfig.getTwitterKeywords().toArray(new String[0]);
        FilterQuery query = new FilterQuery(keywords);
        twitterStream.filter(query);
        log.info("Started filtering twitter stream for keywords {}", keywords);
    }

}
