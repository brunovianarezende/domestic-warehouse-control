package org.vaadin.ext.bruno.frontend.buylists;

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
import org.vaadin.ext.bruno.backend.ShoppingList;
import org.vaadin.ext.bruno.backend.ShoppingListService;
import org.vaadin.ext.bruno.frontend.MainLayout;
import org.vaadin.ext.bruno.backend.Item;
import org.vaadin.ext.bruno.backend.ItemService;
import org.vaadin.ext.bruno.frontend.items.ItemForm;

@Route(value = "add-shopping-list", layout = MainLayout.class)
@PageTitle(AddShoppingListView.TITLE)
public class AddShoppingListView extends Composite<VerticalLayout> {

    public static final String TITLE = "Add new Shopping List";

    public AddShoppingListView() {
        ShoppingListService service = ShoppingListService.getInstance();

        getContent().add(new H3("New List"));
        ShoppingListForm form = new ShoppingListForm();
        getContent().add(form);

        HorizontalLayout buttons = new HorizontalLayout();
        getContent().add(buttons);
        Button save = new Button("Save");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener((e) -> {
            ShoppingList newList = new ShoppingList();
            try {
                form.getBinder().writeBean(newList);
                Notification.show("New List Saved", 1000, Notification.Position.MIDDLE);
                service.addShoppingList(newList);
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
