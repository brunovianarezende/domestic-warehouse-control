package org.vaadin.ext.bruno.frontend.buylists;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import org.vaadin.ext.bruno.backend.ItemService;
import org.vaadin.ext.bruno.backend.ShoppingList;
import org.vaadin.ext.bruno.frontend.NotBlankValidator;

public class ShoppingListForm extends Composite<FormLayout> {

    private Binder<ShoppingList> binder = new Binder<>();

    public ShoppingListForm() {
        ItemService itemService = ItemService.getInstance();
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

        ItemsToBuyField itemsToBuyField = new ItemsToBuyField(itemService.getItems());
        form.add(itemsToBuyField);
        form.setColspan(itemsToBuyField, 2);
        binder.forField(itemsToBuyField)
            .bind(ShoppingList::getItemsToBuy, ShoppingList::setItemsToBuy);
    }

    public Binder<ShoppingList> getBinder() {
        return binder;
    }
}
