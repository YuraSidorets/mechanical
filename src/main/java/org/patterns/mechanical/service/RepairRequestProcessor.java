package org.patterns.mechanical.service;

import org.patterns.mechanical.dao.RepairRequestDao;
import org.patterns.mechanical.dao.UserDao;
import org.patterns.mechanical.model.RepairRequest;
import org.patterns.mechanical.model.User;
import org.springframework.context.ApplicationContext;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

public class RepairRequestProcessor {
    private RepairRequestDao repairRequestDao;
    private UserDao userDao;

    private PriorityBlockingQueue<User> callCenterAgents;
    private PriorityBlockingQueue<User> supportAgents;

    private RepairRequestProcessor() {
        ApplicationContext ctx = SpringContextProvider.getApplicationContext();
        this.repairRequestDao = ctx.getBean(RepairRequestDao.class);
        this.userDao = ctx.getBean(UserDao.class);

        callCenterAgents = new PriorityBlockingQueue<>(11, Comparator.comparingInt(User::getId));
        supportAgents = new PriorityBlockingQueue<>(11, Comparator.comparingInt(User::getId));
    }

    private static class RepairRequestProcessorHolder {
        static final RepairRequestProcessor INSTANCE = new RepairRequestProcessor();
    }

    public static RepairRequestProcessor getInstance() {
        return RepairRequestProcessorHolder.INSTANCE;
    }

    public void addEmployee(User employee) {
        switch (employee.getRole()) {
            case "CALL_CENTER":
                callCenterAgents.add(employee);
                break;
            case "SUPPORT":
                supportAgents.add(employee);
                break;
        }
    }

    public void removeEmployee(User employee) {
        switch (employee.getRole()) {
            case "CALL_CENTER":
                callCenterAgents.removeIf(e -> e.getId().equals(employee.getId()));
                break;
            case "SUPPORT":
                supportAgents.removeIf(e -> e.getId().equals(employee.getId()));
                break;
        }
    }

    public boolean processRequest(RepairRequest repairRequest) {
        try {
            User worker;
            switch (repairRequest.getStatus()) {
                case CALL_CENTER:
                    worker = callCenterAgents.poll();
                    if (worker == null) {
                        return false;
                    }
                    repairRequest.setMechanicId(worker.getId());
                    repairRequest.setUpdatedAt(LocalDateTime.now());
                    repairRequestDao.save(repairRequest);
                    callCenterAgents.put(worker);
                    return true;
                case SUPPORT:
                    worker = supportAgents.take();
                    repairRequest.setMechanicId(worker.getId());
                    repairRequest.setUpdatedAt(LocalDateTime.now());
                    repairRequestDao.save(repairRequest);
                    supportAgents.put(worker);
                    return true;
                default:
                    throw new IllegalArgumentException("Request can't be processed");
            }
        } catch (InterruptedException ex) {
            throw new RuntimeException("Error while processing requests", ex);
        }
    }
}
