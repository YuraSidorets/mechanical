package org.patterns.mechanical.service.pipeline;

import org.patterns.mechanical.mediator.*;
import org.patterns.mechanical.model.RepairRequest;
import org.springframework.stereotype.Component;

@Component
public class RepairRequestPipelineImpl implements RepairRequestPipeline {
    @Override
    public Middleware construct(RepairRequest repairRequest) {
        Middleware start;

        Mediator inspector = new Inspector();
        AutonomousReplyComponent autonomousReply = new AutonomousReplyComponent();
        CallCenterComponent callCenter = new CallCenterComponent();
        SupportComponent supportComponent = new SupportComponent();

        autonomousReply.setMediator(inspector);
        callCenter.setMediator(inspector);
        supportComponent.setMediator(inspector);

        inspector.registerComponent(autonomousReply);
        inspector.registerComponent(callCenter);
        inspector.registerComponent(supportComponent);


        switch (repairRequest.getStatus()) {
            case PROCESSING:
                start = new AutonomousReplyMiddleware(autonomousReply);
                start.linkWith(new CallCenterMiddleware(callCenter))
                        .linkWith(new SupportMiddleware(supportComponent));
                break;
            case AUTO_REPLIED:
                start = new CallCenterMiddleware(callCenter);
                start.linkWith(new SupportMiddleware(supportComponent));
                break;
            case CALL_CENTER:
                start = new SupportMiddleware(supportComponent);
                break;
            default:
                throw new IllegalArgumentException("Invalid request state");
        }
        return start;
    }
}
