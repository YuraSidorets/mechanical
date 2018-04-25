package org.patterns.mechanical.model;

import org.patterns.mechanical.dao.RepairRequestDao;
import org.patterns.mechanical.dao.UserDao;
import org.patterns.mechanical.service.pipeline.Middleware;
import org.patterns.mechanical.service.pipeline.RepairRequestPipeline;
import org.springframework.stereotype.Component;

@Component
public class RepairRequestsObserver implements Observer{
    private RepairRequestDao repairRequestDao;
    private UserDao userDao;
    private RepairRequestPipeline requestPipeline;

    public RepairRequestsObserver(RepairRequestDao repairRequestDao, UserDao userDao, RepairRequestPipeline requestPipeline) {
        this.repairRequestDao = repairRequestDao;
        this.userDao = userDao;
        this.requestPipeline = requestPipeline;
    }

    public void changeState(RepairRequest repairRequest) {
        RepairRequest requestToUpdate;
        if(repairRequest.getId() != null) {
            requestToUpdate = repairRequestDao.findOne(repairRequest.getId()).get();
        }
        else {
            requestToUpdate = repairRequestDao.save(repairRequest);
        }

        requestToUpdate.setStatus(repairRequest.getStatus());
        repairRequestDao.save(requestToUpdate);

        User user = userDao.findOne(repairRequest.getUserId()).get();

        Middleware mv = requestPipeline.construct(repairRequest);
        mv.check(user, repairRequest);

        repairRequestDao.save(requestToUpdate);
    }
}
