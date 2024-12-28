package com.finpulse.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import com.finpulse.model.Expense;
import com.finpulse.model.SpendingCategory;
import com.finpulse.model.User;
import com.finpulse.service.AuthenticationService;
import com.finpulse.service.ExpenseService;
import com.finpulse.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Route(value = "budget", layout = MainLayout.class)
@PermitAll
public class BudgetView extends VerticalLayout {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final ExpenseService expenseService;

    private final NumberField dailySpendingLimitField;

    private static final Map<String, String> CURRENCY_SYMBOLS = new HashMap<>();

    static {
        CURRENCY_SYMBOLS.put("USD ($)", "$");
        CURRENCY_SYMBOLS.put("EUR (€)", "€");
        CURRENCY_SYMBOLS.put("GBP (£)", "£");
        CURRENCY_SYMBOLS.put("JPY (¥)", "¥");
        CURRENCY_SYMBOLS.put("CNY (¥)", "¥");
        CURRENCY_SYMBOLS.put("CAD ($)", "$");
        CURRENCY_SYMBOLS.put("AUD ($)", "$");
    }

    @Autowired
    public BudgetView(AuthenticationService authenticationService, UserService userService, ExpenseService expenseService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.expenseService = expenseService;

        User currentUser = authenticationService.getCurrentUser();
        double currentSavings = currentUser.getCurrentSavings();
        double savingsGoal = currentUser.getMonthlySavingGoal();

        // Ensure currentSavings is within the valid range
        if (currentSavings < 0) {
            currentSavings = 0;
        } else if (currentSavings > savingsGoal) {
            currentSavings = savingsGoal;
        }

        // Progress bar for current savings vs. savings goal
        VerticalLayout progressBarLayout = createProgressBarLayout(currentSavings, savingsGoal);
        add(progressBarLayout);

        // Widget for current savings goal
        VerticalLayout savingsGoalWidget = createSavingsGoalWidget();
//        Button setSavingsGoalButton = new Button("Set Savings Goal", event -> openSavingsGoalDialog());
//        savingsGoalWidget.add(setSavingsGoalButton);

        // Widget for spending categories
        VerticalLayout spendingCategoriesWidget = createSpendingCategoriesWidget();
        Button addCategoryButton = new Button("Add Category", event -> openAddCategoryDialog());
        spendingCategoriesWidget.add(addCategoryButton);

        // Widget for daily spending limit
        VerticalLayout dailySpendingLimitWidget = createDailySpendingLimitWidget();
        dailySpendingLimitField = new NumberField("Daily Spending Limit");
        dailySpendingLimitField.setValue(currentUser.getDailySpendingLimit());
        Button saveDailyLimitButton = new Button("Save", event -> saveDailySpendingLimit());
        dailySpendingLimitWidget.add(dailySpendingLimitField, saveDailyLimitButton);

        add(new Hr());

        VerticalLayout layout = new VerticalLayout();
        layout.addClassName("progress-bar-layout");

        H3 title = new H3("Update Budget");
        Paragraph description = new Paragraph("Use the form below to update your budget settings.");
        layout.add(title, description);
        add(layout);

        // Layout for widgets
        HorizontalLayout widgetsLayout = new HorizontalLayout(savingsGoalWidget, spendingCategoriesWidget, dailySpendingLimitWidget);
        add(widgetsLayout);

        add(new Hr());


        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }

    private VerticalLayout createProgressBarLayout(double currentSavings, double savingsGoal) {
        VerticalLayout layout = new VerticalLayout();
        layout.addClassName("progress-bar-layout");

        H3 title = new H3("Savings Progress");
        Paragraph description = new Paragraph("This progress bar shows your current savings compared to your savings goal.");

        ProgressBar savingsProgressBar = new ProgressBar(0, savingsGoal, currentSavings);
        savingsProgressBar.setWidthFull();

        String formattedCurrentSavings = String.format("%,.2f", currentSavings);
        String formattedSavingsGoal = String.format("%,.2f", savingsGoal);
        String preferredCurrency = authenticationService.getCurrentUser().getPreferredCurrency();
        String currencySymbol = CURRENCY_SYMBOLS.getOrDefault(preferredCurrency, "$");

        Paragraph progressIndicator = new Paragraph(
                String.format("Current Savings: %s%s / Savings Goal: %s%s", currencySymbol, formattedCurrentSavings, currencySymbol, formattedSavingsGoal)
        );

        layout.add(title, description, savingsProgressBar, progressIndicator);
        return layout;
    }

