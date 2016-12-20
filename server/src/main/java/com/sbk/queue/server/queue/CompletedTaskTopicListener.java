package com.sbk.queue.server.queue;

import com.hazelcast.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import task.ProcessingTask;

import java.util.List;
import java.util.UUID;

@Component
public class CompletedTaskTopicListener implements MessageListener<ProcessingTask> {

    private final Logger logger = LoggerFactory.getLogger(CompletedTaskTopicListener.class);
    private final IMap<UUID, List<ProcessingTask>> tasksForSessionsMap;

    @Autowired
    public CompletedTaskTopicListener(HazelcastInstance instance) {
        ITopic<ProcessingTask> completedTasks = instance.getTopic("completed-task-topic");
        completedTasks.addMessageListener(this);
        tasksForSessionsMap = instance.getMap("task-for-session");
        logger.info("Topic waiting for completed tasks");
    }
    @Override
    public void onMessage(Message<ProcessingTask> message) {
        ProcessingTask completedTask = message.getMessageObject();
        logger.info("Task completed: [{}]", completedTask);
        UUID sessionId = completedTask.getSessionId();
        List<ProcessingTask> processingTasks = tasksForSessionsMap.get(sessionId);
        processingTasks.remove(completedTask);
        tasksForSessionsMap.put(sessionId, processingTasks);
    }
}
