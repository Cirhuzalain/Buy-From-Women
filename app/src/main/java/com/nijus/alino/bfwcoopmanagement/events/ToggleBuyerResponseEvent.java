package com.nijus.alino.bfwcoopmanagement.events;

public class ToggleBuyerResponseEvent {

    private int count;

    public ToggleBuyerResponseEvent(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
