package org.vaadin.ext.bruno.frontend.items;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.validator.RegexpValidator;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.ext.bruno.backend.Item;
import org.vaadin.ext.bruno.frontend.NotBlankValidator;

public class ItemForm extends Composite<FormLayout> {
    private Binder<Item> binder = new Binder<>();

    public ItemForm() {
        binder = new Binder<>();
        FormLayout form = getContent();

        TextField name = new TextField();
        name.setRequired(true);
        // TODO: add a required class to name label, given setRequiredIndicatorVisible has no effect in a form item
        //        name.setRequiredIndicatorVisible(true);
        form.addFormItem(name, "Name");
        binder.forField(name)
                .withValidator(new NotBlankValidator("Name must not be empty"))
                .bind(Item::getName, Item::setName);

        IntegerField maxAmount = new IntegerField();
        maxAmount.setTitle("The maximum number of such items.");
        maxAmount.setStep(1);
        maxAmount.setHasControls(true);
        maxAmount.setMin(0);
        form.addFormItem(maxAmount, "Maximum amount");
        binder.bind(maxAmount, Item::getMaxAmount, Item::setMaxAmount);

        IntegerField warningThreshold = new IntegerField();
        warningThreshold.setTitle(
                "Show a warning when the number of items we have is equal or less than this value. Leave empty for no warning.");
        warningThreshold.setStep(1);
        warningThreshold.setHasControls(true);
        warningThreshold.setMin(0);
        form.addFormItem(warningThreshold, "Warning threshold");
        binder.bind(warningThreshold, Item::getWarningThreshold, Item::setWarningThreshold);

        IntegerField mustByThreshold = new IntegerField();
        mustByThreshold.setTitle(
                "Show a critical warning when the number of items we have is equal or less than this value. Leave empty for no warning.");
        mustByThreshold.setStep(1);
        mustByThreshold.setHasControls(true);
        mustByThreshold.setMin(0);
        form.addFormItem(mustByThreshold, "Must buy threshold");
        binder.bind(mustByThreshold, Item::getMustBuyThreshold, Item::setMustBuyThreshold);

        TextArea notes = new TextArea();
        notes.setMaxLength(1000);
        notes.setWidthFull();
        form.addFormItem(notes, "Notes");
        binder.bind(notes, Item::getNotes, Item::setNotes);
    }

    public Binder<Item> getBinder() {
        return binder;
    }
}
