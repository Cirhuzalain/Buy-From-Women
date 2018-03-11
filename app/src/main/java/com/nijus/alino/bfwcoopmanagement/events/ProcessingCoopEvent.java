package com.nijus.alino.bfwcoopmanagement.events;


public class ProcessingCoopEvent {

    private String message;

    public ProcessingCoopEvent(String message) {

        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
