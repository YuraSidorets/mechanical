package org.patterns.mechanical.mediator;
import org.patterns.mechanical.model.RepairRequest;

public interface Mediator {
    void registerComponent(Component component);
    Boolean send(RepairRequest request, Component component);
}
