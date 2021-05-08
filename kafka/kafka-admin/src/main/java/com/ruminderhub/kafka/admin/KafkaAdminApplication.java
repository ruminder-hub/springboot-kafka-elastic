package com.ruminderhub.kafka.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class KafkaAdminApplication {

    private static ApplicationContext context;

    public static void main(String []args) {
        context = SpringApplication.run(KafkaAdminApplication.class, args);
        displayAllBeans();

    }

    private static void displayAllBeans() {
        String[] allBeanNames = context.getBeanDefinitionNames();
        for(String beanName : allBeanNames) {
            System.out.println("Rumi: " + beanName);
        }
    }
}
