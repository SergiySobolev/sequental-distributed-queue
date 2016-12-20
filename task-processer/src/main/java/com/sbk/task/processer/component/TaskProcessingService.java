package com.sbk.task.processer.component;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import task.ProcessingTask;

@Component
public class TaskProcessingService implements MessageListener<ProcessingTask> {
    private final Logger logger = LoggerFactory.getLogger(TaskProcessingService.class);


    private final ITopic<ProcessingTask> readyForProcessingTaskTopic;
    private final ITopic<ProcessingTask> completedTasksTopic;

    @Autowired
    public TaskProcessingService(HazelcastInstance instance) {
        readyForProcessingTaskTopic = instance.getTopic("ready-for-processing-task-topic");
        completedTasksTopic = instance.getTopic("completed-task-topic");
        readyForProcessingTaskTopic.addMessageListener(this);
    }

    @Override
    public void onMessage(Message<ProcessingTask> message) {
        ProcessingTask task = message.getMessageObject();
        logger.info("Task [{}] processing begin", task);
        try {
            Thread.sleep(3000);
            completedTasksTopic.publish(task);
        } catch (InterruptedException ex) {

        }
        logger.info("Task [{}] processing finished", task);
    }
}
