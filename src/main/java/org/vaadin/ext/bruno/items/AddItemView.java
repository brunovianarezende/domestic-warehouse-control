package org.vaadin.ext.bruno.items;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
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
        ItemForm form = new ItemForm();
        getContent().add(form);

        HorizontalLayout buttons = new HorizontalLayout();
        getContent().add(buttons);
        Button save = new Button("Save");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener((e) -> {
            Item newItem = new Item();
            try {
                form.getBinder().writeBean(newItem);
                Notification.show("New Item Saved", 1000, Notification.Position.MIDDLE);
                service.addItem(newItem);
                goBack();
            } catch (ValidationException ignored) {
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
