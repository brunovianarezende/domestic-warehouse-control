package org.vaadin.ext.bruno.frontend.buylists;

import java.util.Optional;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.ext.bruno.backend.ShoppingList;
import org.vaadin.ext.bruno.backend.ShoppingListService;
import org.vaadin.ext.bruno.frontend.MainLayout;

@Route(value = "edit-shopping-list", layout = MainLayout.class)
@PageTitle(EditShoppingListView.TITLE)
public class EditShoppingListView extends Composite<VerticalLayout> implements HasUrlParameter<String> {

    public static final String TITLE = "Edit Shopping List";

    private final ShoppingListForm form = new ShoppingListForm();

    private ShoppingList shoppingList = null;

    private ShoppingListService service;

    public EditShoppingListView() {
        service = ShoppingListService.getInstance();
        getContent().add(new H3("Edit Shopping List"));

        getContent().add(form);

        HorizontalLayout buttons = new HorizontalLayout();
        getContent().add(buttons);
        Button save = new Button("Save");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener((e) -> {
            if (shoppingList != null) {
                try {
                    form.getBinder().writeBean(shoppingList);
                    service.saveShoppingList(shoppingList);
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
        Optional<ShoppingList> optionalShoppingList = service.getShoppingList(itemId);
        if (optionalShoppingList.isPresent()) {
            this.shoppingList = optionalShoppingList.get();
            form.getBinder().readBean(this.shoppingList);
        } else {
            Notification notification = new Notification();
            notification.setText("Shopping List not found");
            notification.setDuration(2000);
            notification.setPosition(Notification.Position.MIDDLE);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.open();
            goBack();
        }
    }
}
