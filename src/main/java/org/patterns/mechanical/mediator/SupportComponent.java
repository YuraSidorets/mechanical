package org.patterns.mechanical.mediator;

import org.patterns.mechanical.model.RepairRequest;
import org.patterns.mechanical.service.RepairRequestProcessor;

public class SupportComponent implements Component {

    private Mediator mediator;

    @Override
    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public String getName() {
        return "Support";
    }

    public Boolean fireAction(RepairRequest request){
        return mediator.send(request, this);
    }

    public Boolean getSupport(RepairRequest request) {
        RepairRequestProcessor processor = RepairRequestProcessor.getInstance();
        return processor.processRequest(request);
    }
}
