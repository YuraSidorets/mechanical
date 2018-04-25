package org.patterns.mechanical.service.pipeline;

import org.patterns.mechanical.model.RepairRequest;
import org.patterns.mechanical.model.RequestState;
import org.patterns.mechanical.model.User;
import org.patterns.mechanical.service.RepairRequestProcessor;

public class SupportMiddleware extends Middleware {
    @Override
    public boolean check(User user, RepairRequest request) {
        request.setStatus(RequestState.SUPPORT);
        RepairRequestProcessor processor = RepairRequestProcessor.getInstance();
        processor.processRequest(request);
        request.setResponse("Requires support agent contact");
        return checkNext(user, request);
    }
}
