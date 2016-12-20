package com.sbk.task.processer;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@EnableEurekaClient
@SpringBootApplication
public class TaskProcesser {

    public static void main(String[] args) {
        SpringApplication.run(TaskProcesser.class, args);
    }

    @Bean
    public ClientConfig config() {
        return new ClientConfig().setNetworkConfig(
                new ClientNetworkConfig()
                        .addAddress("127.0.0.1:10000")
                        .setConnectionAttemptLimit(0));
    }

    @Bean
    public HazelcastInstance getHazelcastClient() {
        return HazelcastClient.newHazelcastClient(config());
    }

}
