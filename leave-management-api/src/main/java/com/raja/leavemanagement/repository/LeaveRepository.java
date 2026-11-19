package com.raja.leavemanagement.repository;

import com.raja.leavemanagement.model.LeaveRequest;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class LeaveRepository {

    private final ConcurrentHashMap<Long, LeaveRequest> leaveRequests =
            new ConcurrentHashMap<>();

    private final AtomicLong idGenerator = new AtomicLong(1);

    public LeaveRequest save(LeaveRequest leaveRequest) {
        if (leaveRequest.getId() == null) {
            leaveRequest.setId(idGenerator.getAndIncrement());
        }

        leaveRequests.put(leaveRequest.getId(), leaveRequest);

        return leaveRequest;
    }

    public List<LeaveRequest> findAll() {
        return new ArrayList<>(leaveRequests.values());
    }

    public Optional<LeaveRequest> findById(Long id) {
        return Optional.ofNullable(leaveRequests.get(id));
    }

    public void deleteById(Long id) {
        leaveRequests.remove(id);
    }
}