package actions.impl.decorators;

import actions.Action;
import org.apache.log4j.Logger;

public class TimedAction<T, K> implements Action<T, K> {

    public static Logger logger = Logger.getLogger(TimedAction.class);

    Action<T, K> action;
    long executionTime = -1;

    public TimedAction(Action<T, K> action) {
        this.action = new LoggedAction(action);
    }

    @Override
    public String getName() {
        return action.getName();
    }

    @Override
    public T execute() {
        return execute(null);
    }

    @Override
    public T execute(K inputData) {
        long tic = System.currentTimeMillis();
        try {
            T outputData = action.execute(inputData);
            return outputData;
        } finally {
            long tac = System.currentTimeMillis();
            executionTime = tac - tic;
        }
    }

    public long getExecutionTime() {
        return executionTime;
    }
}
