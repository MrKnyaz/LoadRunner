package actions.impl;

import actions.StatelessAction;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import model.exceptions.ApplicationException;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

/**
 * Simple HtmlUnit action, accepts WebClient, URL and Javascript delay
 * @returns HtmlPage
 */
public class SeleniumAction implements StatelessAction<Void> {

    public static Logger logger = Logger.getLogger(SeleniumAction.class);

    WebDriver webDriver;
    String url;

    public SeleniumAction(WebDriver webDriver, String url) {
        this.webDriver = webDriver;
        this.url = url;
    }

    @Override
    public String getName() {
        return url;
    }

    @Override
    public Void execute() {
        try {
            webDriver.get(url);
        } catch(Throwable e) {
            throw new ApplicationException(e.getMessage());
        }
        return null;
    }
}
