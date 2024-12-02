package org.vaadin.example.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class MainLayout extends AppLayout {

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        H1 applicationTitle = new H1("Financial Intelligence Platform");
        applicationTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.XLARGE);

        Header header = new Header(toggle, applicationTitle);
        header.addClassNames(
                LumoUtility.Padding.Vertical.SMALL,
                LumoUtility.BorderColor.CONTRAST_10
        );
        addToNavbar(header);
    }

    private void addDrawerContent() {
        H2 appName = new H2("FinIntel");
        appName.addClassNames(LumoUtility.FontSize.LARGE);
        appName.addClassName("drawer-app-name");

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(
                appName,
                scroller,
                createFooter()
        );
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        nav.addItem(new SideNavItem("Dashboard", String.valueOf(DashboardView.class), VaadinIcon.DASHBOARD.create()));
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
}