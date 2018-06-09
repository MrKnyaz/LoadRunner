package actions.impl.decorators;

import actions.Action;
import org.apache.log4j.Logger;

public class LoggedAction<T, K> implements Action<T, K> {

    public static Logger logger = Logger.getLogger(LoggedAction.class);

    Action<T, K> action;

    public LoggedAction(Action<T, K> action) {
        this.action = action;
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
        logger.info("[STARTING...]" + getName());
        try {
            T page = action.execute(inputData);
            return page;
        } catch(Throwable e) {
            logger.error("Error while executing " + action.getName(), e);
            throw e;
        } finally {
            logger.info("[FINISHED]" + getName());
        }
    }
}
