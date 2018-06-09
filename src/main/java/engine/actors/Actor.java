package engine.actors;

import actions.impl.decorators.TimedAction;
import model.Metrics;
import model.MetricsDetails;
import org.apache.log4j.Logger;

import java.time.LocalDateTime;
import java.time.LocalTime;

abstract public class Actor {
    public static Logger logger = Logger.getLogger(Actor.class);

    protected Metrics metrics = new Metrics();

    public Actor(Metrics metrics) {
        this.metrics = metrics;
    }

    public Metrics act(long executionTimeInSeconds) {
        long elapsedTime = 0;
        onStartActing();
        while (elapsedTime < executionTimeInSeconds) {
            long tic = System.currentTimeMillis();
            playScenario();
            long tac = System.currentTimeMillis();
            elapsedTime += (tac - tic) / 1000;
        }
        onStopActing();
        return metrics;
    }

    public abstract void onStartActing();

    public abstract void playScenario();

    public abstract void onStopActing();

    public <T> T executeAction(TimedAction<T, T> action) {
        return executeAction(action, null);
    }

    public <T, K> T executeAction(TimedAction<T, K> action, K inputData) {
        LocalDateTime startTime = LocalDateTime.now();
        try {
            T outputData = action.execute(inputData);
            metrics.add(action.getName(), new MetricsDetails(action.getName(), startTime, action.getExecutionTime(), Thread.currentThread().getName()));
            return outputData;
        } catch (Throwable e) {
            metrics.add(action.getName(), new MetricsDetails(action.getName(), startTime, action.getExecutionTime(), Thread.currentThread().getName(), e.getMessage()));
            return null;
        }
    }

    Metrics getMetrics() { return metrics; }

}
