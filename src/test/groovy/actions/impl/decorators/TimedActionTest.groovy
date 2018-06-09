package actions.impl.decorators

import actions.StatelessAction
import model.exceptions.ApplicationException
import spock.lang.Specification

class TimedActionTest extends Specification {

    //Stateless tests
    StatelessAction statelessAction

    void setup() {
        statelessAction = Mock(StatelessAction)
    }

    def "Execute should record execution time no matter what happens"() {
        given: "Action that throws exception"
        statelessAction.execute(_) >> {throw new ApplicationException("something went wrong")}
        TimedAction timedAction = new TimedAction(statelessAction)

        when:
        try {
            timedAction.execute()
        } catch(Exception e) {}

        then: "Execution time must not be equal -1 (default value)"
        timedAction.getExecutionTime() != -1
    }

    def "Execute should throw exact same exception that was thrown"() {
        given: "Action that throws exception"
        statelessAction.execute(_) >> {throw new ApplicationException("something went wrong")}
        TimedAction timedAction = new TimedAction(statelessAction)

        when:
        timedAction.execute()

        then: "Exception must be ApplicationException"
        thrown(ApplicationException)
    }
}
