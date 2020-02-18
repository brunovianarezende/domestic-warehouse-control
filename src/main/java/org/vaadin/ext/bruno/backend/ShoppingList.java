package org.vaadin.ext.bruno.backend;

import java.util.ArrayList;
import java.util.Collection;

public class ShoppingList {
    private String id;

    private String identifier;

    private Collection<ItemToBuy> itemsToBuy = new ArrayList<>();

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

    public Collection<ItemToBuy> getItemsToBuy() {
        return itemsToBuy;
    }

    public void setItemsToBuy(Collection<ItemToBuy> itemsToBuy) {
        this.itemsToBuy = itemsToBuy;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
