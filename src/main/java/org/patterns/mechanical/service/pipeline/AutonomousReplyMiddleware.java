package org.patterns.mechanical.service.pipeline;
import org.patterns.mechanical.mediator.AutonomousReplyComponent;
import org.patterns.mechanical.model.RepairRequest;
import org.patterns.mechanical.model.User;


public class AutonomousReplyMiddleware extends Middleware {
    @Override
    public boolean check(User user, RepairRequest request) {
       if (!new AutonomousReplyComponent().fireAction(request)) {
            return checkNext(user, request);
        }
        else {
            return true;
        }
    }
}
