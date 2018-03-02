package com.nijus.alino.bfwcoopmanagement.events;

public class ToggleBuyerRequestEvent {

    private int position;

    public ToggleBuyerRequestEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
