package org.vaadin.ext.bruno.frontend.buylists;

import java.util.function.Consumer;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import org.vaadin.ext.bruno.backend.ShoppingList;
import org.vaadin.ext.bruno.backend.ShoppingListService;
import org.vaadin.ext.bruno.frontend.MainLayout;

@Route(value = "shopping-lists", layout = MainLayout.class)
public class ShoppingListsView extends Composite<VerticalLayout> {

    public static final String TITLE = "Shopping Lists";

    public ShoppingListsView() {
        ShoppingListService service = ShoppingListService.getInstance();

        HorizontalLayout gridHeader = new HorizontalLayout();
        H3 title = new H3("Shopping lists");
        title.getStyle().set("margin-top", "0px");
        gridHeader.add(title);
        Button addList = new Button("Add");
        addList.addClickListener((e) -> {
            UI.getCurrent().navigate(AddShoppingListView.class);
        });
        gridHeader.add(addList);
        gridHeader.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);
        getContent().add(gridHeader);

        // when we have some filtering, use ConfigurableFilterDataProvider
        DataProvider<ShoppingList, Void> provider = new CallbackDataProvider<>(q -> {
            // temp stub calls while I don't add proper paging support in service
            q.getLimit();
            q.getOffset();
            return service.getShoppingLists().stream();
        },
                q -> service.getCount());

        Grid<ShoppingList> itemGrid = createGrid(shoppingList -> {
            UI.getCurrent().navigate(EditShoppingListView.class, shoppingList.getId());
        }, shoppingList -> {
            Dialog dialog = new Dialog();
            VerticalLayout layout = new VerticalLayout();
            dialog.add(layout);

            Label label = new Label(
                    String.format(
                            "Are you sure you want to delete the '%s' shopping list? It won't be possible to recover it.",
                            shoppingList.getIdentifier()));
            layout.add(label);

            HorizontalLayout buttons = new HorizontalLayout();
            layout.add(buttons);

            Button confirm = new Button("Confirm");
            confirm.addClickListener(e -> {
                service.deleteShoppingList(shoppingList);
                provider.refreshAll();
                Notification.show("List Deleted", 1000, Notification.Position.MIDDLE);
                dialog.close();
            });
            buttons.add(confirm);

            Button cancel = new Button("Cancel");
            cancel.addClickListener(e -> dialog.close());
            cancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            buttons.add(cancel);

            dialog.open();
        });
        itemGrid.setDataProvider(provider);
        getContent().add(itemGrid);
    }

    private Grid<ShoppingList> createGrid(Consumer<ShoppingList> editEventListener,
            Consumer<ShoppingList> deleteEventListener) {
        Grid<ShoppingList> shoppingListGrid = new Grid<>();
        shoppingListGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        shoppingListGrid.addColumn(ShoppingList::getIdentifier).setHeader("Identifier");
        shoppingListGrid.addColumn(new ComponentRenderer<>(shoppingList -> {
            FlexLayout buttons = new FlexLayout();
            Button editButton = new Button();
            editButton.setIcon(new Icon(VaadinIcon.EDIT));
            editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            editButton.addClickListener(e -> editEventListener.accept(shoppingList));
            buttons.add(editButton);
            Button delButton = new Button();
            delButton.setIcon(new Icon(VaadinIcon.CLOSE));
            delButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            delButton.addClickListener(e -> deleteEventListener.accept(shoppingList));
            buttons.add(delButton);
            return buttons;
        })).setFlexGrow(0);
        shoppingListGrid.setItemDetailsRenderer(new ComponentRenderer<>(shoppingList -> {
            HorizontalLayout result = new HorizontalLayout();
            Label label = new Label("Notes:");
            label.getStyle().set("font-weight", "bold");
            label.getStyle().set("margin-right", "5px");
            result.add(label);
            result.add(new Text(shoppingList.getNotes()));
            return result;
        }));
        return shoppingListGrid;
    }
}

