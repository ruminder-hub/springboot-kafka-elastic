package com.ruminderhub.kafka.producer.service;

import com.ruminderhub.kafaka.avro.model.TwitterAvroModel;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.PreDestroy;

@Service
public class TwitterKafkaProducer implements IKafkaProducer<Long, TwitterAvroModel>{

    @Autowired
    private KafkaTemplate<Long, TwitterAvroModel> kafkaTemplate;
    private static final Logger log = LoggerFactory.getLogger(TwitterKafkaProducer.class);

    @Override
    public void send(String topicName, Long key, TwitterAvroModel message) {
        log.info("Sending message={} with key={} to topic={}", message, key, topicName);
        ListenableFuture<SendResult<Long, TwitterAvroModel>> kafkaResultFuture = kafkaTemplate.send(topicName, key, message);
        kafkaResultFuture.addCallback(new ListenableFutureCallback<SendResult<Long, TwitterAvroModel>>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.info("Failed to send message {} to kafka", message, throwable);
            }

            @Override
            public void onSuccess(SendResult<Long, TwitterAvroModel> longTwitterAvroModelSendResult) {
                RecordMetadata metadata = longTwitterAvroModelSendResult.getRecordMetadata();
                log.info("Successfully sent message to Kafka with metadata, Topic: {}, Partition {}, Offset {}, Timestamp {}, time {}",
                        metadata.topic(), metadata.partition(), metadata.offset(), metadata.timestamp(), System.nanoTime());

            }
        });
    }

    @PreDestroy
    public void closeKafkaTemplate() {
        if (kafkaTemplate != null) {
            log.info("Closing Kafka template");
            kafkaTemplate.destroy();
        }
    }
}
