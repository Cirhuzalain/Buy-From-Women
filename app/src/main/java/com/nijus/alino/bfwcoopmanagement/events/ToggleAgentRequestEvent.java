package com.nijus.alino.bfwcoopmanagement.events;

public class ToggleAgentRequestEvent {

    private int position;

    public ToggleAgentRequestEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
