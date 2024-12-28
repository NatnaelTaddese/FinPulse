package com.finpulse.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.TabSheetVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToDoubleConverter;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import com.finpulse.model.SpendingCategory;
import com.finpulse.model.User;
import com.finpulse.service.AuthenticationService;

import java.util.*;
import java.util.stream.Collectors;

@Route("onboarding")
@PermitAll
public class OnboardingView extends Composite<VerticalLayout> {
    private final TabSheet tabSheet = new TabSheet();
    private final ProgressBar progressBar = new ProgressBar();
    private final AuthenticationService authenticationService;
    private Binder<User> binder;

    // Form fields
    private TextField firstNameField;
    private TextField lastNameField;
    private ComboBox<String> preferredCurrencyField;
    private TextField incomeField;
    private TextField dailySpendingField;
    private TextField monthlySavingGoalField;
    private TextField currentBalanceField;
    private TextField currentSavingsField;
    private ComboBox<String> payScheduleField;
    private MultiSelectComboBox<String> expenseCategoriesField;

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
    public OnboardingView(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
        initComponents();
        setupLayout();
        bindFields();
    }

    private void initComponents() {
        // Personal Info Tab
        FormLayout personalInfo = new FormLayout();
        H2 personalInfoMessage = new H2("Let's get to know you better!");
        personalInfoMessage.addClassName("centered-text");
        Paragraph personalInfoDesc = new Paragraph("Please share some basic information to personalize your experience.");
        personalInfoDesc.addClassName("centered-text");

        firstNameField = createTextField("First Name", "Enter your first name");
        lastNameField = createTextField("Last Name", "Enter your last name");

        preferredCurrencyField = new ComboBox<>("Preferred Currency");
        preferredCurrencyField.setItems(CURRENCY_SYMBOLS.keySet());
        preferredCurrencyField.setWidthFull();
        preferredCurrencyField.addValueChangeListener(event -> updateCurrencySymbols());

        personalInfo.add(personalInfoMessage, personalInfoDesc, firstNameField, lastNameField, preferredCurrencyField);

        // Financial Info Tab
        FormLayout financialInfo = new FormLayout();
        H2 financialInfoMessage = new H2("Let's set up your financial goals!");
        financialInfoMessage.addClassName("centered-text");
        Paragraph financialInfoDesc = new Paragraph("This information helps us create a personalized financial plan for you.");
        financialInfoDesc.addClassName("centered-text");

        incomeField = createCurrencyField("Monthly Income");
        dailySpendingField = createCurrencyField("Daily Spending Limit");
        monthlySavingGoalField = createCurrencyField("Monthly Saving Goal");
        currentBalanceField = createCurrencyField("Current Balance");
        currentSavingsField = createCurrencyField("Current Savings");
        payScheduleField = new ComboBox<>("Pay Schedule");
        payScheduleField.setItems("weekly", "monthly");
        payScheduleField.setWidthFull();

        expenseCategoriesField = new MultiSelectComboBox<>("Expense Categories");
        expenseCategoriesField.setItems("Food", "Rent", "Utilities", "Transportation", "Entertainment", "Other");
        expenseCategoriesField.setWidthFull();

        financialInfo.add(
                financialInfoMessage,
                financialInfoDesc,
                incomeField,
                dailySpendingField,
                monthlySavingGoalField,
                currentBalanceField,
                currentSavingsField,
                payScheduleField,
                expenseCategoriesField
        );

        // Payment Methods Tab
        FormLayout paymentMethods = new FormLayout();
        H2 paymentMethodsMessage = new H2("Almost there! Let's set up your payment methods.");
        paymentMethodsMessage.addClassName("centered-text");
        Paragraph paymentMethodsDesc = new Paragraph("Choose your preferred payment methods for tracking expenses.");
        paymentMethodsDesc.addClassName("centered-text");
        Button alipayButton = createPaymentButton("Alipay", VaadinIcon.CREDIT_CARD);
        Button paypalButton = createPaymentButton("PayPal", VaadinIcon.CREDIT_CARD);
        Button wiseButton = createPaymentButton("Wise", VaadinIcon.CREDIT_CARD);
        paymentMethods.add(paymentMethodsMessage, paymentMethodsDesc, alipayButton, paypalButton, wiseButton);

        Button nextButton1 = new Button("Next", e -> tabSheet.setSelectedIndex(1));
        Button nextButton2 = new Button("Next", e -> tabSheet.setSelectedIndex(2));
        Button completeButton = new Button("Complete Setup", e -> saveOnboardingData());

        nextButton1.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        nextButton2.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        completeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        completeButton.setEnabled(false);

        // Add components to tabs
        personalInfo.add(nextButton1);
        financialInfo.add(nextButton2);
        paymentMethods.add(completeButton);

        // Center all layouts
        Arrays.asList(personalInfo, financialInfo, paymentMethods).forEach(layout -> {
            layout.setMaxWidth("600px");
            layout.getStyle().set("margin", "0 auto");
        });

        // Add tabs
        tabSheet.add("Personal Info", personalInfo);
        tabSheet.add("Financial Details", financialInfo);
        tabSheet.add("Payment Methods", paymentMethods);
        tabSheet.addClassName("tab-sheet-centered");

        tabSheet.addThemeVariants(TabSheetVariant.LUMO_BORDERED);
        tabSheet.addThemeVariants(TabSheetVariant.LUMO_TABS_CENTERED);

    }

    private TextField createTextField(String label, String placeholder) {
        TextField field = new TextField(label);
        field.setPlaceholder(placeholder);
        field.setHelperText("Required");
        field.setWidthFull();
        return field;
    }

