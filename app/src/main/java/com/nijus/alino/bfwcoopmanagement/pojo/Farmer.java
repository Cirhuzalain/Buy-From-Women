package com.nijus.alino.bfwcoopmanagement.pojo;


public class Farmer {

    private int farmerId;
    private String phone;
    private String name;
    private boolean isSync;

    public Farmer(String phone, String name, boolean isSync, int farmerId) {
        this.phone = phone;
        this.name = name;
        this.isSync = isSync;
        this.farmerId = farmerId;
    }

    public int getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(int farmerId) {
        this.farmerId = farmerId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSync() {
        return isSync;
    }

    public void setSync(boolean sync) {
        isSync = sync;
    }
}
