package runners;

import engine.ParallelExecutor;
import engine.actors.Actor;
import model.Metrics;
import org.apache.log4j.Logger;

abstract public class CommonRunner {

    public static Logger logger = Logger.getLogger(CommonRunner.class);

    protected int actorsNum = 10;
    protected int actorExecutionTime = 300;
    protected String baseUrl = "http://localhost:80";
    protected String username = "test";
    protected String password = "test";
    protected int randomLoginDelay = 80;
    protected double logoutChance = 0.5;
    protected int actionDelayMin = 5;
    protected int actionDelayMax = 15;


    public CommonRunner() {
        extractProperties();
    }

    public Metrics runLoadTester() {
        ParallelExecutor parallelExecutor = new ParallelExecutor();
        for (int i = 0; i < actorsNum; i++) {
            parallelExecutor.addActor(createActor(i));
        }
        return parallelExecutor.executeAll(actorExecutionTime);
    }

    abstract protected Actor createActor(int actorIndex);

    protected void extractProperties() {
        String actorsNumProp = System.getProperty("actorsNum");
        String actorExecutionTimeProp = System.getProperty("execTime");
        String baseUrlProp = System.getProperty("baseUrl");
        String usernameProp = System.getProperty("username");
        String passwordProp = System.getProperty("password");
        String randomLoginDelayProp = System.getProperty("randomLoginDelay");
        String logoutChanceProp = System.getProperty("logoutChance");
        String actionDelayMinProp = System.getProperty("actionDelayMin");
        String actionDelayMaxProp = System.getProperty("actionDelayMax");
        if (actorsNumProp != null) actorsNum = Integer.parseInt(actorsNumProp);
        if (actorExecutionTimeProp != null) actorExecutionTime = Integer.parseInt(actorExecutionTimeProp);
        if (baseUrlProp != null) baseUrl = baseUrlProp;
        if (usernameProp != null) username = usernameProp;
        if (passwordProp != null) password = passwordProp;
        if (randomLoginDelayProp != null) randomLoginDelay = Integer.parseInt(randomLoginDelayProp);
        if (logoutChanceProp != null) logoutChance = Double.parseDouble(logoutChanceProp);
        if (actionDelayMinProp != null) actionDelayMin = Integer.parseInt(actionDelayMinProp);
        if (actionDelayMaxProp != null) actionDelayMax = Integer.parseInt(actionDelayMaxProp);
    }

}
