package starters;

import actions.impl.HtmlUnitAction;
import actions.impl.example.ExampleLoginAction;
import actions.impl.example.ExampleLogoutAction;
import actions.impl.example.ExampleStatefulAction;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import engine.ParallelExecutor;
import engine.actors.RandomizedActor;
import model.Metrics;
import runners.ExampleRunner;

public class ExampleStarter {

    public static void vanillaStarter(String[] args) {
        ParallelExecutor parallelExecutor = new ParallelExecutor();
        for (int i = 0; i < 2; i++) {
            RandomizedActor actor = new RandomizedActor(0, 2);
            WebClient webClient = new WebClient(BrowserVersion.FIREFOX_45);
            webClient.getOptions().setThrowExceptionOnScriptError(false);

            actor.addAction(new HtmlUnitAction(webClient, "http://www.google.com", 0));
            actor.addAction(new HtmlUnitAction(webClient, "http://www.bing.com", 0));
            actor.addAction(new HtmlUnitAction(webClient, "http://www.ya.ru", 0));

            parallelExecutor.addActor(actor);
        }
        Metrics metrics = parallelExecutor.executeAll(15);
        System.out.println(metrics.toString());
    }

    public static void main(String[] args) {
        ParallelExecutor parallelExecutor = new ParallelExecutor();
        for (int i = 0; i < 2; i++) {
            WebClient webClient = new WebClient(BrowserVersion.FIREFOX_45);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            RandomizedActor actor = new RandomizedActor(
                    new ExampleLoginAction(webClient, "user", "password", "url"),
                    new ExampleLogoutAction(webClient, "url"),
                    5, 7.5, 1, 3);

            actor.addAction(new ExampleStatefulAction(webClient, "url1"));
            actor.addAction(new ExampleStatefulAction(webClient, "url2"));
            actor.addAction(new ExampleStatefulAction(webClient, "url3"));

            parallelExecutor.addActor(actor);
        }
        Metrics metrics = parallelExecutor.executeAll(10);
        System.out.println(metrics.toString());
    }

    public static void runnerStarter(String[] args) {
        Metrics metrics = new ExampleRunner().runLoadTester();
        String csv = metrics.toCSV();//if you need
        System.out.println(metrics.toString());
    }
}
