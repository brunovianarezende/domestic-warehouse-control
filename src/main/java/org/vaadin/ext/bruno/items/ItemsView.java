package org.vaadin.ext.bruno.items;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import org.vaadin.ext.bruno.MainLayout;

@Route(value = "", layout = MainLayout.class)
@PageTitle(ItemsView.TITLE)
public class ItemsView extends Composite<VerticalLayout> {
    public static final String TITLE = "Items";

    public ItemsView() {
        Button addItemButton = new Button("Add Item");
        addItemButton.addClickListener((e) -> {
            UI.getCurrent().navigate(AddItemView.class);
        });
        getContent().add(addItemButton);
    }
}
