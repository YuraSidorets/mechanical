package org.patterns.mechanical.service.pipeline;

import org.patterns.mechanical.model.RepairRequest;
import org.patterns.mechanical.model.User;

public abstract class Middleware {
    private Middleware next;

    public Middleware linkWith(Middleware next) {
        this.next = next;
        return next;
    }

    public abstract boolean check(User user, RepairRequest request);

    protected boolean checkNext(User user, RepairRequest request) {
        if (next == null) {
            return false;
        }
        return next.check(user, request);
    }
}
