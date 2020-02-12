package org.vaadin.ext.bruno.items;

import java.util.Optional;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.ext.bruno.MainLayout;
import org.vaadin.ext.bruno.backend.Item;
import org.vaadin.ext.bruno.backend.ItemService;

@Route(value = "edit-item", layout = MainLayout.class)
@PageTitle(EditItemView.TITLE)
public class EditItemView extends Composite<VerticalLayout> implements HasUrlParameter<String> {

    public static final String TITLE = "Edit Item";

    private final ItemForm form = new ItemForm();

    private Item item = null;

    private ItemService service;

    public EditItemView() {
        service = ItemService.getInstance();
        getContent().add(new H3("Edit Item"));

        getContent().add(form);

        HorizontalLayout buttons = new HorizontalLayout();
        getContent().add(buttons);
        Button save = new Button("Save");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener((e) -> {
            if (item != null) {
                try {
                    form.getBinder().writeBean(item);
                    service.saveItem(item);
                } catch (ValidationException ignored) {
                    return;
                }
            }
            goBack();
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

    @Override
    public void setParameter(BeforeEvent beforeEvent, String itemId) {
        Optional<Item> optionalItem = service.getItem(itemId);
        if (optionalItem.isPresent()) {
            this.item = optionalItem.get();
            form.getBinder().readBean(this.item);
        } else {
            Notification notification = new Notification();
            notification.setText("Item not found");
            notification.setDuration(2000);
            notification.setPosition(Notification.Position.MIDDLE);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.open();
            goBack();
        }
    }
}
