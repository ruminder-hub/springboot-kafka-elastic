package com.ruminderhub.kafka.admin.service;

import com.ruminderhub.kafka.admin.client.KafkaAdminClient;
import com.ruminderhub.kafka.admin.client.KafkaAdminClient;
import com.ruminderhub.kafka.admin.exception.KafkaClientException;
import com.ruminderhub.twitter_kafka.config.KafkaConfigData;
import com.ruminderhub.twitter_kafka.config.RetryConfigData;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.common.protocol.types.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class TopicService implements ITopicService{

    @Autowired
    private RetryTemplate retryTemplate;
    @Autowired
    private KafkaConfigData kafkaConfigData;
    @Autowired
    private AdminClient adminClient;
    @Autowired
    private RetryConfigData retryConfigData;
    @Autowired
    private WebClient webClient;
    @Autowired
    private KafkaAdminClient kafkaAdminClient;

    private final Logger log = LoggerFactory.getLogger(KafkaConfigData.class);

    @Override
    public void createTopicWithRetry() {
        try {
            CreateTopicsResult result = retryTemplate.execute(this::createTopics);
        } catch (Throwable e) {
            throw new KafkaClientException("Failed to create topic after maximum attempts", e);
        }
    }

    @Override
    public boolean checkIfTopicExists(String topic) {
        Collection<TopicListing> topicListings = getTopicListWithRetry();
        int retryCount = 1;
        Integer maxRetry = retryConfigData.getMaxAttempts();
        Integer multiplier = retryConfigData.getMultiplier();
        Long sleepTime = retryConfigData.getSleepTimeMs();
        while(!isTopicCreated(topic, topicListings)) {
            boolean shouldRetry = checkMaxRetry(maxRetry, retryCount++);
            if (shouldRetry == Boolean.FALSE) {
                throw new KafkaClientException("Failed to check topic existence after max attempts");
            }
            sleepTime(sleepTime);
            sleepTime = sleepTime * multiplier;
            topicListings = getTopicListWithRetry();
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean checkSchemaReqistry() {
        int retryCount = 1;
        Integer maxAttempts = retryConfigData.getMaxAttempts();
        Integer multiplier = retryConfigData.getMultiplier();
        Long sleepTime = retryConfigData.getSleepTimeMs();
        while(!getSchemaResistryStatus().is2xxSuccessful()) {
            boolean attemptAvailable = checkMaxRetry(maxAttempts, retryCount);
            if (attemptAvailable == Boolean.FALSE) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    private HttpStatus getSchemaResistryStatus() {
        try {
            return webClient.post().uri(kafkaConfigData.getSchemaRegistryUrl())
                    .exchange().map(ClientResponse::statusCode)
                    .block();
        } catch (Exception e) {
            e.printStackTrace();
            return HttpStatus.SERVICE_UNAVAILABLE;
        }
    }

    @Override
    public void checkIfTopicListCreated() {
        Collection<TopicListing> topicListings = getTopicListWithRetry();
        int retryCount = 1;
        Integer maxRetry = retryConfigData.getMaxAttempts();
        Integer multiplier = retryConfigData.getMultiplier();
        Long sleepTime = retryConfigData.getSleepTimeMs();
        for (String topic : kafkaConfigData.getTopicNamesToCreate()) {
            while(!isTopicCreated(topic, topicListings)) {
                boolean shouldRetry = checkMaxRetry(maxRetry, retryCount++);
                if (shouldRetry == Boolean.FALSE) {
                    throw new KafkaClientException("Failed to check topic existence after max attempts");
                }
                sleepTime(sleepTime);
                sleepTime = sleepTime * multiplier;
                topicListings = getTopicListWithRetry();
            }
        }
    }

    private void sleepTime(Long sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            throw new KafkaClientException("Error while waiting for retry of checking topic existence");
        }
    }

    private Boolean checkMaxRetry(Integer maxRetry, int retry) {
        if (maxRetry > retry) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    private boolean isTopicCreated(String topicName, Collection<TopicListing> topicListings) {
        if (topicListings != null && topicListings.size() > 0) {
            return topicListings.stream().anyMatch(topic -> topic.name().equals(topicName));
        }
        return Boolean.FALSE;
    }

    public CreateTopicsResult createTopics(RetryContext retryContext) {
        List<String> topicNames = kafkaConfigData.getTopicNamesToCreate();
        log.info("Creating Topics {} with try {}", topicNames, retryContext.getRetryCount());
        List<NewTopic> kafkaTopics = topicNames.stream().map(topic -> new NewTopic(topic.trim(),
                kafkaConfigData.getNoOfPartitions(),
                kafkaConfigData.getReplicationFactor())).collect(Collectors.toList());
        return adminClient.createTopics(kafkaTopics);
    }

    @Override
    public Collection<TopicListing> getTopicListWithRetry() {
        try {
            Collection<TopicListing> topicList = retryTemplate.execute(this::getTopicList);
            return topicList;
        } catch (Exception e) {
            throw new KafkaClientException("Failed to get list of topics after maximum attempts", e);
        }

    }

    public Collection<TopicListing> getTopicList(RetryContext retryContext) throws InterruptedException, ExecutionException {
        Collection<TopicListing> topicListings = adminClient.listTopics().listings().get();
        return topicListings;
    }
}
