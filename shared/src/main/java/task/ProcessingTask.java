package task;


import java.io.Serializable;
import java.util.UUID;

public class ProcessingTask implements Serializable {
    private UUID taskId;
    private UUID sessionId;
    private long timeStamp;

    public ProcessingTask(UUID taskId, UUID sessionId, long timeStamp) {
        this.taskId = taskId;
        this.sessionId = sessionId;
        this.timeStamp = timeStamp;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    @Override
    public String toString() {
        return "ProcessingTask{" +
                "taskId=" + taskId +
                ", sessionId=" + sessionId +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
