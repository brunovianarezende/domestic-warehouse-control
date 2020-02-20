package org.vaadin.ext.bruno.frontend.buylists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import com.vaadin.flow.component.AbstractCompositeField;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.vaadin.ext.bruno.backend.Item;
import org.vaadin.ext.bruno.backend.ItemToBuy;

public class ItemsToBuyField extends AbstractCompositeField<FormLayout, ItemsToBuyField, Collection<ItemToBuy>> {

    private final LinkedHashMap<String, ItemToBuy> currentItems = new LinkedHashMap<>();

    public ItemsToBuyField(List<Item> availableItems) {
        super(Collections.emptyList());

        FormLayout grids = getContent();

        Grid<ItemToBuy> itemsToBuyGrid = new Grid<>();
        itemsToBuyGrid.addColumn(i -> i.getItem().getName()).setHeader("Item");
        itemsToBuyGrid.addColumn(new ComponentRenderer<>(itemToBuy -> {
            IntegerField numItems = new IntegerField();
            numItems.setStep(1);
            numItems.setHasControls(true);
            numItems.setMin(1);
            numItems.setValue(itemToBuy.getNumberOfItems());
            numItems.addValueChangeListener(e -> {
                int value = e.getValue() == null ? 0 : e.getValue();
                itemToBuy.setNumberOfItems(value);
            });
            return numItems;
        })).setHeader("Number of items to buy");
        itemsToBuyGrid.setItemDetailsRenderer(new ComponentRenderer<>(itemToBuy -> {
            TextArea notesField = new TextArea();
            notesField.setValue(itemToBuy.getNotes() == null ? "": itemToBuy.getNotes());
            notesField.setLabel("Notes");
            notesField.setWidthFull();
            notesField.addValueChangeListener(e -> itemToBuy.setNotes(e.getValue()));
            return notesField;
        }));
        itemsToBuyGrid.setItems(currentItems.values());
        itemsToBuyGrid.setWidth("100px");
        grids.add(itemsToBuyGrid);

        CallbackDataProvider.FetchCallback<Item, Collection<String>> itemCollectionFetchCallback = q -> availableItems
                .stream().filter(i -> !q.getFilter().orElse(Collections.emptySet()).contains(i.getId()));
        DataProvider<Item, Collection<String>> availableItemsDP = DataProvider.fromFilteringCallbacks(
                itemCollectionFetchCallback,
                q -> (int) itemCollectionFetchCallback.fetch(q).count()
        );
        ConfigurableFilterDataProvider<Item, Void, Collection<String>> configurableAvailableItemsDP = availableItemsDP
                .withConfigurableFilter();
        configurableAvailableItemsDP.setFilter(currentItems.keySet());

        Grid<Item> availableItemsGrid = new Grid<>();
        availableItemsGrid.addColumn(Item::getName).setHeader("Name");
        availableItemsGrid.addColumn(Item::getCurrentAmount).setHeader("We have");
        availableItemsGrid.addColumn(this::createShouldHaveColum).setHeader("We should have");

        availableItemsGrid.setDataProvider(configurableAvailableItemsDP);
        availableItemsGrid.setRowsDraggable(true);
        availableItemsGrid.setWidth("100px");
        availableItemsGrid.setClassNameGenerator(item -> {
            if (item.getWarningThreshold() == null && item.getMustBuyThreshold() == null) {
                return "";
            }
            if (item.getMustBuyThreshold() != null) {
                if (item.getCurrentAmount() <= item.getMustBuyThreshold()) {
                    return "must-buy";
                }
            }
            if (item.getWarningThreshold() != null) {
                if (item.getCurrentAmount() <= item.getWarningThreshold()) {
                    return "should-buy";
                }
            }
            return "";
        });
        grids.add(availableItemsGrid);

        List<Item> draggedItems = new ArrayList<>();

        availableItemsGrid.addDragStartListener((e) -> {
            draggedItems.addAll(e.getDraggedItems());
            itemsToBuyGrid.setDropMode(GridDropMode.BETWEEN);
        });

        availableItemsGrid.addDragEndListener((e) -> {
            itemsToBuyGrid.setDropMode(null);
            draggedItems.clear();
        });

        itemsToBuyGrid.addDropListener((e) -> {
            e.getSource().setDropMode(null);
            ItemToBuy newItem = new ItemToBuy();
            Item theItem = draggedItems.get(0);
            newItem.setItem(theItem);

            newItem.setNumberOfItems(getSuggestionForNumberOfItems(theItem));
            currentItems.put(newItem.getItem().getId(), newItem);
            setModelValue(new ArrayList<>(currentItems.values()), true);
            draggedItems.clear();
            itemsToBuyGrid.setDropMode(null);
            itemsToBuyGrid.setItems(currentItems.values());
            configurableAvailableItemsDP.setFilter(currentItems.keySet());
        });
    }

    private int getSuggestionForNumberOfItems(Item theItem) {
        if (theItem.getWarningThreshold() != null) {
            int weShouldHave = theItem.getWarningThreshold() + 1;
            int weHave = theItem.getCurrentAmount();
            if (weShouldHave > weHave) {
                return weShouldHave - weHave;
            } else {
                return 1;
            }
        }

        return 1;
    }

    private String createShouldHaveColum(Item item) {
        Integer warningThreshold = item.getWarningThreshold();
        Integer maxAmount = item.getMaxAmount();
        if (warningThreshold == null) {
            return "-";
        } else if (maxAmount == null) {
            return String.valueOf(warningThreshold + 1);
        } else {
            return String.format("%s - %s", warningThreshold + 1, maxAmount);
        }
    }

    @Override
    protected void setPresentationValue(Collection<ItemToBuy> itemsToBuy) {
        currentItems.clear();
        for (ItemToBuy itemToBuy : itemsToBuy) {
            currentItems.put(itemToBuy.getItem().getId(), itemToBuy);
        }
    }
}
