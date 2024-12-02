package org.vaadin.example.views;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle("Dashboard | FinIntel")
public class DashboardView extends Composite<VerticalLayout> {
    public DashboardView() {
        VerticalLayout layout = new VerticalLayout();
        layout.add("Dashboard");
        layout.add("Welcome to the Financial Intelligence Platform Dashboard");
        layout.add("This is where you can view your financial data and analytics");
        getContent().add(layout);
    }
}
