package com.sbk.queue.server.queue;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.IQueue;
import com.hazelcast.map.listener.EntryAddedListener;
import com.hazelcast.map.listener.EntryUpdatedListener;
import com.hazelcast.map.listener.MapListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import task.ProcessingTask;

import java.util.List;
import java.util.UUID;

@Component
public class TasksForSessions implements EntryAddedListener<UUID, List<ProcessingTask>>,
                                         EntryUpdatedListener<UUID, List<ProcessingTask>> {

    private final Logger logger = LoggerFactory.getLogger(TasksForSessions.class);

    private final HazelcastInstance instance;
    private final IMap<UUID, List<ProcessingTask>> tasksForSessionsMap;

    @Autowired
    public TasksForSessions(HazelcastInstance instance) {
        this.instance = instance;
        tasksForSessionsMap = this.instance.getMap("task-for-session");
        tasksForSessionsMap.addEntryListener(this, true);
        logger.info("Waiting for tasks");
    }

    @Override
    public void entryAdded( EntryEvent<UUID, List<ProcessingTask>> event ) {
        System.out.println( "Entry Added:" + event );
        printMap();
    }

    @Override
    public void entryUpdated( EntryEvent<UUID, List<ProcessingTask>> event ) {
        System.out.println( "Entry Updated:" + event );
        printMap();
    }

    private void printMap() {
        for(UUID session : tasksForSessionsMap.keySet()) {
            List<ProcessingTask> processingTasks = tasksForSessionsMap.get(session);
            logger.info("Tasks count for session [{}] = [{}]: ", session, processingTasks.size());
        }
    }
}
