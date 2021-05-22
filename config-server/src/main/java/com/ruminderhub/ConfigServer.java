package com.ruminderhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class ConfigServer {

    public static void main(String []args) {
        try {
            SpringApplication.run(ConfigServer.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
