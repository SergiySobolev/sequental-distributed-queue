package com.sbk.queue.server.queue;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.ITopic;
import com.hazelcast.map.listener.EntryAddedListener;
import com.hazelcast.map.listener.EntryUpdatedListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import task.ProcessingTask;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

@Component
public class TasksForSessions implements EntryAddedListener<UUID, List<ProcessingTask>>,
                                         EntryUpdatedListener<UUID, List<ProcessingTask>> {

    private final Logger logger = LoggerFactory.getLogger(TasksForSessions.class);

    private final IMap<UUID, List<ProcessingTask>> tasksForSessionsMap;
    private final ITopic<ProcessingTask> readyForProcessingTaskTopic;

    @Autowired
    public TasksForSessions(HazelcastInstance instance) {
        tasksForSessionsMap = instance.getMap("task-for-session");
        tasksForSessionsMap.addEntryListener(this, true);
        readyForProcessingTaskTopic = instance.getTopic("ready-for-processing-task-topic");
        logger.info("Waiting for tasks");
    }

    @Override
    public void entryAdded( EntryEvent<UUID, List<ProcessingTask>> event ) {
        ProcessingTask task = event.getValue().get(0);
        logger.info("First task for sessionId [{}], taskNum [{}]", task.getSessionId(), task.getTaskNum());
        logger.info("Sending task for processing: [{}],", task);
        readyForProcessingTaskTopic.publish(task);
        printMap();
    }

    @Override
    public void entryUpdated( EntryEvent<UUID, List<ProcessingTask>> event ) {
        List<ProcessingTask> currentTasks = event.getValue();
        List<ProcessingTask> oldTasks = event.getOldValue();
        boolean firstTaskArrived = oldTasks.size() == 0 && currentTasks.size() == 1;
        logger.debug("First task arrived: [{}]", firstTaskArrived);
        boolean someTaskCompletedAndRemovedFromMap = oldTasks.size() - currentTasks.size() == 1;
        logger.debug("Task removed : [{}]", someTaskCompletedAndRemovedFromMap);
        if((firstTaskArrived || someTaskCompletedAndRemovedFromMap ) && currentTasks.size() >= 1) {
            ProcessingTask task = currentTasks.get(0);
            logger.info("Sending task for processing: [{}],", task);
            readyForProcessingTaskTopic.publish(task);
        }
        printMap();
    }

    private void printMap() {
        for(UUID session : tasksForSessionsMap.keySet()) {
            List<ProcessingTask> processingTasks = tasksForSessionsMap.get(session);
            logger.info("Tasks count for session [{}] = [{}]: ", session,
                    processingTasks.stream().map(t -> "" + t.getTaskNum())
                    .collect(joining(",")));
        }
    }
}
