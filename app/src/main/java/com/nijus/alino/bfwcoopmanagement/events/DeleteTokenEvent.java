package com.nijus.alino.bfwcoopmanagement.events;


public class DeleteTokenEvent {

    private boolean isTokenDeleted;

    public DeleteTokenEvent(boolean isTokenDeleted) {
        this.isTokenDeleted = isTokenDeleted;
    }

    public boolean isTokenDeleted() {
        return isTokenDeleted;
    }
}
