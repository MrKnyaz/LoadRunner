package actions.impl;

import actions.StatelessAction;
import com.gargoylesoftware.htmlunit.WebClient;
import model.exceptions.ApplicationException;
import org.apache.log4j.Logger;

/**
 * Simple HtmlUnit Rest action, accepts WebClient and URL
 */
public class HtmlUnitRestAction implements StatelessAction<String> {

    public static Logger logger = Logger.getLogger(HtmlUnitRestAction.class);

    WebClient webClient;
    String url;

    public HtmlUnitRestAction(WebClient webClient, String url) {
        this.webClient = webClient;
        this.url = url;
    }

    @Override
    public String getName() {
        return url;
    }

    @Override
    public String execute() {
        try {
            String content = webClient.getPage(url).getWebResponse().getContentAsString();
            return content;
        } catch(Exception e) {
            throw new ApplicationException(e.getMessage());
        }
    }
}
