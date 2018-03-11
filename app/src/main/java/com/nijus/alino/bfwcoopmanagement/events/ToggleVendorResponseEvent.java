package com.nijus.alino.bfwcoopmanagement.events;

public class ToggleVendorResponseEvent {

    private int count;

    public ToggleVendorResponseEvent(int count) {

        this.count = count;


    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
