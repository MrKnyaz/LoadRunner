# LoadRunner

**LoadRunner** is a small and easy to use load testing framework
  - Use **HtmlUnit**, **Selenium WebDriver** or anything else
  - Ready to start simple standard **Action** classes
  - Simple api to create your own **Actions**
  - Collect metrics in human readable or csv format
  - Extend metrics to get data in any form you like

### How to use

Just a few steps to start testing
  - Create **ParallelExecutor** object
  - Create **RandomizedActor** object with **minimumDelay** and **maximumDelay** betwen actions
  - Create **WebClient** object, in case you use **HtmlUnit** 
  - Create and add some **actions** to **Actor**
  - Add **Actor** to **ParallelExecutor**
  - Call **.executeAll(actorExecutionTimeInSeconds)** on **ParallelExecutor**
#### Example
```java
public class ExampleStarter {

    public static void main(String[] args) {
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
        Metrics metrics = parallelExecutor.executeAll(15); //Each actor runs for 15 seconds
        System.out.println(metrics.toString());
    }
}
```
#### Output 

```$cmd
14:39:01.400 [pool-2-thread-1] INFO  engine.actors.Actor - Starting actor
14:39:01.401 [pool-2-thread-2] INFO  engine.actors.Actor - Starting actor
14:39:02.368 [pool-2-thread-2] INFO  actions.impl.decorators.LoggedAction - [STARTING...]http://www.google.com
14:39:03.003 [pool-2-thread-1] INFO  actions.impl.decorators.LoggedAction - [STARTING...]http://www.ya.ru
14:39:07.467 [pool-2-thread-2] INFO  actions.impl.decorators.LoggedAction - [FINISHED]http://www.google.com
14:39:08.347 [pool-2-thread-2] INFO  actions.impl.decorators.LoggedAction - [STARTING...]http://www.bing.com
14:39:08.951 [pool-2-thread-2] INFO  actions.impl.decorators.LoggedAction - [FINISHED]http://www.bing.com
14:39:09.310 [pool-2-thread-1] INFO  actions.impl.decorators.LoggedAction - [FINISHED]http://www.ya.ru
14:39:10.923 [pool-2-thread-2] INFO  actions.impl.decorators.LoggedAction - [STARTING...]http://www.ya.ru
14:39:11.300 [pool-2-thread-1] INFO  actions.impl.decorators.LoggedAction - [STARTING...]http://www.bing.com
14:39:11.879 [pool-2-thread-1] INFO  actions.impl.decorators.LoggedAction - [FINISHED]http://www.bing.com
14:39:13.304 [pool-2-thread-1] INFO  actions.impl.decorators.LoggedAction - [STARTING...]http://www.ya.ru
14:39:13.732 [pool-2-thread-2] INFO  actions.impl.decorators.LoggedAction - [FINISHED]http://www.ya.ru
14:39:14.098 [pool-2-thread-1] INFO  actions.impl.decorators.LoggedAction - [FINISHED]http://www.ya.ru
14:39:14.172 [pool-2-thread-2] INFO  actions.impl.decorators.LoggedAction - [STARTING...]http://www.google.com
14:39:15.203 [pool-2-thread-2] INFO  actions.impl.decorators.LoggedAction - [FINISHED]http://www.google.com
14:39:15.830 [pool-2-thread-1] INFO  actions.impl.decorators.LoggedAction - [STARTING...]http://www.ya.ru
14:39:16.680 [pool-2-thread-1] INFO  actions.impl.decorators.LoggedAction - [FINISHED]http://www.ya.ru
14:39:16.747 [pool-2-thread-2] INFO  actions.impl.decorators.LoggedAction - [STARTING...]http://www.ya.ru
14:39:17.486 [pool-2-thread-2] INFO  actions.impl.decorators.LoggedAction - [FINISHED]http://www.ya.ru
14:39:17.794 [pool-2-thread-2] INFO  actions.impl.decorators.LoggedAction - [STARTING...]http://www.ya.ru
14:39:18.456 [pool-2-thread-1] INFO  actions.impl.decorators.LoggedAction - [STARTING...]http://www.bing.com
14:39:18.566 [pool-2-thread-2] INFO  actions.impl.decorators.LoggedAction - [FINISHED]http://www.ya.ru
14:39:18.566 [pool-2-thread-2] INFO  engine.actors.Actor - Stopped actor
14:39:19.071 [pool-2-thread-1] INFO  actions.impl.decorators.LoggedAction - [FINISHED]http://www.bing.com
14:39:19.071 [pool-2-thread-1] INFO  engine.actors.Actor - Stopped actor
******************************************************

http://www.google.com: [14:39:02 -> 5099ms] [14:39:14 -> 1031ms] (AVG: 3065ms)
------------
http://www.ya.ru: [14:39:03 -> 6307ms] [14:39:13 -> 795ms] [14:39:15 -> 850ms] [14:39:10 -> 2809ms] [14:39:16 -> 739ms] [14:39:17 -> 772ms] (AVG: 2045ms)
------------
http://www.bing.com: [14:39:11 -> 579ms] [14:39:18 -> 615ms] [14:39:08 -> 604ms] (AVG: 599ms)
------------
Current Date:2018-06-08
******************************************************
```
http://www.google.com: [14:39:02 -> 5099ms] - means that action was executing 5099 milliseconds

