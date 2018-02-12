package com.nijus.alino.bfwcoopmanagement.events;

/**
 * Check Data validation event for general info page (Receiver)
 */
public class DataValidEventR {

    private boolean isDataValid;

    public DataValidEventR(boolean isDataValid) {
        this.isDataValid = isDataValid;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}
