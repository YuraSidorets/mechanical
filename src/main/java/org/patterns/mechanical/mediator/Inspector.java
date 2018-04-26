package org.patterns.mechanical.mediator;

import org.patterns.mechanical.model.RepairRequest;
import org.patterns.mechanical.model.RequestState;

public class Inspector implements Mediator {
    private AutonomousReplyComponent autoReply;
    private CallCenterComponent callCenter;
    private SupportComponent support;
    private ResponseHandler responseHandler;

    @Override
    public void registerComponent(Component component) {
        component.setMediator(this);
        switch (component.getName()){
            case "AutoReply" : autoReply = (AutonomousReplyComponent)component; break;
            case "CallCenter" : callCenter = (CallCenterComponent)component; break;
            case "Support" : support = (SupportComponent)component;break;
        }
    }

    @Override
    public Boolean send(RepairRequest request, Component component){
        if (component == autoReply){
            String answer = autoReply.getAutoReply(request);
            if (answer != null){
                responseHandler = new ResponseHandler();
                responseHandler.setResponse(request, answer);
                responseHandler.setResolved(request);
                responseHandler.setStatus(request, RequestState.AUTO_REPLIED);
                responseHandler.setUpdated(request);
                return true;
            }
            else {
                return false;
            }

        }
        else if (component == callCenter){
            if (callCenter.getCallReply(request)){
                request.setResponse("Requires call center agent contact");
                return true;
            }
            else {
                return false;
            }
        }
        else if (component == support){
            if (support.getSupport(request)){
                request.setStatus(RequestState.SUPPORT);
                request.setResponse("Requires support agent contact");
                return true;
            }else {
                return false;
            }
        }
        return false;
    }
}
