package org.patterns.mechanical.dao;

import org.patterns.mechanical.model.RepairRequest;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class RepairRequestDaoImpl implements RepairRequestDao {
    private AtomicInteger idGenerator;
    private List<RepairRequest> items;

    public RepairRequestDaoImpl() {
        items = new ArrayList<>();
        idGenerator = new AtomicInteger(0);
    }

    @Override
    public List<RepairRequest> findAll() {
        return Collections.unmodifiableList(items);
    }

    @Override
    public List<RepairRequest> findByUserId(long userId) {
        return items.stream()
                .filter(r -> r.getUserId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public List<RepairRequest> findByEmployeeId(long employeeId) {
        return items.stream()
                .filter(r -> r.getMechanicId() == employeeId)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RepairRequest> findOne(long repairRequestId) {
        return items.stream().filter(r -> r.getId() == repairRequestId)
                .findFirst()
                .map(RepairRequest::clone);
    }

    @Override
    public RepairRequest save(RepairRequest repairRequest) {
        if (repairRequest.getId() == null) {
            repairRequest.setId(idGenerator.incrementAndGet());
        } else {
            items.removeIf(r -> r.getId().equals(repairRequest.getId()));
        }
        items.add(repairRequest.clone());
        return repairRequest;
    }

    @Override
    public void delete(long repairRequestId) {
        items.removeIf(r -> r.getId().equals(repairRequestId));
    }
}
