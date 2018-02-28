package com.nijus.alino.bfwcoopmanagement.events;

public class DeleteFarmerEvent {

    private String message;
    private boolean isSuccess;

    public DeleteFarmerEvent(String message, boolean isSuccess) {
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
