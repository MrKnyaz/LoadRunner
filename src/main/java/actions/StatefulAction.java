package actions;

/**
 * Child has to implement execute(T inputData) method
 */
public interface StatefulAction<T, K> extends Action<T, K> {

    default T execute() {
        throw new UnsupportedOperationException("StatefulAction must use 'inputData' as a parameter");
    }
}
