package org.patterns.mechanical.service.pipeline;

import org.patterns.mechanical.model.RepairRequest;
import org.patterns.mechanical.model.RequestState;
import org.patterns.mechanical.model.User;

import java.time.LocalDateTime;
import java.util.Map;

public class AutonomousReplyMiddleware extends Middleware {
    private Map<String, String> commonAnswers =
            Map.of("BSOD", "Reset computer.",
                    "Mouse not working", "Replace batteries");

    @Override
    public boolean check(User user, RepairRequest request) {
        String answer = commonAnswers.get(request.getProblem());
        if (answer == null) {
            return checkNext(user, request);
        }
        else {
            request.setResponse(answer);
            request.setResolved(true);
            request.setStatus(RequestState.AUTO_REPLIED);
            request.setUpdatedAt(LocalDateTime.now());
            return true;
        }
    }
}
