package com.nijus.alino.bfwcoopmanagement.events;

import java.util.ArrayList;

public class ResponseEventFarmerToDelete {

    ArrayList<Integer> farmerIds;

    public ResponseEventFarmerToDelete(ArrayList<Integer> farmerIds) {
        this.farmerIds = farmerIds;
    }

    public ArrayList<Integer> getFarmerIds() {
        return farmerIds;
    }

    public void setFarmerIds(ArrayList<Integer> farmerIds) {
        this.farmerIds = farmerIds;
    }
}