You may export data to **CSV** format if you want

You can also:
  - Extend *Actor* class and implement your own scenario methods **onStartActing**, **playScenario**, **onStopActing**
  - Implement **StatelessAction< T >** and **StatefulAction<T, K>** interfaces to write your own Actions
  - Extend **Metrics** and **MetricsDetails** and pass them to Actor contstructor
  - Extend **CommonRunner** class and implement **createActor(int actorIndex)** method to get useful system properties (see example below)

#### CommonRunner example
```java
public class ExampleRunner extends CommonRunner{

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

public class ExampleStarter {

    public static void main(String[] args) {
        Metrics metrics = new ExampleRunner().runLoadTester();
        String csv = metrics.toCSV();//if you need
        System.out.println(metrics.toString());
    }
}
```
Now you may run program with system properties like this
```-DactorsNum=2 -DexecTime=15 -DrandomLoginDelay=10 -DlogoutChance=2 -DactionDelayMin=0 -DactionDelayMax=1"```
It will produce the same result like above. Properties like **randomLoginDelay** and **logoutChance**
are necessary in **RandomizedActor** constructor if you also have some **LoginAction** and **LogoutAction**.
Architecture and behaviour of **RandomizedActor** will be explained below.

## Architecture

### ParallelExecutor
Runs all **Actors** added to it in parallel. Then collects and combines all **Metrics** from each **Actor**.

### Actors
Each **Actor** must implement three methods **onStartActing**, **playScenario**, **onStopActing**
  - **onStartActing** - Runs once when actor starts
  - **playScenario** - Runs many times in cycle until time is finished
  - **onStopActing** - Runs before actor stops
  
#### RandomizedActor
Extends **Actor** and implements **playScenario** this way:
  - Tries to run **LoginAction** if exists. Logins only after 0 to *firstRandomLoginDelay* seconds
  - Gets data from **LoginAction** and calls *(random action from list)*.execute(loginData)
    - If *(random action from list)* is *Stateless* it will just call .execute() without parameters, 
    so no **loginData** will be passed to *action*
  - If **LogoutAction** exists, tries to logout with *logoutChance*
    - After logout, next *login* will start after standard *action delay* (not **firstRandomLoginDelay**)
    
Now I'll give an example on how to create and use **LoginAction**

## Login Action for standard web application
If you have to test usual web application. 
You just create *LoginAction* where using **WebClient** or **WebDriver** you log into a web-site.
And as **WebClient** is a browser, it saves all cookies etc just as in usual browser like **Chrome**.
After that you only must be sure that your **Actions** use the same **WebClient**.
Example:
```java
for (int i = 0; i < numOfActors; i++) {
    WebClient webClient = new WebClient(BrowserVersion.FIREFOX_45);
    webClient.getOptions().setThrowExceptionOnScriptError(false);
    RandomizedActor actor = new RandomizedActor(
            new SomeHtmlUnitLoginAction(webClient, "user", "password", "url"),
            new SomeHtmlUnitLogoutAction(webClient, "url"),
            5, 7.5, 1, 3);

    actor.addAction(new HtmlUnitAction(webClient, "url1"));
    actor.addAction(new HtmlUnitAction(webClient, "url2"));
    actor.addAction(new HtmlUnitAction(webClient, "url3"));

    parallelExecutor.addActor(actor);
}
```

## Login Action for Restful services example
In restful services you usually have to get some data from *auth-service* and call other services using that data.
```java
@Getter
@Setter
public class ExampleLoginData {

    String username;
    String token;
}

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
```
Its pretty straightforward. You may define any Class to return, just extend **StatelessAction< someDataClass >**

Then you have to create some **StatefulAction<returnType, inputType>** to accept *userData* returned by **LoginAction**
## StatefulAction example
**RandomizedActor** passes the data returned by **LoginAction** to all **StatefulActions** in the list
```java
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
```
## Logout action example
It is just a stateful action that uses login data to make logout
```java
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
```
## Starter
Here we create **RandomizedActor** with another constructor which accepts *Login* and *Logout* actions
and additional parameters like *firstRandomLoginDelay* and *logoutChance*
```java
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

```
