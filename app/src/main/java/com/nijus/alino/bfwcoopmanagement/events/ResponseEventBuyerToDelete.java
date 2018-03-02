package com.nijus.alino.bfwcoopmanagement.events;

import java.util.ArrayList;

public class ResponseEventBuyerToDelete {

    ArrayList<Integer> buyerIds;

    public ResponseEventBuyerToDelete(ArrayList<Integer> buyerIds) {
        this.buyerIds = buyerIds;
    }

    public ArrayList<Integer> getBuyerIds() {
        return buyerIds;
    }

    public void setBuyerIds(ArrayList<Integer> buyerIds) {
        this.buyerIds = buyerIds;
    }
}
