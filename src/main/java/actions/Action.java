package actions;


public interface Action<T, K> {

    String getName();
    T execute();
    T execute(K inputData);
}
