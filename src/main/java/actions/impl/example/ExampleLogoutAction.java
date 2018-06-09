package actions.impl.example;

import actions.StatefulAction;
import actions.impl.example.actionmodel.ExampleLoginData;
import com.gargoylesoftware.htmlunit.WebClient;
import org.apache.log4j.Logger;

public class ExampleLogoutAction implements StatefulAction<String, ExampleLoginData> {

    public static Logger logger = Logger.getLogger(ExampleLogoutAction.class);

    WebClient webClient;
    String url;

    public ExampleLogoutAction(WebClient webClient, String url) {
        this.webClient = webClient;
        this.url = url;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String execute(ExampleLoginData userData) {
        try {
            /* logout from service */
            System.out.println(userData.getUsername() + ": " + userData.getToken());
            return "logout successful";
        } catch(Exception e) {
            logger.error("Error while executing simple REST action " + url, e);
            throw new RuntimeException(e);
        }
    }
}
