package org.vaadin.ext.bruno.items;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.ext.bruno.MainLayout;
import org.vaadin.ext.bruno.backend.Item;
import org.vaadin.ext.bruno.backend.ItemService;

@Route(value = "add-item", layout = MainLayout.class)
@PageTitle(AddItemView.TITLE)
public class AddItemView extends Composite<VerticalLayout> {

    public static final String TITLE = "Add new Item";

    public AddItemView() {
        ItemService service = ItemService.getInstance();

        getContent().add(new H3("New Item"));
        FormLayout form = new FormLayout();
        getContent().add(form);

        Binder<Item> binder = new Binder<>();

        TextField name = new TextField();
        name.setRequired(true);
        // TODO: add a required class to name label, given setRequiredIndicatorVisible has no effect in a form item
        //        name.setRequiredIndicatorVisible(true);
        form.addFormItem(name, "Name");
        binder.forField(name)
                .withValidator((b, v) ->  {
                    if (StringUtils.isBlank(b)) {
                        return ValidationResult.error("Name must not be empty");
                    }
                    else {
                        return ValidationResult.ok();
                    }
                })
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

        HorizontalLayout buttons = new HorizontalLayout();
        getContent().add(buttons);
        Button save = new Button("Save");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener((e) -> {
            Item newItem = new Item();
            try {
                binder.writeBean(newItem);
                Notification.show("New Item Saved", 1000, Notification.Position.MIDDLE);
                service.addItem(newItem);
                goBack();
            } catch (ValidationException ex) {
            }
        });
        buttons.add(save);

        Button cancel = new Button("Cancel");
        cancel.addClickListener((e) -> {
            goBack();
        });
        buttons.add(cancel);
    }

    private void goBack() {
        UI current = UI.getCurrent();
        current.getPage().getHistory().back();
    }
}
