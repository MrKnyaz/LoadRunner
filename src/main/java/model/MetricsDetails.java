package model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class MetricsDetails {

    String actionName;
    LocalDateTime startTime;
    Long execTime;
    String threadName;
    String error;

    public MetricsDetails(String actionName, LocalDateTime startTime, Long execTime, String threadName) {
        this.actionName = actionName;
        this.startTime = startTime;
        this.execTime = execTime;
        this.threadName = threadName;
    }

    public MetricsDetails(String actionName, LocalDateTime startTime, Long execTime, String threadName, String error) {
        this(actionName, startTime, execTime, threadName);
        this.error = error;
    }

    public String toCSV() {
        return actionName + "," + startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ","
                + execTime + "," + threadName + (error != null ? "," + error : "");
    }

    @Override
    public String toString() {
        return "[" + startTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " -> "
                + execTime + "ms" + (error != null ? ";err->" + error : "") + "]";
    }
}
