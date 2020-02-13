package org.vaadin.ext.bruno.backend;

import java.util.ArrayList;
import java.util.List;

public class ShoppingList {
    private String id;

    private String identifier;

    private List<ItemToBuy> itemsToBuy = new ArrayList<>();

    private String notes = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public List<ItemToBuy> getItemsToBuy() {
        return itemsToBuy;
    }

    public void setItemsToBuy(List<ItemToBuy> itemsToBuy) {
        this.itemsToBuy = itemsToBuy;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
