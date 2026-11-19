package com.raja.leavemanagement.service;

import com.raja.leavemanagement.exception.LeaveNotFoundException;
import com.raja.leavemanagement.repository.LeaveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.raja.leavemanagement.dto.LeaveRequestDto;
import com.raja.leavemanagement.dto.LeaveResponseDto;
import com.raja.leavemanagement.model.LeaveRequest;
import com.raja.leavemanagement.model.LeaveStatus;
import com.raja.leavemanagement.model.LeaveType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LeaveServiceTest {

    @Mock
    private LeaveRepository leaveRepository;

    @InjectMocks
    private LeaveService leaveService;

    @BeforeEach
    void setUp() {
        // Test setup will be added here.
    }

    @Test
    void shouldCreateLeaveRequestSuccessfully() {
        LeaveRequestDto dto = new LeaveRequestDto();
        dto.setEmployeeId("EMP101");
        dto.setLeaveType(LeaveType.CASUAL);
        dto.setStartDate(LocalDate.of(2026, 12, 20));
        dto.setEndDate(LocalDate.of(2026, 12, 22));
        dto.setReason("Personal work");

        when(leaveRepository.save(any(LeaveRequest.class)))
                .thenAnswer(invocation -> {
                    LeaveRequest request = invocation.getArgument(0);
                    request.setId(1L);
                    return request;
                });

        LeaveResponseDto response = leaveService.createLeaveRequest(dto);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("EMP101", response.getEmployeeId());
        assertEquals(LeaveType.CASUAL, response.getLeaveType());
        assertEquals(LeaveStatus.PENDING, response.getStatus());

        verify(leaveRepository).save(any(LeaveRequest.class));
    }

    @Test
    void shouldRejectLeaveRequestWhenStartDateIsAfterEndDate() {
        LeaveRequestDto dto = new LeaveRequestDto();
        dto.setEmployeeId("EMP101");
        dto.setLeaveType(LeaveType.CASUAL);
        dto.setStartDate(LocalDate.of(2026, 12, 25));
        dto.setEndDate(LocalDate.of(2026, 12, 20));
        dto.setReason("Personal work");

        assertThrows(
                IllegalArgumentException.class,
                () -> leaveService.createLeaveRequest(dto)
        );

        verify(leaveRepository, never()).save(any(LeaveRequest.class));
    }

    @Test
    void shouldReturnAllLeaveRequests() {
        LeaveRequest request = new LeaveRequest(
                1L,
                "EMP101",
                LeaveType.CASUAL,
                LocalDate.of(2026, 12, 20),
                LocalDate.of(2026, 12, 22),
                "Personal work",
                LeaveStatus.PENDING
        );

        when(leaveRepository.findAll())
                .thenReturn(List.of(request));

        List<LeaveResponseDto> response =
                leaveService.getAllLeaveRequests();

        assertEquals(1, response.size());
        assertEquals(1L, response.get(0).getId());
        assertEquals("EMP101", response.get(0).getEmployeeId());
        assertEquals(LeaveStatus.PENDING, response.get(0).getStatus());

        verify(leaveRepository).findAll();
    }

    @Test
    void shouldReturnLeaveRequestById() {
        LeaveRequest request = new LeaveRequest(
                1L,
                "EMP101",
                LeaveType.CASUAL,
                LocalDate.of(2026, 12, 20),
                LocalDate.of(2026, 12, 22),
                "Personal work",
                LeaveStatus.PENDING
        );

        when(leaveRepository.findById(1L))
                .thenReturn(Optional.of(request));

        LeaveResponseDto response =
                leaveService.getLeaveRequestById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("EMP101", response.getEmployeeId());
        assertEquals(LeaveType.CASUAL, response.getLeaveType());

        verify(leaveRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenLeaveRequestDoesNotExist() {
        when(leaveRepository.findById(99L))
                .thenReturn(Optional.empty());

        LeaveNotFoundException exception = assertThrows(
                LeaveNotFoundException.class,
                () -> leaveService.getLeaveRequestById(99L)
        );

        assertEquals(
                "Leave request not found with ID: 99",
                exception.getMessage()
        );

        verify(leaveRepository).findById(99L);
    }

    @Test
    void shouldUpdateLeaveRequestSuccessfully() {
        LeaveRequest existingRequest = new LeaveRequest(
                1L,
                "EMP101",
                LeaveType.CASUAL,
                LocalDate.of(2026, 12, 20),
                LocalDate.of(2026, 12, 22),
                "Personal work",
                LeaveStatus.PENDING
        );

        LeaveRequestDto dto = new LeaveRequestDto();
        dto.setEmployeeId("EMP102");
        dto.setLeaveType(LeaveType.SICK);
        dto.setStartDate(LocalDate.of(2026, 12, 25));
        dto.setEndDate(LocalDate.of(2026, 12, 27));
        dto.setReason("Medical appointment");

        when(leaveRepository.findById(1L))
                .thenReturn(Optional.of(existingRequest));

        when(leaveRepository.save(any(LeaveRequest.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        LeaveResponseDto response =
                leaveService.updateLeaveRequest(1L, dto);

        assertEquals(1L, response.getId());
        assertEquals("EMP102", response.getEmployeeId());
        assertEquals(LeaveType.SICK, response.getLeaveType());
        assertEquals("Medical appointment", response.getReason());
        assertEquals(LeaveStatus.PENDING, response.getStatus());

        verify(leaveRepository).save(existingRequest);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentLeaveRequest() {
        LeaveRequestDto dto = new LeaveRequestDto();
        dto.setEmployeeId("EMP102");
        dto.setLeaveType(LeaveType.SICK);
        dto.setStartDate(LocalDate.of(2026, 12, 25));
        dto.setEndDate(LocalDate.of(2026, 12, 27));
        dto.setReason("Medical appointment");

        when(leaveRepository.findById(99L))
                .thenReturn(Optional.empty());

        LeaveNotFoundException exception = assertThrows(
                LeaveNotFoundException.class,
                () -> leaveService.updateLeaveRequest(99L, dto)
        );

        assertEquals(
                "Leave request not found with ID: 99",
                exception.getMessage()
        );

        verify(leaveRepository, never()).save(any(LeaveRequest.class));
    }

    @Test
    void shouldDeleteLeaveRequestSuccessfully() {
        LeaveRequest request = new LeaveRequest(
                1L,
                "EMP101",
                LeaveType.CASUAL,
                LocalDate.of(2026, 12, 20),
                LocalDate.of(2026, 12, 22),
                "Personal work",
                LeaveStatus.PENDING
        );

        when(leaveRepository.findById(1L))
                .thenReturn(Optional.of(request));

        leaveService.deleteLeaveRequest(1L);

        verify(leaveRepository).findById(1L);
        verify(leaveRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentLeaveRequest() {
        when(leaveRepository.findById(99L))
                .thenReturn(Optional.empty());

        LeaveNotFoundException exception = assertThrows(
                LeaveNotFoundException.class,
                () -> leaveService.deleteLeaveRequest(99L)
        );

        assertEquals(
                "Leave request not found with ID: 99",
                exception.getMessage()
        );

        verify(leaveRepository, never()).deleteById(99L);
    }

    @Test
    void shouldRejectUpdateWhenStartDateIsAfterEndDate() {
        LeaveRequest existingRequest = new LeaveRequest(
                1L,
                "EMP101",
                LeaveType.CASUAL,
                LocalDate.of(2026, 12, 20),
                LocalDate.of(2026, 12, 22),
                "Personal work",
                LeaveStatus.PENDING
        );

        LeaveRequestDto dto = new LeaveRequestDto();
        dto.setEmployeeId("EMP101");
        dto.setLeaveType(LeaveType.CASUAL);
        dto.setStartDate(LocalDate.of(2026, 12, 28));
        dto.setEndDate(LocalDate.of(2026, 12, 25));
        dto.setReason("Personal work");

        when(leaveRepository.findById(1L))
                .thenReturn(Optional.of(existingRequest));

        assertThrows(
                IllegalArgumentException.class,
                () -> leaveService.updateLeaveRequest(1L, dto)
        );

        verify(leaveRepository, never()).save(any(LeaveRequest.class));
    }
}