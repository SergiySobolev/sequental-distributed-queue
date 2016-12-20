package task;


import java.io.Serializable;
import java.util.UUID;

public class ProcessingTask implements Serializable {
    private UUID sessionId;
    private long taskNum;

    public ProcessingTask(UUID sessionId, long taskNum) {
        this.sessionId = sessionId;
        this.taskNum = taskNum;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public long getTaskNum() {
        return taskNum;
    }

    @Override
    public String toString() {
        return "ProcessingTask{" +
                "sessionId=" + sessionId +
                ", taskNum=" + taskNum +
                '}';
    }
}
