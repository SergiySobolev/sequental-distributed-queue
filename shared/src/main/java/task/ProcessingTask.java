package task;


import java.io.Serializable;
import java.util.UUID;

public class ProcessingTask implements Serializable {
    private UUID taskId;
    private long timeStamp;

    public ProcessingTask(UUID taskId, long timeStamp) {
        this.taskId = taskId;
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "ProcessingTask{" +
                "taskId=" + taskId +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
