package com.nijus.alino.bfwcoopmanagement.events;

public class ProcessingAgentEvent {

    private String message;

    public ProcessingAgentEvent(String message) {

        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
