package com.finpulse.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.finpulse.model.User;
import com.finpulse.security.CustomUserDetails;
import com.finpulse.service.AuthenticationService;


public class MainLayout extends AppLayout implements BeforeEnterObserver {

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        User currentUser = authenticationService.getCurrentUser();

        // If onboarding is not completed, redirect to onboarding view
        if (!authenticationService.isOnboardingCompleted(currentUser)) {
            event.forwardTo("onboarding");  // Redirect to onboarding
        }
    }


    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

//    private void addHeaderContent() {
//        DrawerToggle toggle = new DrawerToggle();
//        H1 applicationTitle = new H1("Financial Intelligence Platform");
//        applicationTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.XLARGE);
//
//
//
//        Header header = new Header(toggle, applicationTitle);
//        header.addClassNames(
//                LumoUtility.Padding.Vertical.SMALL,
//                LumoUtility.BorderColor.CONTRAST_10,
//                LumoUtility.Display.FLEX,
//                LumoUtility.JustifyContent.BETWEEN
//        );
//        addToNavbar(header);
//    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setIcon(new SvgIcon("/icons/panel-left.svg"));

        Avatar avatar = new Avatar(getLoggedInUsername().toUpperCase());

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(toggle,avatar);
        Header header = new Header(horizontalLayout);
        header.addClassNames("main-header");



        addToNavbar(header,avatar);
    }


private void addDrawerContent() {
    // Logo or App Name
    H1 logo = new H1("FinPulse");
    logo.addClassNames("sidebar-logo");

    // Navigation Menu
    Scroller navigation = new Scroller(createNavigation());
    navigation.addClassName("sidebar-nav");

    // Footer (User Profile Section)
    Footer footer = new Footer();
    footer.add(createLogoutButton());
    footer.addClassName("sidebar-footer");

    addToDrawer(logo, navigation, footer);
}


    private Button createLogoutButton() {
        Button logoutButton = new Button("Logout", VaadinIcon.SIGN_OUT.create());
        logoutButton.addClickListener(event -> {
            // Get the current security context
            SecurityContextHolder.getContext().setAuthentication(null);

            // Invalidate the session
            VaadinSession.getCurrent().getSession().invalidate();

            // Redirect to login page

//            UI.getCurrent().navigate("login");
            UI.getCurrent().access(() -> UI.getCurrent().navigate("login"));
        });
        return logoutButton;
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        // Add logout button

        nav.addItem(new SideNavItem("Dashboard", DashboardView.class, VaadinIcon.DASHBOARD.create()));
        nav.addItem(new SideNavItem("Expenses", ExpenseView.class, VaadinIcon.ADD_DOCK.create()));
        nav.addItem(new SideNavItem("Budget", BudgetView.class, VaadinIcon.MONEY.create()));
        nav.addItem(new SideNavItem("History", HistoryView.class, VaadinIcon.CALENDAR_USER.create()));
//        nav.addItem(new SideNavItem("Market Data", MarketDataView.class, VaadinIcon.CHART.create()));
//        nav.addItem(new SideNavItem("Portfolio", PortfolioView.class, VaadinIcon.BRIEFCASE.create()));
//        nav.addItem(new SideNavItem("Analytics", AnalyticsView.class, VaadinIcon.CALC.create()));
//        nav.addItem(new SideNavItem("Risk Management", RiskView.class, VaadinIcon.SHIELD.create()));
//        nav.addItem(new SideNavItem("Trading", TradingView.class, VaadinIcon.EXCHANGE.create()));


        return nav;
    }

    private Footer createFooter() {
        Footer footer = new Footer();
        // Add additional footer content if needed
        return footer;
    }

    public String getLoggedInUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        } else {
            return "Guest";
        }
    }
}