package actions.impl;

import actions.StatelessAction;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import model.exceptions.ApplicationException;
import org.apache.log4j.Logger;

/**
 * Simple HtmlUnit action, accepts WebClient, URL and Javascript delay
 * @returns HtmlPage
 */
public class HtmlUnitAction implements StatelessAction<HtmlPage> {

    public static Logger logger = Logger.getLogger(HtmlUnitAction.class);

    WebClient webClient;
    String url;
    long javascriptDelay;

    public HtmlUnitAction(WebClient webClient, String url, long javascriptDelay) {
        this.webClient = webClient;
        this.url = url;
        this.javascriptDelay = javascriptDelay;
    }

    @Override
    public String getName() {
        return url;
    }

    @Override
    public HtmlPage execute() {
        try {
            HtmlPage page = webClient.getPage(url);
            if (javascriptDelay > 0) {
                webClient.waitForBackgroundJavaScriptStartingBefore(javascriptDelay);
            }
            return page;
        } catch(Throwable e) {
            throw new ApplicationException(e.getMessage());
        }
    }
}
