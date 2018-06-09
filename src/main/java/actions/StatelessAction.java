package actions;

import com.gargoylesoftware.htmlunit.WebClient;

/**
 * Child has to implement execute() method
 */
public interface StatelessAction<T> extends Action<T, T> {

    default T execute(T inputData) {
        return execute();
    }

}
