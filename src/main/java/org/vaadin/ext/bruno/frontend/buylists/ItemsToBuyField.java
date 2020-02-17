package org.vaadin.ext.bruno.frontend.buylists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.vaadin.flow.component.AbstractCompositeField;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import org.vaadin.ext.bruno.backend.Item;
import org.vaadin.ext.bruno.backend.ItemToBuy;

public class ItemsToBuyField extends AbstractCompositeField<HorizontalLayout, ItemsToBuyField, Collection<ItemToBuy>> {

    private final LinkedHashMap<String, ItemToBuy> currentItems = new LinkedHashMap<>();

    public ItemsToBuyField(String label, List<Item> availableItems) {
        super(Collections.emptyList());
        //        getContent().add(new Label(label));

        HorizontalLayout grids = getContent();
        grids.setAlignItems(FlexComponent.Alignment.STRETCH);

        Grid<ItemToBuy> itemsToBuyGrid = new Grid<>(ItemToBuy.class);
        itemsToBuyGrid.setItems(currentItems.values());
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

        Grid<Item> availableItemsGrid = new Grid<>(Item.class);
        availableItemsGrid.setDataProvider(configurableAvailableItemsDP);
        availableItemsGrid.setRowsDraggable(true);
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
            Integer warningThreshold = theItem.getWarningThreshold();
            warningThreshold = warningThreshold == null ? 0 : warningThreshold;
            newItem.setNumberOfItems(warningThreshold + 1);
            currentItems.put(newItem.getItem().getId(), newItem);
            setModelValue(new ArrayList<>(currentItems.values()), true);
            draggedItems.clear();
            itemsToBuyGrid.setDropMode(null);
            itemsToBuyGrid.setItems(currentItems.values());
            configurableAvailableItemsDP.setFilter(currentItems.keySet());
        });
    }

    @Override
    protected void setPresentationValue(Collection<ItemToBuy> itemToBuys) {
        currentItems.clear();
        currentItems
                .putAll(itemToBuys.stream().collect(Collectors.toMap(i -> i.getItem().getId(), Function.identity())));
    }
}
