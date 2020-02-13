package org.vaadin.ext.bruno.frontend.buylists;

import java.util.function.Consumer;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
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

        Grid<ShoppingList> itemGrid = createGrid(shoppingList -> {
            UI.getCurrent().navigate(EditShoppingListView.class, shoppingList.getId());
        });
        DataProvider<ShoppingList, Void> provider = new CallbackDataProvider<>(q -> {
            // temp stub calls while I don't add proper paging support in service
            q.getLimit();
            q.getOffset();
            return service.getShoppingLists().stream();
        },
                q -> service.getCount());
        // when we have some filtering, use ConfigurableFilterDataProvider
        itemGrid.setDataProvider(provider);
        getContent().add(itemGrid);
    }

    private Grid<ShoppingList> createGrid(Consumer<ShoppingList> editEventListener) {
        Grid<ShoppingList> shoppingListGrid = new Grid<>();
        shoppingListGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        shoppingListGrid.addColumn(ShoppingList::getIdentifier).setHeader("Identifier");
        shoppingListGrid.addColumn(new ComponentRenderer<>(shoppingList -> {
            Button button = new Button();
            button.setIcon(new Icon(VaadinIcon.EDIT));
            button.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            button.addClickListener(e -> editEventListener.accept(shoppingList));
            return button;
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

