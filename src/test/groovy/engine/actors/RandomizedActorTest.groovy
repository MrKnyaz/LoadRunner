package engine.actors

import actions.StatefulAction
import actions.StatelessAction
import model.Metrics
import model.exceptions.ApplicationException
import spock.lang.Specification

class RandomizedActorTest extends Specification {

    //Stateless tests
    StatelessAction statelessAction
    Actor simpleActor

    //Statefull tests
    StatelessAction<String> loginAction
    StatefulAction<Integer, String> statefulAction
    StatefulAction<Integer, String> logoutAction
    Actor signedInActor

    void setup() {
        statelessAction = Mock(StatelessAction)
        simpleActor = new RandomizedActor<String, String>(0, 1)
        simpleActor.addAction(statelessAction)
    }

    def "Scenario must execute correctly and put data into Metrics"() {
        given: "action that executes correctly"
        1 * statelessAction.execute(_) >> "some data"
        statelessAction.getName() >> "mockAction"

        when: "scenario is played"
        simpleActor.playScenario()

        then: "no scenario interruption should occur and MetricDetails should contain data"
        Metrics metrics = simpleActor.getMetrics()
        metrics.getData().get("mockAction").size() == 1
        String data = metrics.getData().get("mockAction").get(0).toString()
        !data.isEmpty()
    }

    def "Metrics should have an error when action throws exception"() {
        given: "action that throws exception"
        1 * statelessAction.execute(_) >> { throw new ApplicationException("something went wrong") }
        statelessAction.getName() >> "mockAction"

        when: "scenario is played"
        simpleActor.playScenario()

        then: "no scenario interruption should occur and MetricDetails should contain error"
        Metrics metrics = simpleActor.getMetrics()
        metrics.getData().get("mockAction").size() == 1
        String errorMessage = metrics.getData().get("mockAction").get(0).getError()
        errorMessage.equals("something went wrong")
    }

    def "Login data must be passed to actions"() {
        given: "Login and simple action"
        loginAction = Mock(StatelessAction)
        statefulAction = Mock(StatefulAction)
        logoutAction = Mock(StatefulAction)
        signedInActor = new RandomizedActor(loginAction, logoutAction, 0, 100, 0, 1)
        signedInActor.addAction(statefulAction)

        and: "interactions"
        1 * loginAction.execute(_) >> "loginData"
        loginAction.getName() >> "mockLoginAction"
        1 * logoutAction.execute(_ as String) >> ""
        logoutAction.getName() >> "logoutAction"
        1 * statefulAction.execute(_ as String) >> {
            String s -> if (s == null || s != "loginData") throw new ApplicationException("no data") else return "ok"
        }
        statefulAction.getName() >> "mockAction"

        when: "scenario is played"
        signedInActor.playScenario()

        then: "no scenario interruption should occur and MetricDetails should NOT contain error"
        Metrics metrics = signedInActor.getMetrics()
        metrics.getData().get("mockAction").size() == 1
        String errorMessage = metrics.getData().get("mockAction").get(0).getError()
        errorMessage == null
    }

}
