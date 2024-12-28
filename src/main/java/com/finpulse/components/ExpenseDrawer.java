package com.finpulse.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.dom.Style;

public class ExpenseDrawer extends Dialog {

    public ExpenseDrawer() {
        // Set dialog to fullscreen style
        addClassName("fullscreen-drawer");
        setModal(true);
        setDraggable(false);
        setCloseOnOutsideClick(false);

        // Main layout
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.setWidthFull();
        layout.getStyle().set("overflow", "auto");

        // Title
        H2 title = new H2("Add Expense");
        layout.add(title);

        // Form layout
        FormLayout formLayout = new FormLayout();
        TextField descriptionField = new TextField("Description");
        NumberField amountField = new NumberField("Amount");
        formLayout.add(descriptionField, amountField);

        layout.add(formLayout);

        // Button layout
        HorizontalLayout buttonLayout = new HorizontalLayout();
        Button submitButton = new Button("Submit", event -> close());
        Button cancelButton = new Button("Cancel", event -> close());
        buttonLayout.add(submitButton, cancelButton);

        layout.add(buttonLayout);
        add(layout);
    }
}