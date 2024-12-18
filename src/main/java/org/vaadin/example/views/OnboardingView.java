package org.vaadin.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToDoubleConverter;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.model.SpendingCategory;
import org.vaadin.example.model.User;
import org.vaadin.example.service.AuthenticationService;

import java.util.Set;
import java.util.stream.Collectors;

@Route("onboarding")
@PermitAll
public class OnboardingView extends VerticalLayout {

    private final AuthenticationService authenticationService;
    private Binder<User> binder;

    private TextField incomeField;
    private TextField dailySpendingField;
    private MultiSelectComboBox<String> expenseCategoriesField;

    @Autowired
    public OnboardingView(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;

        initLayout();
        bindFields();
    }

    private void initLayout() {
        H1 title = new H1("Complete Your Profile");
        title.addClassName("onboarding-title");

        incomeField = new TextField("Monthly Income");
        dailySpendingField = new TextField("Daily Spending Limit");
        expenseCategoriesField = new MultiSelectComboBox<>("Expense Categories");
        expenseCategoriesField.setItems("Food", "Rent", "Utilities", "Transportation", "Entertainment", "Other");

        Button saveButton = new Button("Save and Continue", event -> saveOnboardingData());

        add(title, incomeField, dailySpendingField, expenseCategoriesField, saveButton);
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }

    private void bindFields() {
        binder = new Binder<>(User.class);

        binder.forField(incomeField)
                .withConverter(new StringToDoubleConverter("Must be a number"))
                .bind(User::getIncome, User::setIncome);

        binder.forField(dailySpendingField)
                .withConverter(new StringToDoubleConverter("Must be a number"))
                .bind(
                        user -> Double.valueOf(String.valueOf(user.getDailySpendingLimit())),
                        (user, value) -> user.setDailySpendingLimit(Double.parseDouble(String.valueOf(value)))
                );

        User currentUser = authenticationService.getCurrentUser();
        binder.readBean(currentUser);
    }

    private void saveOnboardingData() {
        User currentUser = authenticationService.getCurrentUser();

        try {
            // Validate and write bean
            binder.writeBean(currentUser);

            // Handle expense categories
            Set<SpendingCategory> spendingCategories = expenseCategoriesField.getSelectedItems().stream()
                    .map(categoryName -> {
                        SpendingCategory category = new SpendingCategory();
                        category.setName(categoryName);
                        category.setUser(currentUser);
                        return category;
                    })
                    .collect(Collectors.toSet());

            currentUser.setSpendingCategories(spendingCategories);

            // Mark onboarding as completed
            currentUser.setOnboardingCompleted(true);

            // Update user
            authenticationService.updateUser(currentUser);

            // Navigate to dashboard
            getUI().ifPresent(ui -> ui.navigate("dashboard"));
        } catch (Exception e) {
            // Handle validation errors
            add(new H1("Validation failed"));
            e.printStackTrace();
        }
    }

}