    private VerticalLayout createSavingsGoalWidget() {
        VerticalLayout widget = new VerticalLayout();
        widget.addClassName("widget");

        H3 title = new H3("Savings Goal");
        User currentUser = authenticationService.getCurrentUser();
        double savingsGoal = currentUser.getMonthlySavingGoal();
        String preferredCurrency = currentUser.getPreferredCurrency();
        String currencySymbol = CURRENCY_SYMBOLS.getOrDefault(preferredCurrency, "$");

        // Format the savings goal amount with commas
        String formattedSavingsGoal = String.format("%,.2f", savingsGoal);

        // Display the currency symbol followed by the formatted amount
        Paragraph savingsGoalDisplay = new Paragraph(currencySymbol + " " + formattedSavingsGoal);

        Button setSavingsGoalButton = new Button("Set Savings Goal", event -> openSavingsGoalDialog());
        widget.add(title, savingsGoalDisplay, setSavingsGoalButton);
        return widget;
    }

    private void openSavingsGoalDialog() {
        Dialog dialog = new Dialog();
        NumberField newSavingsGoalField = new NumberField("New Savings Goal");
        Button saveButton = new Button("Save", event -> {
            User currentUser = authenticationService.getCurrentUser();
            currentUser.setMonthlySavingGoal(newSavingsGoalField.getValue());
            userService.saveUser(currentUser);
            dialog.close();
            Notification.show("Savings goal updated successfully!", 3000, Notification.Position.BOTTOM_END);
        });
        dialog.add(newSavingsGoalField, saveButton);
        dialog.open();
    }

    private VerticalLayout createSpendingCategoriesWidget() {
        VerticalLayout widget = new VerticalLayout();
        widget.addClassName("widget");

        H3 title = new H3("Spending Categories");
        widget.add(title);

        User currentUser = authenticationService.getCurrentUser();
        Set<SpendingCategory> spendingCategories = currentUser.getSpendingCategories();

        HorizontalLayout categoriesLayout = new HorizontalLayout();
        categoriesLayout.getStyle().set("flex-wrap", "wrap");

        for (SpendingCategory category : spendingCategories) {
            VerticalLayout categoryWidget = new VerticalLayout();
            categoryWidget.addClassName("category-widget");

            Paragraph categoryName = new Paragraph(category.getName());

            categoryWidget.add(categoryName);
            categoriesLayout.add(categoryWidget);
        }

        widget.add(categoriesLayout);
        return widget;
    }

    private VerticalLayout createDailySpendingLimitWidget() {
        VerticalLayout widget = new VerticalLayout();
        widget.addClassName("widget");

        H3 title = new H3("Daily Spending Limit");
        widget.add(title);

        return widget;
    }

    private void openAddCategoryDialog() {
        Dialog dialog = new Dialog();
        TextField categoryNameField = new TextField("Category Name");
        NumberField budgetAmountField = new NumberField("Budget Amount");
        Button saveButton = new Button("Save", event -> {
            User currentUser = authenticationService.getCurrentUser();
            SpendingCategory category = new SpendingCategory();
            category.setName(categoryNameField.getValue());
            category.setBudgetAmount(budgetAmountField.getValue());
            category.setUser(currentUser);
            userService.addSpendingCategory(category);
            dialog.close();
            Notification.show("Category added successfully!", 3000, Notification.Position.BOTTOM_END);
        });
        dialog.add(categoryNameField, budgetAmountField, saveButton);
        dialog.open();
    }

    private void saveDailySpendingLimit() {
        User currentUser = authenticationService.getCurrentUser();
        currentUser.setDailySpendingLimit(dailySpendingLimitField.getValue());
        userService.saveUser(currentUser);
        Notification.show("Daily spending limit updated successfully!", 3000, Notification.Position.BOTTOM_END);
    }

    private void saveBudget(String categoryName, double budgetAmount, double savingsGoal) {
        User currentUser = authenticationService.getCurrentUser();

        SpendingCategory category = new SpendingCategory();
        category.setName(categoryName);
        category.setBudgetAmount(budgetAmount);
        category.setUser(currentUser);

        currentUser.getSpendingCategories().add(category);
        currentUser.setMonthlySavingGoal(savingsGoal);

        userService.saveUser(currentUser);

        Notification.show("Budget saved successfully!", 3000, Notification.Position.BOTTOM_END);
    }

    private void checkBudgetLimits() {
        User currentUser = authenticationService.getCurrentUser();
        Set<SpendingCategory> spendingCategories = currentUser.getSpendingCategories();

        for (SpendingCategory category : spendingCategories) {
            double totalSpent = expenseService.getExpensesByCategory(currentUser, category.getName())
                    .stream().mapToDouble(Expense::getAmount).sum();

            if (totalSpent > category.getBudgetAmount()) {
                Notification.show("You have exceeded your budget for " + category.getName(), 5000, Notification.Position.BOTTOM_END);
            } else if (totalSpent > category.getBudgetAmount() * 0.9) {
                Notification.show("You are approaching your budget limit for " + category.getName(), 5000, Notification.Position.BOTTOM_END);
            }
        }
    }
}