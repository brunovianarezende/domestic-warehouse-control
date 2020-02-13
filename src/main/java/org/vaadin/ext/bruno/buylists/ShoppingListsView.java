package org.vaadin.ext.bruno.buylists;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.vaadin.ext.bruno.MainLayout;

@Route(value = "shopping-lists", layout = MainLayout.class)
public class ShoppingListsView extends Composite<VerticalLayout> {
    public static final String TITLE = "Shopping Lists";

    public ShoppingListsView() {
        getContent().add(new Text("Shopping lists"));
    }
}
