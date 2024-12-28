package com.finpulse.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.Theme;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

@AnonymousAllowed
@Route("login")
@PageTitle("Login | FinPulse")
public class LoginView extends VerticalLayout {

    public LoginView() {
        LoginForm loginForm = new LoginForm();
        loginForm.setAction("login");

        // Add remember-me checkbox
        loginForm.getElement().setAttribute(
                "onlogin",
                "event.preventDefault();" +
                        "document.querySelector('[name=\"remember-me\"]').checked = true;" +
                        "document.querySelector('form').submit();"
        );

        H1 title = new H1("FinPulse");

        // Link to signup page
        Anchor signupLink = new Anchor("/signup", "Don't have an account? Sign Up");

        add(title, loginForm, signupLink);

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }
}