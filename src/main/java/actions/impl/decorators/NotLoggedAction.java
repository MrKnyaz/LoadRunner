package actions.impl.decorators;

import actions.Action;
import org.apache.log4j.Logger;

public class NotLoggedAction<T, K> implements Action<T, K> {

    public static Logger logger = Logger.getLogger(NotLoggedAction.class);

    Action<T, K> action;

    public NotLoggedAction(Action<T, K> action) {
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
        try {
            T page = action.execute(inputData);
            return page;
        } catch(Exception e) {
            logger.error("Error while executing " + action.getName(), e);
            throw e;
        }
    }
}
