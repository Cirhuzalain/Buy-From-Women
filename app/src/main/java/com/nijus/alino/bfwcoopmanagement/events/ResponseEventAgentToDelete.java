package com.nijus.alino.bfwcoopmanagement.events;

import java.util.ArrayList;

public class ResponseEventAgentToDelete {

    ArrayList<Integer> agentIds;

    public ResponseEventAgentToDelete(ArrayList<Integer> agentIds) {
        this.agentIds = agentIds;
    }

    public ArrayList<Integer> getAgentIds() {
        return agentIds;
    }

    public void setAgentIds(ArrayList<Integer> agentIds) {
        this.agentIds = agentIds;
    }
}
