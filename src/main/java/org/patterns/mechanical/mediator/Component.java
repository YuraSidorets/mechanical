package org.patterns.mechanical.mediator;

import org.patterns.mechanical.model.RepairRequest;

public interface Component {
    void setMediator(Mediator mediator);
    String getName();
    Boolean fireAction(RepairRequest request);
}
