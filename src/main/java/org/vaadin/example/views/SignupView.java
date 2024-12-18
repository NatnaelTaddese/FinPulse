package org.vaadin.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import org.vaadin.example.service.AuthenticationService;

@AnonymousAllowed
@Route("signup")
@PageTitle("Sign Up | FinPulse")
public class SignupView extends VerticalLayout {
    private final AuthenticationService authenticationService;

    public SignupView(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;

        // Set up the overall layout
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setPadding(true);
        setSpacing(true);
        setMaxWidth("500px");
        getStyle().set("margin", "0 auto");

        // Title
        H1 title = new H1("Create FinPulse Account");
        title.getStyle()
                .set("text-align", "center")
                .set("color", "#333")
                .set("margin-bottom", "20px");

        // Subtitle
        Paragraph subtitle = new Paragraph("Join FinPulse to take control of your financial journey");
        subtitle.getStyle()
                .set("color", "#666")
                .set("margin-bottom", "30px")
                .set("text-align", "center");

        // Create form fields with improved styling
        TextField usernameField = createUsernameField();
        EmailField emailField = createEmailField();
        PasswordField passwordField = createPasswordField();
        PasswordField confirmPasswordField = createConfirmPasswordField();

        // Signup button with enhanced styling
        Button signupButton = new Button("Sign Up", event -> {
            try {
                // Validate input
                if (!validateInput(usernameField, emailField, passwordField, confirmPasswordField)) {
                    return;
                }

                // Register user
                authenticationService.registerNewUser(
                        usernameField.getValue(),
                        emailField.getValue(),
                        passwordField.getValue()
                );

                // Show success notification
                Notification.show("Account created successfully! Please log in.", 3000, Notification.Position.TOP_CENTER);

                // Redirect to login page
                getUI().ifPresent(ui -> ui.navigate("login"));

            } catch (Exception e) {
                Notification.show("Registration failed: " + e.getMessage(), 3000, Notification.Position.TOP_CENTER);
            }
        });
        signupButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        signupButton.getStyle()
                .set("width", "100%")
                .set("margin-top", "20px");

        Anchor loginLink = new Anchor("/login", "Already have an account? Log In");
        loginLink.getStyle()
                .set("display", "block")
                .set("text-align", "center")
                .set("margin-top", "20px");

        // Layout
        FormLayout formLayout = new FormLayout(
                usernameField,
                emailField,
                passwordField,
                confirmPasswordField,
                signupButton,
                loginLink
        );
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1)
        );
        formLayout.setColspan(usernameField, 1);
        formLayout.setColspan(emailField, 1);
        formLayout.setColspan(passwordField, 1);
        formLayout.setColspan(confirmPasswordField, 1);
        formLayout.setColspan(signupButton, 1);

        // Add components
        add(title, subtitle, formLayout);
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }

    private TextField createUsernameField() {
        TextField usernameField = new TextField("Username");
        usernameField.setRequired(true);
        usernameField.setErrorMessage("Username is required");
        usernameField.getStyle()
                .set("width", "100%");
        return usernameField;
    }

    private EmailField createEmailField() {
        EmailField emailField = new EmailField("Email");
        emailField.setRequired(true);
        emailField.setErrorMessage("Please enter a valid email address");
        emailField.getStyle()
                .set("width", "100%");
        return emailField;
    }

    private PasswordField createPasswordField() {
        PasswordField passwordField = new PasswordField("Password");
        passwordField.setRequired(true);
        passwordField.setErrorMessage("Password is required");
        passwordField.getStyle()
                .set("width", "100%");
        return passwordField;
    }

    private PasswordField createConfirmPasswordField() {
        PasswordField confirmPasswordField = new PasswordField("Confirm Password");
        confirmPasswordField.setRequired(true);
        confirmPasswordField.setErrorMessage("Passwords must match");
        confirmPasswordField.getStyle()
                .set("width", "100%");
        return confirmPasswordField;
    }

    private boolean validateInput(
            TextField usernameField,
            EmailField emailField,
            PasswordField passwordField,
            PasswordField confirmPasswordField
    ) {
        boolean isValid = true;

        // Username validation
        if (usernameField.getValue().trim().isEmpty()) {
            usernameField.setInvalid(true);
            isValid = false;
        }
        // if username already exists
        if (authenticationService.userExists(usernameField.getValue())) {
            usernameField.setErrorMessage("Username already exists");
            usernameField.setInvalid(true);
            isValid = false;
        }

        // Email validation
        if (emailField.getValue().trim().isEmpty()) {
            emailField.setInvalid(true);
            isValid = false;
        }

        // valid email address
        if (!emailField.getValue().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            emailField.setErrorMessage("Please enter a valid email address");
            emailField.setInvalid(true);
            isValid = false;
        }

        // Password validation
        if (passwordField.getValue().length() < 8) {
            passwordField.setErrorMessage("Password must be at least 8 characters long");
            passwordField.setInvalid(true);
            isValid = false;
        }

        // Confirm password validation
        if (!passwordField.getValue().equals(confirmPasswordField.getValue())) {
            confirmPasswordField.setErrorMessage("Passwords do not match");
            confirmPasswordField.setInvalid(true);
            isValid = false;
        }

        return isValid;
    }
}