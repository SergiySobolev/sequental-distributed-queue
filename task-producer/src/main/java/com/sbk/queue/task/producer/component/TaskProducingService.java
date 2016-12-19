package com.sbk.queue.task.producer.component;


import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import task.ProcessingTask;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

@Component
public class TaskProducingService {

    private final Logger logger = LoggerFactory.getLogger(TaskProducingService.class);

    private final HazelcastInstance instance;

    @Autowired
    public TaskProducingService(HazelcastInstance instance) {
        this.instance = instance;
    }

    @PostConstruct
    public void generateMessage() {
        IQueue<ProcessingTask> taskQueue = instance.getQueue("inbound-tasks-queue");
        UUID[] sessions = new UUID[]{UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()};
        Random r = new Random();
        logger.info("Starting generating tasks for sessions: [{}]", Arrays.toString(sessions));
        for(;;){
            try {
                ProcessingTask task = new ProcessingTask(UUID.randomUUID(), sessions[r.nextInt(3)], Instant.now().getEpochSecond());
                logger.info("Sending task: [{}]", task);
                taskQueue.offer(task);
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
