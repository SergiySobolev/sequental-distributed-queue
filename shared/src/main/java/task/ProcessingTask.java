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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProcessingTask that = (ProcessingTask) o;

        if (taskNum != that.taskNum) return false;
        return sessionId.equals(that.sessionId);
    }

    @Override
    public int hashCode() {
        int result = sessionId.hashCode();
        result = 31 * result + (int) (taskNum ^ (taskNum >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "ProcessingTask{" +
                "sessionId=" + sessionId +
                ", taskNum=" + taskNum +
                '}';
    }
}
