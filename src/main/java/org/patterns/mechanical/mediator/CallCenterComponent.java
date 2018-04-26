package org.patterns.mechanical.mediator;

import org.patterns.mechanical.model.RepairRequest;
import org.patterns.mechanical.service.RepairRequestProcessor;

public class CallCenterComponent implements Component {
    private Mediator mediator;

    @Override
    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public String getName() {
        return "CallCenter";
    }

    public Boolean fireAction(RepairRequest request){
        return mediator.send(request, this);
    }

    public Boolean getCallReply(RepairRequest request) {
        RepairRequestProcessor processor = RepairRequestProcessor.getInstance();
        return processor.processRequest(request);
    }
}
