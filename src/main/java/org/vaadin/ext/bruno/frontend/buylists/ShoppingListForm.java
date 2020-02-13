package org.vaadin.ext.bruno.frontend.buylists;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import org.vaadin.ext.bruno.backend.ShoppingList;
import org.vaadin.ext.bruno.frontend.NotBlankValidator;

public class ShoppingListForm extends Composite<FormLayout> {
    private Binder<ShoppingList> binder = new Binder<>();

    public ShoppingListForm() {
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
    }

    public Binder<ShoppingList> getBinder() {
        return binder;
    }
}
