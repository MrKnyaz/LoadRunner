package actions.impl.example;

import actions.StatefulAction;
import actions.impl.example.actionmodel.ExampleLoginData;
import com.gargoylesoftware.htmlunit.WebClient;
import org.apache.log4j.Logger;

public class ExampleStatefulAction implements StatefulAction<String, ExampleLoginData> {

    public static Logger logger = Logger.getLogger(ExampleStatefulAction.class);

    WebClient webClient;
    String url;

    public ExampleStatefulAction(WebClient webClient, String url) {
        this.webClient = webClient;
        this.url = url;
    }

    @Override
    public String getName() {
        return url;
    }

    @Override
    public String execute(ExampleLoginData userData) {
        try {
            /* call some service using data passed from LoginAction */
            System.out.println(userData.getUsername() + ": " + userData.getToken());
            return "some data from service";
        } catch(Exception e) {
            logger.error("Error while executing simple REST action " + url, e);
            throw new RuntimeException(e);
        }
    }
}
