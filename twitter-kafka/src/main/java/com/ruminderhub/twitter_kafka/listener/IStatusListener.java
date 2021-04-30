package com.ruminderhub.twitter_kafka.listener;

import twitter4j.Status;
import twitter4j.StatusAdapter;

public interface IStatusListener {

    public void onStatus(Status status);
}
