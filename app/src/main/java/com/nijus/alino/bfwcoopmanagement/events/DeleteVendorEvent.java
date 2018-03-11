package com.nijus.alino.bfwcoopmanagement.events;

public class DeleteVendorEvent {

    private String message;
    private boolean isSuccess;

    public DeleteVendorEvent(String message, boolean isSuccess) {
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
