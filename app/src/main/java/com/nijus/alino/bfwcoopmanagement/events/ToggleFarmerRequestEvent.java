package com.nijus.alino.bfwcoopmanagement.events;

public class ToggleFarmerRequestEvent {

    private int position;

    public ToggleFarmerRequestEvent(int position) {

        this.position = position;

    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
