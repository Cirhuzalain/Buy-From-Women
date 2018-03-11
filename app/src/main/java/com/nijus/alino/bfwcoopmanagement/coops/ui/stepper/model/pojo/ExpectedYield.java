package com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo;


public class ExpectedYield {

    private String harvestSeason;
    private double expectedCoopProductionInMt;
    private double totalCoopLandSize;
    private double expectedTotalProduction;

    public ExpectedYield() {

    }

    public ExpectedYield(String harvestSeason, double expectedCoopProductionInMt, double totalCoopLandSize, double expectedTotalProduction) {
        this.harvestSeason = harvestSeason;
        this.expectedCoopProductionInMt = expectedCoopProductionInMt;
        this.totalCoopLandSize = totalCoopLandSize;
        this.expectedTotalProduction = expectedTotalProduction;
    }

    public String getHarvestSeason() {
        return harvestSeason;
    }

    public void setHarvestSeason(String harvestSeason) {
        this.harvestSeason = harvestSeason;
    }

    public double getExpectedCoopProductionInMt() {
        return expectedCoopProductionInMt;
    }

    public void setExpectedCoopProductionInMt(double expectedCoopProductionInMt) {
        this.expectedCoopProductionInMt = expectedCoopProductionInMt;
    }

    public double getTotalCoopLandSize() {
        return totalCoopLandSize;
    }

    public void setTotalCoopLandSize(double totalCoopLandSize) {
        this.totalCoopLandSize = totalCoopLandSize;
    }

    public double getExpectedTotalProduction() {
        return expectedTotalProduction;
    }

    public void setExpectedTotalProduction(double expectedTotalProduction) {
        this.expectedTotalProduction = expectedTotalProduction;
    }
}
