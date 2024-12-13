package org.vaadin.example.views;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle("Dashboard | FinPulse")
@PermitAll
public class DashboardView extends Composite<VerticalLayout> {
    public DashboardView() {

        VerticalLayout layout = new VerticalLayout();
        H2 appName = new H2("Welcome back, " + getLoggedInUsername());
        layout.add(appName);
        layout.add(new Hr());
        layout.add(new Paragraph("This is where you can view your financial data and analytics") );
        getContent().add(layout);
    }

    public String getLoggedInUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); // This will return the username
    }
}
