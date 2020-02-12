package org.vaadin.ext.bruno.backend;

public class Item {
    private String id;

    private String name;

    private Integer maxAmount;

    private Integer warningThreshold;

    private Integer mustBuyThreshold;

    private String notes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Integer maxAmount) {
        this.maxAmount = maxAmount;
    }

    public Integer getWarningThreshold() {
        return warningThreshold;
    }

    public void setWarningThreshold(Integer warningThreshold) {
        this.warningThreshold = warningThreshold;
    }

    public Integer getMustBuyThreshold() {
        return mustBuyThreshold;
    }

    public void setMustBuyThreshold(Integer mustBuyThreshold) {
        this.mustBuyThreshold = mustBuyThreshold;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
