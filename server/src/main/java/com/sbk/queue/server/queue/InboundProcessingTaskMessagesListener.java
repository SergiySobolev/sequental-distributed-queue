package com.sbk.queue.server.queue;

import com.hazelcast.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import task.ProcessingTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class InboundProcessingTaskMessagesListener implements MessageListener<ProcessingTask> {

    private final Logger logger = LoggerFactory.getLogger(InboundProcessingTaskMessagesListener.class);

    private final IMap<UUID, List<ProcessingTask>> tasksForSessionsMap;

    @Autowired
    public InboundProcessingTaskMessagesListener(HazelcastInstance instance) {
        ITopic<ProcessingTask> processingTaskITopic = instance.getTopic("inbound-task-topic");
        processingTaskITopic.addMessageListener(this);
        tasksForSessionsMap = instance.getMap("task-for-session");
        logger.info("Topic waiting for inbound tasks");
    }


    @Override
    public void onMessage(Message<ProcessingTask> message) {
        ProcessingTask task = message.getMessageObject();
        UUID session = task.getSessionId();
        logger.info("Task received: [{}] for session Id : [{}]", task, session);
        List<ProcessingTask> tasksForSession = tasksForSessionsMap.get(session);
        logger.info("Getting task queue for :[{}]", session);
        if(tasksForSession == null) {
            List<ProcessingTask> tasksForSessionsQueue = new ArrayList<>();
            tasksForSessionsQueue.add(task);
            tasksForSessionsMap.put(session, tasksForSessionsQueue);
        } else {
            tasksForSession.add(task);
            tasksForSessionsMap.put(session, tasksForSession);
        }

    }
}
