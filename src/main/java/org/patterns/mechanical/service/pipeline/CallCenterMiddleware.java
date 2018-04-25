package org.patterns.mechanical.service.pipeline;

import org.patterns.mechanical.model.RepairRequest;
import org.patterns.mechanical.model.RequestState;
import org.patterns.mechanical.model.User;
import org.patterns.mechanical.service.RepairRequestProcessor;

public class CallCenterMiddleware extends Middleware {
    @Override
    public boolean check(User user, RepairRequest request) {
        request.setStatus(RequestState.CALL_CENTER);
        RepairRequestProcessor processor = RepairRequestProcessor.getInstance();
        boolean assigned = processor.processRequest(request);
        if (assigned) {
            request.setResponse("Requires call center agent contact");
            return true;
        }
        return checkNext(user, request);
    }
}
