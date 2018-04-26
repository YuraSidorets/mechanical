package org.patterns.mechanical.service.pipeline;
import org.patterns.mechanical.mediator.CallCenterComponent;
import org.patterns.mechanical.model.RepairRequest;
import org.patterns.mechanical.model.User;

public class CallCenterMiddleware extends Middleware {
    @Override
    public boolean check(User user, RepairRequest request) {
        if(!new CallCenterComponent().fireAction(request)){
            return checkNext(user, request);
        }
        return true;
    }
}
