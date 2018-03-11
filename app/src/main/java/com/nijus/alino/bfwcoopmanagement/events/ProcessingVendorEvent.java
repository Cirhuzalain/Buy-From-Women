package com.nijus.alino.bfwcoopmanagement.events;

public class ProcessingVendorEvent {

    private String message;

    public ProcessingVendorEvent(String message) {

        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
