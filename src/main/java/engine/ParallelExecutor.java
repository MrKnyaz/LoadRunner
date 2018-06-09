package engine;

import engine.actors.Actor;
import model.Metrics;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallelExecutor {

    Metrics allMetrics = new Metrics();
    List<Actor> actors = new ArrayList<>();

    public void addActor(Actor actor) {
        actors.add(actor);
    }

    public Metrics executeAll(int actorExecutionTimeInSeconds) {
        ExecutorService executorService = Executors.newFixedThreadPool(actors.size());
        List<CompletableFuture<Metrics>> allFutures = new ArrayList<>();
        for (Actor actor : actors) {
            allFutures.add(CompletableFuture.supplyAsync(
                    () -> actor.act(actorExecutionTimeInSeconds),
                    executorService
                    ));
        }
        CompletableFuture
                .allOf(allFutures.toArray(new CompletableFuture[allFutures.size()]))
                .thenAccept(a ->
                        allFutures.forEach(future -> {
                            Metrics metrics = future.join();
                            allMetrics.add(metrics);
                        })).join();
        executorService.shutdownNow();
        return allMetrics;
    }

}
