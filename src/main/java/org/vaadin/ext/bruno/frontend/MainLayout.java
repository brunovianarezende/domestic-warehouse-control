package org.vaadin.ext.bruno.frontend;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import org.vaadin.ext.bruno.frontend.buylists.ShoppingListsView;
import org.vaadin.ext.bruno.frontend.items.ItemsView;

@PWA(name = "Domestic Warehouse Control", shortName = "DWC")
@CssImport("styles/shared-styles.css")
@CssImport(value = "styles/vaadin-grid-styles.css", themeFor = "vaadin-grid")
public class MainLayout extends AppLayout {
    public MainLayout() {
        addToNavbar(new DrawerToggle());
        addToNavbar(new H2("Domestic Warehouse Control"));

        VerticalLayout menuBar = new VerticalLayout();
        menuBar.setId("menuBar");
        menuBar.add(new RouterLink(ItemsView.TITLE, ItemsView.class));
        menuBar.add(new RouterLink(ShoppingListsView.TITLE, ShoppingListsView.class));
        addToDrawer(menuBar);
    }
}
