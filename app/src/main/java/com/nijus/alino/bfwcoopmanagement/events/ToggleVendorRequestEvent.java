package com.nijus.alino.bfwcoopmanagement.events;

public class ToggleVendorRequestEvent {

    private int position;

    public ToggleVendorRequestEvent(int position) {

        this.position = position;

    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
