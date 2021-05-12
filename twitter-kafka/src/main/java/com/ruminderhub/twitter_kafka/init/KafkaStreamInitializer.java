package com.ruminderhub.twitter_kafka.init;

import com.ruminderhub.kafka.admin.client.KafkaAdminClient;
import com.ruminderhub.kafka.admin.service.ITopicService;
import com.ruminderhub.twitter_kafka.config.KafkaConfigData;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

@Component
public class KafkaStreamInitializer implements IStreamInitializer{

    private static final Logger log = LoggerFactory.getLogger(KafkaStreamInitializer.class);
    @Autowired
    private KafkaAdminClient kafkaAdminClient;
    @Autowired
    private ITopicService topicService;


    @Override
    public void init() {
        topicService.createTopicWithRetry();
        topicService.checkSchemaReqistry();
        log.info("Topics with name {} have been created ", kafkaAdminClient.getKafkaConfigData().getTopicNamesToCreate());

    }
}
