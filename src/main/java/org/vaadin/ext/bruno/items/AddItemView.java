package org.vaadin.ext.bruno.items;


import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.ext.bruno.MainLayout;

@Route(value = "add-item", layout = MainLayout.class)
@PageTitle(AddItemView.TITLE)
public class AddItemView extends Composite<VerticalLayout> {
    public static final String TITLE = "Add new Item";
    public AddItemView() {
        FormLayout form = new FormLayout();
        TextField name = new TextField();
        form.addFormItem(name, "Name");
        getContent().add(form);
        
        HorizontalLayout buttons = new HorizontalLayout();
        getContent().add(buttons);

        Button save = new Button("Save");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener((e) -> {
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
}
