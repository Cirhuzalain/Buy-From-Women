package com.nijus.alino.bfwcoopmanagement.events;

public class ProcessingFarmerEvent {

    private String message;

    public ProcessingFarmerEvent(String message) {

        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
