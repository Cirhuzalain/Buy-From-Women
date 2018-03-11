package com.nijus.alino.bfwcoopmanagement.events;

public class ProcessingBuyerEvent {

    private String message;

    public ProcessingBuyerEvent(String message) {

        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
