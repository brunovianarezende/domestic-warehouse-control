package org.vaadin.ext.bruno.frontend.buylists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.DataProvider;
import org.vaadin.ext.bruno.backend.Item;
import org.vaadin.ext.bruno.backend.ItemService;
import org.vaadin.ext.bruno.backend.ItemToBuy;
import org.vaadin.ext.bruno.backend.ShoppingList;
import org.vaadin.ext.bruno.frontend.NotBlankValidator;

public class ShoppingListForm extends Composite<FormLayout> {

    private Binder<ShoppingList> binder = new Binder<>();

    public ShoppingListForm() {
        ItemService itemService = ItemService.getInstance();
        binder = new Binder<>();
        FormLayout form = getContent();

        TextField identifier = new TextField();
        identifier.setRequired(true);
        form.addFormItem(identifier, "Identifier");
        binder.forField(identifier)
                .withValidator(new NotBlankValidator("Identifier must not be empty"))
                .bind(ShoppingList::getIdentifier, ShoppingList::setIdentifier);

        TextArea notes = new TextArea();
        notes.setMaxLength(1000);
        notes.setWidthFull();
        form.addFormItem(notes, "Notes");
        binder.bind(notes, ShoppingList::getNotes, ShoppingList::setNotes);

        HorizontalLayout grids = new HorizontalLayout();
        form.add(grids);

        List<ItemToBuy> currentItems = new ArrayList<>();

        Grid<ItemToBuy> itemsToBuyGrid = new Grid<>(ItemToBuy.class);
        itemsToBuyGrid.setItems(currentItems);
        grids.add(itemsToBuyGrid);

        Grid<Item> availableItemsGrid = new Grid<>(Item.class);
        availableItemsGrid.setItems(itemService.getItems());
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
            currentItems.add(newItem);
            draggedItems.clear();
            itemsToBuyGrid.setDropMode(null);
            itemsToBuyGrid.setItems(currentItems);
        });

    }

    public Binder<ShoppingList> getBinder() {
        return binder;
    }
}
