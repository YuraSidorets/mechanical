package org.patterns.mechanical.mediator;

import org.patterns.mechanical.model.RepairRequest;
import org.patterns.mechanical.model.RequestState;

import java.time.LocalDateTime;

public class ResponseHandler {
    public void setResponse(RepairRequest request, String response) {
        request.setResponse(response);
    }

    public void setResolved(RepairRequest request) {
        request.setResolved(true);
    }

    public void setUpdated(RepairRequest request) {
        request.setUpdatedAt(LocalDateTime.now());
    }

    public void setStatus(RepairRequest request,RequestState status) {
        request.setStatus(status);
    }
}
