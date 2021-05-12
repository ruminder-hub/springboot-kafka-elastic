package com.ruminderhub.twitter_kafka.listener;

import com.ruminderhub.kafka.avro.model.TwitterAvroModel;
//import com.ruminderhub.kafka.producer.service.IKafkaProducer;
import com.ruminderhub.kafka.producer.service.IKafkaProducer;
import com.ruminderhub.twitter_kafka.config.KafkaConfigData;
import com.ruminderhub.twitter_kafka.transformer.TwitterStatusToAvroTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twitter4j.Status;
import twitter4j.StatusAdapter;

@Component
public class TwitterStatusListener extends StatusAdapter {

    private static final Logger log = LoggerFactory.getLogger(TwitterStatusListener.class);
    @Autowired
    private KafkaConfigData kafkaConfigData;
    @Autowired
    private IKafkaProducer<Long, TwitterAvroModel> producer;
    @Autowired
    private TwitterStatusToAvroTransformer transformer;

    @Override
    public void onStatus(Status status) {
        log.info("Twitter status with text {} sending to kafka topic {}", status.getText(), kafkaConfigData.getTopicName());
        TwitterAvroModel twitterAvroModel = transformer.getTwitterAvroModelFromStatus(status);
       producer.send(kafkaConfigData.getTopicName(), twitterAvroModel.getUserId(), twitterAvroModel);
    }
}
