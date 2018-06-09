package runners;

import actions.impl.HtmlUnitAction;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import engine.actors.Actor;
import engine.actors.RandomizedActor;

public class ExampleRunner extends CommonRunner {

    @Override
    protected Actor createActor(int actorIndex) {
        RandomizedActor actor = new RandomizedActor(0, 2);
        WebClient webClient = new WebClient(BrowserVersion.FIREFOX_45);
        webClient.getOptions().setThrowExceptionOnScriptError(false);

        actor.addAction(new HtmlUnitAction(webClient, "http://www.google.com", 0));
        actor.addAction(new HtmlUnitAction(webClient, "http://www.bing.com", 0));
        actor.addAction(new HtmlUnitAction(webClient, "http://www.ya.ru", 0));
        return actor;
    }
}
