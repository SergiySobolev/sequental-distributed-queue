package com.sbk.queue.task.producer;


import com.hazelcast.config.Config;
import com.hazelcast.config.QueueConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@EnableEurekaClient
@SpringBootApplication
public class TaskProducer {

    public static void main(String[] args) {
        SpringApplication.run(TaskProducer.class, args);
    }

    @Bean
    public Config config() {
        Config config = new Config();
        config.addQueueConfig(new QueueConfig()
                .setName("inbound-tasks-queue")
                .setMaxSize(120)
        )
        ;
        return config;
    }



}