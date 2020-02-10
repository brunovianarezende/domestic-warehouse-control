package org.vaadin.ext.bruno.buylists;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.vaadin.ext.bruno.MainLayout;

@Route(value = "buylists", layout = MainLayout.class)
public class BuyListsView extends Composite<VerticalLayout> {
    public static final String TITLE = "Buy Lists";

    public BuyListsView() {
        getContent().add(new Text("Buy lists"));
    }
}
