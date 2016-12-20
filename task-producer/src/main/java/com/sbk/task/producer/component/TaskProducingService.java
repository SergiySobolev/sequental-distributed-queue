package com.sbk.task.producer.component;


import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import task.ProcessingTask;

import javax.annotation.PostConstruct;
import java.util.*;

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
        ITopic<ProcessingTask> taskTopic = instance.getTopic("inbound-task-topic");
        UUID[] sessions = new UUID[]{UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()};
        Random r = new Random();
        logger.info("Starting generating tasks for sessions: [{}]", Arrays.toString(sessions));
        Map<UUID, Long> taskNumsForSession = new HashMap<>();
        for(;;){
            try {
                UUID sessionId = sessions[r.nextInt(3)];
                long taskNum = taskNumsForSession.getOrDefault(sessionId,1L);
                ProcessingTask task = new ProcessingTask(sessionId, taskNum);
                taskNumsForSession.put(sessionId, taskNum+1);
                logger.info("Publishing task: [{}]", task);
                taskTopic.publish(task);
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
