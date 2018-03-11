package com.nijus.alino.bfwcoopmanagement.events;

import java.util.ArrayList;

public class ResponseEventVendorToDelete {

    ArrayList<Integer> vendorIds;

    public ResponseEventVendorToDelete(ArrayList<Integer> vendorIds) {
        this.vendorIds = vendorIds;
    }

    public ArrayList<Integer> getVendorIds() {
        return vendorIds;
    }

    public void setVendorIds(ArrayList<Integer> vendorIds) {
        this.vendorIds = vendorIds;
    }
}
