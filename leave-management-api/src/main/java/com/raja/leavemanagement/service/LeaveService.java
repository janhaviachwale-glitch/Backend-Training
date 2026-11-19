package com.raja.leavemanagement.service;

import com.raja.leavemanagement.repository.LeaveRepository;
import org.springframework.stereotype.Service;
import com.raja.leavemanagement.model.LeaveRequest;
import com.raja.leavemanagement.model.LeaveStatus;
import com.raja.leavemanagement.dto.LeaveRequestDto;
import com.raja.leavemanagement.dto.LeaveResponseDto;
import com.raja.leavemanagement.exception.LeaveNotFoundException;
import java.util.List;

@Service
public class LeaveService {

    private final LeaveRepository leaveRepository;

    public LeaveService(LeaveRepository leaveRepository) {
        this.leaveRepository = leaveRepository;
    }

    public LeaveResponseDto createLeaveRequest(LeaveRequestDto dto) {
        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new IllegalArgumentException(
                    "Start date cannot be after end date"
            );
        }

        LeaveRequest leaveRequest = mapToEntity(dto);
        leaveRequest.setStatus(LeaveStatus.PENDING);
        LeaveRequest savedRequest = leaveRepository.save(leaveRequest);

        return mapToResponse(savedRequest);
    }

    public List<LeaveResponseDto> getAllLeaveRequests() {
        return leaveRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public LeaveResponseDto getLeaveRequestById(Long id) {
        LeaveRequest leaveRequest = leaveRepository.findById(id)
                .orElseThrow(() -> new LeaveNotFoundException(
                        "Leave request not found with ID: " + id
                ));

        return mapToResponse(leaveRequest);
    }

    public LeaveResponseDto updateLeaveRequest(
            Long id,
            LeaveRequestDto dto
    ) {
        LeaveRequest existingRequest = leaveRepository.findById(id)
            .orElseThrow(() -> new LeaveNotFoundException(
                    "Leave request not found with ID: " + id
            ));

        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new IllegalArgumentException(
                    "Start date cannot be after end date"
            );
        }

        existingRequest.setEmployeeId(dto.getEmployeeId());
        existingRequest.setLeaveType(dto.getLeaveType());
        existingRequest.setStartDate(dto.getStartDate());
        existingRequest.setEndDate(dto.getEndDate());
        existingRequest.setReason(dto.getReason());

        LeaveRequest updatedRequest = leaveRepository.save(existingRequest);

        return mapToResponse(updatedRequest);
    }

    public void deleteLeaveRequest(Long id) {
        if (leaveRepository.findById(id).isEmpty()) {
            throw new LeaveNotFoundException(
                    "Leave request not found with ID: " + id
            );
        }

        leaveRepository.deleteById(id);
    }

    private LeaveRequest mapToEntity(LeaveRequestDto dto) {
        LeaveRequest leaveRequest = new LeaveRequest();

        leaveRequest.setEmployeeId(dto.getEmployeeId());
        leaveRequest.setLeaveType(dto.getLeaveType());
        leaveRequest.setStartDate(dto.getStartDate());
        leaveRequest.setEndDate(dto.getEndDate());
        leaveRequest.setReason(dto.getReason());

        return leaveRequest;
    }

    private LeaveResponseDto mapToResponse(LeaveRequest leaveRequest) {
        return new LeaveResponseDto(
                leaveRequest.getId(),
                leaveRequest.getEmployeeId(),
                leaveRequest.getLeaveType(),
                leaveRequest.getStartDate(),
                leaveRequest.getEndDate(),
                leaveRequest.getReason(),
                leaveRequest.getStatus()
        );
    }
}