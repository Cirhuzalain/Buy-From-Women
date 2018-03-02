package com.nijus.alino.bfwcoopmanagement.events;

public class ToggleAgentResponseEvent {

    private int count;

    public ToggleAgentResponseEvent(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
