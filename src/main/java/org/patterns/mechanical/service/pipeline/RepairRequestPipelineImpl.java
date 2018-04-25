package org.patterns.mechanical.service.pipeline;

import org.patterns.mechanical.model.RepairRequest;
import org.springframework.stereotype.Component;

@Component
public class RepairRequestPipelineImpl implements RepairRequestPipeline {
    @Override
    public Middleware construct(RepairRequest repairRequest) {
        Middleware start;
        switch (repairRequest.getStatus()) {
            case PROCESSING:
                start = new AutonomousReplyMiddleware();
                start.linkWith(new CallCenterMiddleware())
                        .linkWith(new SupportMiddleware());
                break;
            case AUTO_REPLIED:
                start = new CallCenterMiddleware();
                start.linkWith(new SupportMiddleware());
                break;
            case CALL_CENTER:
                start = new SupportMiddleware();
                break;
            default:
                throw new IllegalArgumentException("Invalid request state");
        }
        return start;
    }
}
