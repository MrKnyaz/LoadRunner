package actions.impl.example;


import actions.StatelessAction;
import actions.impl.example.actionmodel.ExampleLoginData;
import com.gargoylesoftware.htmlunit.WebClient;

public class ExampleLoginAction implements StatelessAction<ExampleLoginData> {

    private WebClient webClient;
    private String username;
    private String password;
    private String url;

    public ExampleLoginAction(WebClient webClient, String username, String password, String url) {
        this.webClient = webClient;
        this.username = username;
        this.password = password;
        this.url = url;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public ExampleLoginData execute() {
        try {
            /*call some service and get data*/
            ExampleLoginData data = new ExampleLoginData();
            data.setUsername(username);
            //fill data from service
            data.setToken("some token");
            return data;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
