package org.vaadin.ext.bruno.items;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.ext.bruno.MainLayout;
import org.vaadin.ext.bruno.backend.Item;
import org.vaadin.ext.bruno.backend.ItemService;

@Route(value = "", layout = MainLayout.class)
@PageTitle(ItemsView.TITLE)
public class ItemsView extends Composite<VerticalLayout> {

    public static final String TITLE = "Items";

    public ItemsView() {
        ItemService service = ItemService.getInstance();
        Button addItemButton = new Button("Add Item");
        addItemButton.addClickListener((e) -> {
            UI.getCurrent().navigate(AddItemView.class);
        });
        getContent().add(addItemButton);

        Grid<Item> itemGrid = createItemGrid();
        DataProvider<Item, Void> provider = new CallbackDataProvider<>(q -> {
            // temp stub calls while I don't add proper paging support in service
            q.getLimit();
            q.getOffset();
            return service.getItems().stream();
        },
                q -> service.getCount());
        // when we have some filtering, use ConfigurableFilterDataProvider
        itemGrid.setDataProvider(provider);
        getContent().add(itemGrid);
    }

    private Grid<Item> createItemGrid() {
        Grid<Item> itemGrid = new Grid<>();
        itemGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        itemGrid.addColumn(Item::getName).setHeader("Name");
        itemGrid.addColumn(Item::getMaxAmount).setHeader("Max amount");
        itemGrid.addColumn(Item::getWarningThreshold).setHeader("Warning Threshold");
        itemGrid.addColumn(Item::getMustBuyThreshold).setHeader("Must Buy Threshold");
        itemGrid.setItemDetailsRenderer(new ComponentRenderer<>(item -> {
            HorizontalLayout result = new HorizontalLayout();
            Label label = new Label("Notes:");
            label.getStyle().set("font-weight", "bold");
            label.getStyle().set("margin-right", "5px");
            result.add(label);
            result.add(new Text(item.getNotes()));
            return result;
        }));
        return itemGrid;
    }
}
