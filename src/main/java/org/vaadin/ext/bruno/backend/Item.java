package org.vaadin.ext.bruno.backend;

public class Item {
    private String name;
    private int currentQuantity;
    private int maxAmount;
    private int warningThreshold;
    private int mustBuyThreshold;
    private String notes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(int currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }

    public int getWarningThreshold() {
        return warningThreshold;
    }

    public void setWarningThreshold(int warningThreshold) {
        this.warningThreshold = warningThreshold;
    }

    public int getMustBuyThreshold() {
        return mustBuyThreshold;
    }

    public void setMustBuyThreshold(int mustBuyThreshold) {
        this.mustBuyThreshold = mustBuyThreshold;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
