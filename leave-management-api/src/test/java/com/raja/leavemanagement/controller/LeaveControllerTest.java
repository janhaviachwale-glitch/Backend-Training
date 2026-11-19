package com.raja.leavemanagement.controller;

import com.raja.leavemanagement.dto.LeaveRequestDto;
import com.raja.leavemanagement.dto.LeaveResponseDto;
import com.raja.leavemanagement.exception.GlobalExceptionHandler;
import com.raja.leavemanagement.exception.LeaveNotFoundException;
import com.raja.leavemanagement.model.LeaveStatus;
import com.raja.leavemanagement.model.LeaveType;
import com.raja.leavemanagement.service.LeaveService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LeaveController.class)
@Import(GlobalExceptionHandler.class)
class LeaveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LeaveService leaveService;

    // GET /leaves
    @Test
    void shouldReturnAllLeaveRequests() throws Exception {

        LeaveResponseDto response = createResponse();

        when(leaveService.getAllLeaveRequests())
                .thenReturn(List.of(response));

        mockMvc.perform(get("/leaves"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].employeeId").value("EMP101"))
                .andExpect(jsonPath("$[0].leaveType").value("CASUAL"))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    // GET /leaves/{id}
    @Test
    void shouldReturnLeaveRequestById() throws Exception {

        when(leaveService.getLeaveRequestById(1L))
                .thenReturn(createResponse());

        mockMvc.perform(get("/leaves/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.employeeId").value("EMP101"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    // GET /leaves/{id} - Not found
    @Test
    void shouldReturnNotFoundWhenLeaveRequestDoesNotExist() throws Exception {

        when(leaveService.getLeaveRequestById(99L))
                .thenThrow(new LeaveNotFoundException(
                        "Leave request not found with ID: 99"
                ));

        mockMvc.perform(get("/leaves/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(
                        "Leave request not found with ID: 99"
                ));
    }

    // POST /leaves
    @Test
    void shouldCreateLeaveRequest() throws Exception {

        when(leaveService.createLeaveRequest(any(LeaveRequestDto.class)))
                .thenReturn(createResponse());

        String requestBody = """
                {
                    "employeeId": "EMP101",
                    "leaveType": "CASUAL",
                    "startDate": "2026-12-20",
                    "endDate": "2026-12-22",
                    "reason": "Personal work"
                }
                """;

        mockMvc.perform(post("/leaves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.employeeId").value("EMP101"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    // POST /leaves - Validation error
    @Test
    void shouldRejectInvalidLeaveRequest() throws Exception {

        String requestBody = """
                {
                    "employeeId": "",
                    "leaveType": "CASUAL",
                    "startDate": "2026-12-20",
                    "endDate": "2026-12-22",
                    "reason": "Personal work"
                }
                """;

        mockMvc.perform(post("/leaves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

    }

    // PUT /leaves/{id}
    @Test
    void shouldUpdateLeaveRequest() throws Exception {

        LeaveResponseDto updatedResponse = new LeaveResponseDto(
                1L,
                "EMP102",
                LeaveType.SICK,
                LocalDate.of(2026, 12, 25),
                LocalDate.of(2026, 12, 27),
                "Medical appointment",
                LeaveStatus.PENDING
        );

        when(leaveService.updateLeaveRequest(
                org.mockito.ArgumentMatchers.eq(1L),
                any(LeaveRequestDto.class)
        )).thenReturn(updatedResponse);

        String requestBody = """
                {
                    "employeeId": "EMP102",
                    "leaveType": "SICK",
                    "startDate": "2026-12-25",
                    "endDate": "2026-12-27",
                    "reason": "Medical appointment"
                }
                """;

        mockMvc.perform(put("/leaves/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.employeeId").value("EMP102"))
                .andExpect(jsonPath("$.leaveType").value("SICK"));
    }

    // DELETE /leaves/{id}
    @Test
    void shouldDeleteLeaveRequest() throws Exception {

        doNothing().when(leaveService).deleteLeaveRequest(1L);

        mockMvc.perform(delete("/leaves/1"))
                .andExpect(status().isNoContent());

        verify(leaveService).deleteLeaveRequest(1L);
    }

    // DELETE /leaves/{id} - Not found
    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentRequest()
            throws Exception {

        org.mockito.Mockito.doThrow(new LeaveNotFoundException(
                "Leave request not found with ID: 99"
        )).when(leaveService).deleteLeaveRequest(99L);

        mockMvc.perform(delete("/leaves/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(
                        "Leave request not found with ID: 99"
                ));
    }

    // Reusable response data
    private LeaveResponseDto createResponse() {

        return new LeaveResponseDto(
                1L,
                "EMP101",
                LeaveType.CASUAL,
                LocalDate.of(2026, 12, 20),
                LocalDate.of(2026, 12, 22),
                "Personal work",
                LeaveStatus.PENDING
        );
    }
}