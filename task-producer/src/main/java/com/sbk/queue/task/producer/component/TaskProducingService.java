package com.sbk.queue.task.producer.component;


import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import task.ProcessingTask;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.UUID;

@Component
public class TaskProducingService {

    private final HazelcastInstance instance;

    @Autowired
    public TaskProducingService(HazelcastInstance instance) {
        this.instance = instance;
    }

    @PostConstruct
    public void generateMessage() {
        IQueue<ProcessingTask> taskQueue = instance.getQueue("inbound-tasks-queue");
        for(;;){
            try {
                ProcessingTask e = new ProcessingTask(UUID.randomUUID(), Instant.now().getEpochSecond());
                System.out.println("Sending task: " + e.toString());
                taskQueue.offer(e);
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
