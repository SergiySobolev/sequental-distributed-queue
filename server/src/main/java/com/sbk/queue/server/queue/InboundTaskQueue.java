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
public class InboundTaskQueue implements ItemListener<ProcessingTask> {

    private final Logger logger = LoggerFactory.getLogger(InboundTaskQueue.class);

    private final HazelcastInstance instance;
    private final IQueue<ProcessingTask> taskQueue;
    private final IMap<UUID, List<ProcessingTask>> tasksForSessionsMap;

    @Autowired
    public InboundTaskQueue(HazelcastInstance instance) {
        this.instance = instance;
        taskQueue = this.instance.getQueue("inbound-tasks-queue");
        taskQueue.addItemListener(this, true);
        tasksForSessionsMap = this.instance.getMap("task-for-session");
        logger.info("Waiting for tasks");
    }

    @Override
    public void itemAdded(ItemEvent<ProcessingTask> item) {
        ProcessingTask task = item.getItem();
        UUID session = task.getSessionId();
        logger.info("Task received:[{}] for session Id : [{}]", task, session);
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
        logger.info("Current tasks for sessions map:[{}]", tasksForSessionsMap);
    }

    @Override
    public void itemRemoved(ItemEvent<ProcessingTask> item) {

    }
}