    private TextField createCurrencyField(String label) {
        TextField field = createTextField(label, "0.00");
        String selectedCurrency = preferredCurrencyField != null && preferredCurrencyField.getValue() != null
                ? CURRENCY_SYMBOLS.get(preferredCurrencyField.getValue())
                : "$";
        field.setPrefixComponent(new Span(selectedCurrency));
        return field;
    }

    private Button createPaymentButton(String text, VaadinIcon icon) {
        Button button = new Button(text, new Icon(icon));
        button.addThemeVariants(ButtonVariant.LUMO_LARGE);
        button.setWidthFull();
        button.addClickListener(e -> Notification.show("Selected " + text + " as payment method"));
        return button;
    }

    private void setupLayout() {
        VerticalLayout mainLayout = getContent();
        mainLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        mainLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        mainLayout.setMaxWidth("800px");
        mainLayout.getStyle().set("margin", "0 auto");

        H1 title = new H1("Welcome to FinPulse");
        title.getStyle().set("margin", "0");

        progressBar.setWidth("100%");
        progressBar.setMin(0);
        progressBar.setMax(3);
        progressBar.setValue(1);

        tabSheet.addSelectedChangeListener(event -> {
            int step = tabSheet.getSelectedIndex() + 1;
            progressBar.setValue(step);
            updateProgress(step);
        });

        Button completeButton = new Button("Complete Setup", e -> saveOnboardingData());
        completeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        getContent().add(title, progressBar, tabSheet, completeButton);
        getContent().setPadding(true);
        getContent().setSpacing(true);
    }

    private void bindFields() {
        binder = new Binder<>(User.class);

        binder.forField(firstNameField)
                .asRequired("First name is required")
                .bind(User::getFirstName, User::setFirstName);

        binder.forField(lastNameField)
                .asRequired("Last name is required")
                .bind(User::getLastName, User::setLastName);

        binder.forField(preferredCurrencyField)
                .asRequired("Preferred currency is required")
                .bind(User::getPreferredCurrency, User::setPreferredCurrency);

        StringToDoubleConverter numberConverter = new StringToDoubleConverter("Must be a number");

        binder.forField(incomeField)
                .withConverter(numberConverter)
                .asRequired("Income is required")
                .bind(User::getIncome, User::setIncome);

        binder.forField(dailySpendingField)
                .withConverter(numberConverter)
                .asRequired("Daily spending limit is required")
                .bind(
                        user -> Double.valueOf(String.valueOf(user.getDailySpendingLimit())),
                        (user, value) -> user.setDailySpendingLimit(Double.parseDouble(String.valueOf(value)))
                );

        binder.forField(monthlySavingGoalField)
                .withConverter(numberConverter)
                .asRequired("Monthly saving goal is required")
                .bind(User::getMonthlySavingGoal, User::setMonthlySavingGoal);

        binder.forField(currentBalanceField)
                .withConverter(numberConverter)
                .asRequired("Current balance is required")
                .bind(User::getCurrentBalance, User::setCurrentBalance);

        binder.forField(currentSavingsField)
                .withConverter(numberConverter)
                .asRequired("Current savings is required")
                .bind(User::getCurrentSavings, User::setCurrentSavings);

        binder.forField(payScheduleField)
                .asRequired("Pay schedule is required")
                .bind(User::getPaySchedule, User::setPaySchedule);

        User currentUser = authenticationService.getCurrentUser();
        binder.readBean(currentUser);

        // Enable/disable complete button based on validation
        binder.addStatusChangeListener(event -> {
            boolean isValid = event.hasValidationErrors();
            Button completeButton = findCompleteButton();
            if (completeButton != null) {
                completeButton.setEnabled(!isValid && allFieldsFilled());
            }
        });
    }

    private boolean allFieldsFilled() {
        return !firstNameField.isEmpty() &&
                !lastNameField.isEmpty() &&
                !preferredCurrencyField.isEmpty() &&
                !incomeField.isEmpty() &&
                !monthlySavingGoalField.isEmpty() &&
                !expenseCategoriesField.isEmpty();
    }

    private Button findCompleteButton() {
        Optional<Component> button = tabSheet.getChildren()
                .filter(component -> component instanceof Button)
                .filter(component -> "Complete Setup".equals(((Button) component).getText()))
                .findFirst();
        return (Button) button.orElse(null);
    }

    private void updateProgress(int step) {
        progressBar.setValue(step);
    }

    private void updateCurrencySymbols() {
        String selectedCurrency = preferredCurrencyField.getValue();
        String symbol = CURRENCY_SYMBOLS.getOrDefault(selectedCurrency, "$");

        incomeField.setPrefixComponent(new Span(symbol));
        dailySpendingField.setPrefixComponent(new Span(symbol));
        monthlySavingGoalField.setPrefixComponent(new Span(symbol));
        currentBalanceField.setPrefixComponent(new Span(symbol));
        currentSavingsField.setPrefixComponent(new Span(symbol));
    }

    private void saveOnboardingData() {
        User currentUser = authenticationService.getCurrentUser();

        try {
            if (binder.validate().isOk()) {
                // Write the form contents to the user object
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
                currentUser.setOnboardingCompleted(true);

                // Update user in the database
                authenticationService.updateUser(currentUser);

                // Show success message and navigate to dashboard
                Notification.show("Profile setup completed successfully!", 3000, Notification.Position.TOP_CENTER);
                getUI().ifPresent(ui -> ui.navigate("dashboard"));
            } else {
                Notification.show("Please fill in all required fields", 3000, Notification.Position.TOP_CENTER);
            }
        } catch (Exception e) {
            Notification.show("Error saving profile: " + e.getMessage(), 3000, Notification.Position.TOP_CENTER);
            e.printStackTrace();
        }
    }
}