package com.sbk.task.producer;


import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.core.HazelcastInstance;
import com.sbk.task.producer.component.TaskProducingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@EnableEurekaClient
@SpringBootApplication
@RestController
public class TaskProducer {

    @Autowired
    private TaskProducingService taskProducingService;

    public static void main(String[] args) {
        SpringApplication.run(TaskProducer.class, args);
    }

    @RequestMapping(method = POST, path="/send/start")
    public void taskSendingStart() {
        taskProducingService.sendTasks();
    }

    @Bean
    public ClientConfig config() {
        return new ClientConfig().setNetworkConfig(
                new ClientNetworkConfig()
                        .addAddress("127.0.0.1:10000")
                        .setConnectionAttemptLimit(0)
        );
    }

    @Bean
    public HazelcastInstance getHazelcastClient() {
        return HazelcastClient.newHazelcastClient(config());
    }



}