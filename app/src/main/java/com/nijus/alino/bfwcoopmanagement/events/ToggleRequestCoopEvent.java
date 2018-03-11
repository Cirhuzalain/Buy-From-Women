package com.nijus.alino.bfwcoopmanagement.events;

public class ToggleRequestCoopEvent {

    private int position;

    public ToggleRequestCoopEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
