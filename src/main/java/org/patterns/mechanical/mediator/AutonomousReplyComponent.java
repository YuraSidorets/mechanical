package org.patterns.mechanical.mediator;

import org.patterns.mechanical.model.RepairRequest;

import java.util.Map;

public class AutonomousReplyComponent implements Component {
    private Map<String, String> commonAnswers =
            Map.of("BSOD", "Reset computer.",
                    "Mouse not working", "Replace batteries");
    private Mediator mediator;

    @Override
    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public String getName() {
        return "AutoReply";
    }

    public Boolean fireAction(RepairRequest problem){
         return mediator.send(problem,this);
    }

    public String getAutoReply(RepairRequest request){
        return commonAnswers.get(request.getProblem());
    }
}
