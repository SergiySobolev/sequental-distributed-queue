package com.sbk.queue.server;

import com.hazelcast.config.Config;
import com.hazelcast.config.QueueConfig;
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
        config.addQueueConfig(new QueueConfig()
                .setName("inbound-tasks-queue")
                .setMaxSize(120)
        )
        ;
        return config;
    }
}
