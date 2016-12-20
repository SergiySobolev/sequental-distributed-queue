package com.sbk.queue.server;

import com.hazelcast.config.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Bean;

@EnableEurekaServer
@SpringBootApplication
public class Server {

    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
    }

    @Bean
    public Config config() {
        Config config = new Config();
        NetworkConfig networkConfig = new NetworkConfig();
        networkConfig.setPort(10_000);
        config.setNetworkConfig(networkConfig);
        config.addTopicConfig(new TopicConfig()
                .setName("inbound-task-topic"));
        config.addTopicConfig(new TopicConfig()
                .setName("ready-for-processing-task-topic"));
        config.addTopicConfig(new TopicConfig()
                .setName("completed-task-topic"));
        config.addMapConfig(new MapConfig()
                .setName("task-for-session"))
        ;
        return config;
    }
}
