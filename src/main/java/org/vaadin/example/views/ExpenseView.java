package org.vaadin.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.service.AuthenticationService;
import org.vaadin.example.model.User;
import org.vaadin.example.model.SpendingCategory;
import org.vaadin.example.model.Expense;
import org.vaadin.example.service.ExpenseService;

import java.time.LocalDate;
import java.util.Set;

@Route(value = "expense", layout = MainLayout.class)
@PermitAll
public class ExpenseView extends VerticalLayout {

    private final AuthenticationService authenticationService;
    private final ExpenseService expenseService;

    private final TextField amountField;
    private final ComboBox<SpendingCategory> categoryComboBox;
    private final DatePicker datePicker;
    private final TextField descriptionField;

    @Autowired
    public ExpenseView(AuthenticationService authenticationService, ExpenseService expenseService) {
        this.authenticationService = authenticationService;
        this.expenseService = expenseService;

        User currentUser = authenticationService.getCurrentUser();
        Set<SpendingCategory> spendingCategories = currentUser.getSpendingCategories();

        amountField = new TextField("Amount");
        categoryComboBox = new ComboBox<>("Category");
        categoryComboBox.setItems(spendingCategories);
        datePicker = new DatePicker("Date");
        descriptionField = new TextField("Description");

        Button saveButton = new Button("Save Expense", event -> saveExpense());

        add(amountField, categoryComboBox, datePicker, descriptionField, saveButton);
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }

    private void saveExpense() {
        User currentUser = authenticationService.getCurrentUser();

        double amount = Double.parseDouble(amountField.getValue());
        SpendingCategory category = categoryComboBox.getValue();
        LocalDate date = datePicker.getValue();
        String description = descriptionField.getValue();

        // Create Expense entity and save it
        Expense expense = new Expense();
        expense.setAmount(amount);
        expense.setCategory(category);
        expense.setDate(date.atStartOfDay());
        expense.setUser(currentUser);
        expense.setDescription(description != null && !description.isEmpty() ? description : "No description");

        // Save expense
        expenseService.saveExpense(expense);

        category.setBudgetAmount(amount);

        // Navigate back to dashboard
        getUI().ifPresent(ui -> ui.navigate("dashboard"));
    }
}