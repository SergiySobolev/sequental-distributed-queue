package com.sbk.queue.task.producer;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class TaskProducer {

    public static void main(String[] args) {
        SpringApplication.run(TaskProducer.class, args);
    }
}