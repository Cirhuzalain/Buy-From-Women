package com.nijus.alino.bfwcoopmanagement.events;

public class ToggleCoopResponseEvent {

    private int count;

    public ToggleCoopResponseEvent(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
