package com.nijus.alino.bfwcoopmanagement.events;

public class ToggleFarmerResponseEvent {

    private int count;

    public ToggleFarmerResponseEvent(int count) {

        this.count = count;


    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
