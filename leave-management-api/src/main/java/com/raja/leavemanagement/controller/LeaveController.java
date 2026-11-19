package com.raja.leavemanagement.controller;

import com.raja.leavemanagement.service.LeaveService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.raja.leavemanagement.dto.LeaveResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.raja.leavemanagement.dto.LeaveRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@RestController
@RequestMapping("/leaves")
public class LeaveController {

    private final LeaveService leaveService;

    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @GetMapping
    public ResponseEntity<List<LeaveResponseDto>> getAllLeaveRequests() {
        return ResponseEntity.ok(leaveService.getAllLeaveRequests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaveResponseDto> getLeaveRequestById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(leaveService.getLeaveRequestById(id));
    }

    @PostMapping
    public ResponseEntity<LeaveResponseDto> createLeaveRequest(
            @Valid @RequestBody LeaveRequestDto dto
    ) {
        LeaveResponseDto createdRequest =
                leaveService.createLeaveRequest(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeaveResponseDto> updateLeaveRequest(
            @PathVariable Long id,
            @Valid @RequestBody LeaveRequestDto dto
    ) {
        LeaveResponseDto updatedRequest =
                leaveService.updateLeaveRequest(id, dto);

        return ResponseEntity.ok(updatedRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeaveRequest(
            @PathVariable Long id
    ) {
        leaveService.deleteLeaveRequest(id);

        return ResponseEntity.noContent().build();
    }
}