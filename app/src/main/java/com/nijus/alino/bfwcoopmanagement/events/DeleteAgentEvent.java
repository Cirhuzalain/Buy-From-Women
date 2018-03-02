package com.nijus.alino.bfwcoopmanagement.events;

public class DeleteAgentEvent {

    private String message;
    private boolean isSuccess;

    public DeleteAgentEvent(String message, boolean isSuccess) {
        this.isSuccess = isSuccess;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
