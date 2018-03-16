package com.nijus.alino.bfwcoopmanagement.events;

import java.util.ArrayList;

public class ResponseEventCoopToDelete {

    ArrayList<Integer> farmerIds;

    public ResponseEventCoopToDelete(ArrayList<Integer> farmerIds) {
        this.farmerIds = farmerIds;
    }

    public ArrayList<Integer> getFarmerIds() {
        return farmerIds;
    }

    public void setFarmerIds(ArrayList<Integer> farmerIds) {
        this.farmerIds = farmerIds;
    }
}
