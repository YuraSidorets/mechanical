package org.patterns.mechanical.dao;

import org.patterns.mechanical.model.RepairRequest;

import java.util.List;
import java.util.Optional;

public interface RepairRequestDao {
    List<RepairRequest> findAll();
    List<RepairRequest> findByUserId(long userId);
    List<RepairRequest> findByEmployeeId(long employeeId);
    Optional<RepairRequest> findOne(long repairRequestId);
    RepairRequest save(RepairRequest repairRequest);
    void delete(long repairRequestId);
}
