package org.patterns.mechanical.service.pipeline;

import org.patterns.mechanical.model.RepairRequest;

public interface RepairRequestPipeline {
    Middleware construct(RepairRequest repairRequest);
}
