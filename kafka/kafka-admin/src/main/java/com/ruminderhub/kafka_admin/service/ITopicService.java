package com.ruminderhub.kafka_admin.service;

import org.apache.kafka.clients.admin.TopicListing;

import java.util.Collection;

public interface ITopicService {

    Boolean checkSchemaReqisrtry();
    void createTopicWithRetry();
    boolean checkIfTopicExists(String topic);
    void checkIfTopicListCreated();
    Collection<TopicListing> getTopicListWithRetry();
}
