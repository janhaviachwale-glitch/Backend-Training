package com.raja.leavemanagement.exception;

public class LeaveNotFoundException extends RuntimeException {

    public LeaveNotFoundException(String message) {
        super(message);
    }
}