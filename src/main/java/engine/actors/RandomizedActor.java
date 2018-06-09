package engine.actors;

import actions.Action;
import actions.impl.decorators.TimedAction;
import model.Metrics;
import util.Randomizer;

import java.util.ArrayList;
import java.util.List;

public class RandomizedActor<T, K> extends Actor {

    protected boolean isFirstLogIn = true;
    protected boolean isLoggedIn = false;
    protected int randomLoginDelay;
    protected double logoutChance;
    protected int actionDelayMin;
    protected int actionDelayMax;

    protected List<TimedAction<T, K>> actions = new ArrayList<>();
    protected TimedAction<K, K> loginAction;
    protected TimedAction<T, K> logoutAction;

    protected K loggedInData;

    public RandomizedActor(int actionDelayMin, int actionDelayMax, Metrics metrics) {
        super(metrics);
        this.actionDelayMin = actionDelayMin;
        this.actionDelayMax = actionDelayMax;
    }

    public RandomizedActor(int actionDelayMin, int actionDelayMax) {
        this(actionDelayMin, actionDelayMax, new Metrics());
    }

    public RandomizedActor(Action loginAction, Action logoutAction,
                           int randomFirstLoginDelay, double logoutChance, int actionDelayMin, int actionDelayMax,
                           Metrics metrics) {
        this(actionDelayMin, actionDelayMax, metrics);
        this.loginAction = new TimedAction(loginAction);
        this.logoutAction = logoutAction != null ? new TimedAction(logoutAction) : null;
        this.logoutChance = logoutChance;
        this.randomLoginDelay = randomFirstLoginDelay;
    }

    public RandomizedActor(Action loginAction, Action logoutAction,
                           int randomLoginDelay, double logoutChance, int actionDelayMin, int actionDelayMax) {
        this(loginAction, logoutAction, randomLoginDelay, logoutChance, actionDelayMin,
                actionDelayMax, new Metrics());
    }

    public void addAction(Action action) {
        actions.add(new TimedAction(action));
    }

    @Override
    public void onStartActing() {
        logger.info("Starting actor");
    }

    @Override
    public void playScenario() {
        loginIfNotAlready();
        if (isLoggedIn || loginAction == null) {
            delayAndExecuteAnyAction();
            logoutMayBe(logoutChance);
        }
    }

    @Override
    public void onStopActing() {
        logoutMayBe(100);
        logger.info("Stopped actor");

    }

    protected void loginIfNotAlready() {
        if (loginAction != null && !isLoggedIn) {
            if (isFirstLogIn) {
                sleepThread(Randomizer.randomValue(0, randomLoginDelay * 1000));
                isFirstLogIn = false;
            } else {
                sleepThread(Randomizer.randomValue(actionDelayMin * 1000, actionDelayMax * 1000));
            }
            loggedInData = executeAction(loginAction);
            if (loggedInData != null)
                isLoggedIn = true;
        }
    }

    protected void logoutMayBe(double chance) {
        if (logoutAction != null && Randomizer.throwTheCoin(chance)) {
            executeAction(logoutAction, loggedInData);
            isLoggedIn = false;
        }
    }

    protected void delayAndExecuteAnyAction() {
        sleepThread(Randomizer.randomValue(actionDelayMin * 1000, actionDelayMax * 1000));
        int random = Randomizer.randomValue(0, actions.size() - 1);
        TimedAction currentAction = actions.get(random);
        //execute action with logged in data, in case actions are stateful
        executeAction(currentAction, loggedInData);
    }

    protected void sleepThread(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
