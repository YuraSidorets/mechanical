package org.patterns.mechanical.service.pipeline;

import org.patterns.mechanical.mediator.SupportComponent;
import org.patterns.mechanical.model.RepairRequest;
import org.patterns.mechanical.model.RequestState;
import org.patterns.mechanical.model.User;
import org.patterns.mechanical.service.RepairRequestProcessor;

public class SupportMiddleware extends Middleware {
	private SupportComponent supportComponent;

	public SupportMiddleware(SupportComponent supportComponent){
		this.supportComponent = supportComponent;
	}

    @Override
    public boolean check(User user, RepairRequest request) {
        if (!supportComponent.fireAction(request)) {
            return checkNext(user, request);
        }
        else {
            return true;
        }
    }
}
