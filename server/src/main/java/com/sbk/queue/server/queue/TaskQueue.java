package com.sbk.queue.server.queue;


import com.hazelcast.config.Config;
import com.hazelcast.config.QueueConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import com.hazelcast.core.ItemEvent;
import com.hazelcast.core.ItemListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import task.ProcessingTask;

@Component
public class TaskQueue implements ItemListener {
    private final HazelcastInstance instance;
    private IQueue<ProcessingTask> taskQueue;

    @Autowired
    public TaskQueue(HazelcastInstance instance) {
        this.instance = instance;
        taskQueue = instance.getQueue("inbound-tasks-queue");
        taskQueue.addItemListener(this, true);
    }

    @Override
    public void itemAdded(ItemEvent item) {
        System.out.println("Item added : " + item.getItem().toString() );
    }

    @Override
    public void itemRemoved(ItemEvent item) {

    }
}
