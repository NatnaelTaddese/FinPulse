package org.vaadin.example.views;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.service.GreetService;

/**
 * A sample Vaadin view class.
 * <p>
 * To implement a Vaadin view just extend any Vaadin component and use @Route
 * annotation to announce it in a URL as a Spring managed bean.
 * <p>
 * A new instance of this class is created for every new user and every browser
 * tab/window.
 * <p>
 * The main view contains a text field for getting the user name and a button
 * that shows a greeting message in a notification.
 */

@AnonymousAllowed
@Route
public class MainView extends VerticalLayout {

    /**
     * Construct a new Vaadin view.
     * <p>
     * Build the initial UI state for the user accessing the application.
     *
     * @param service
     *            The message service. Automatically injected Spring managed bean.
     */
    public MainView(@Autowired GreetService service) {

        H1 title = new H1("Welcome to FinPulse");
        title.addClassName("centered-title");

        Button loginButton = new Button("Login", e -> {
            // navigate to login view
            getUI().ifPresent(ui -> ui.navigate("login"));
        });

        loginButton.addThemeVariants(
                ButtonVariant.LUMO_PRIMARY
        );

        Button signupButton = new Button("Sign Up", e -> {
            // navigate to signup view
            getUI().ifPresent(ui -> ui.navigate("signup"));
        });

        signupButton.addThemeVariants(
                ButtonVariant.MATERIAL_OUTLINED
        );

        Button dashboardButton = new Button("Dashboard", e -> {
            // navigate to dashboard view
            getUI().ifPresent(ui -> ui.navigate("dashboard"));
        });

        dashboardButton.addThemeVariants(
                ButtonVariant.LUMO_TERTIARY
        );

        add(title, loginButton, signupButton);
        add(new Hr());
        add(dashboardButton);
        addClassName("centered-content");
        setAlignItems(Alignment.CENTER);
        setHeightFull();

        // Boilerplate code from the Vaadin starter project
        // FOR REFERENCE


//        // Use TextField for standard text input
//        TextField textField = new TextField("Your name");
//        textField.addClassName("bordered");
//
//        // Button click listeners can be defined as lambda expressions
//        Button button = new Button("Say hello", e -> {
//            if(textField.getValue().equals("login")){
//                // navigate to dashboard view
//                getUI().ifPresent(ui -> ui.navigate("dashboard"));
//            }
//            add(new Paragraph(service.greet(textField.getValue())));
//        });
//
//        // Theme variants give you predefined extra styles for components.
//        // Example: Primary button has a more prominent look.
//        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
//
//        // You can specify keyboard shortcuts for buttons.
//        // Example: Pressing enter in this view clicks the Button.
//        button.addClickShortcut(Key.ENTER);
//
//        // Use custom CSS classes to apply styling. This is defined in
//        // styles.css.
//        addClassName("centered-content");
//
//        add(textField, button);
    }
}
