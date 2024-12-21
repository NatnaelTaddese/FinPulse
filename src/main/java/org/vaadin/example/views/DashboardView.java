package org.vaadin.example.views;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaadin.example.model.User;
import org.vaadin.example.model.Transaction;
import org.vaadin.example.security.CustomUserDetails;
import org.vaadin.example.service.AlipayService;
import org.vaadin.example.service.AuthenticationService;


import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle("Dashboard | FinPulse")
@PermitAll
public class DashboardView extends Composite<VerticalLayout> {

    @Value("${alipay.appId}")
    private String appId;

    @Value("${alipay.redirectUri}")
    private String redirectUri;

    private final AlipayService alipayService;
    private final AuthenticationService authenticationService;

    private static final Logger logger = LoggerFactory.getLogger(DashboardView.class);

    @Autowired
    public DashboardView(@Value("${alipay.appId}") String appId,
                         @Value("${alipay.redirectUri}") String redirectUri,
                         AlipayService alipayService,
                         AuthenticationService authenticationService) {
        this.appId = appId;
        this.redirectUri = redirectUri;
        this.alipayService = alipayService;
        this.authenticationService = authenticationService;

        VerticalLayout layout = new VerticalLayout();

        // Get query parameters
        Location location = UI.getCurrent().getInternals().getActiveViewLocation();
        Map<String, List<String>> parameters = location.getQueryParameters().getParameters();

        // Check for error or success messages
        if (parameters.containsKey("error")) {
            String error = parameters.get("error").get(0);
            Notification.show("Error: " + error, 5000, Notification.Position.BOTTOM_END);
        } else if (parameters.containsKey("success")) {
            Notification.show("Successfully connected to Alipay!", 5000, Notification.Position.BOTTOM_END);
        }

        HorizontalLayout header = new HorizontalLayout();
        H2 appName = new H2("Welcome back, " + getLoggedInFirstName() + "!");
        header.add(appName);


        Button connectAlipayButton = new Button("Connect Alipay", event -> {
            String authUrl = getAlipayAuthUrl();
            logger.info("Redirecting to Alipay auth URL: " + authUrl);
            getUI().ifPresent(ui -> ui.getPage().setLocation(authUrl));
        });

        connectAlipayButton.setIcon( new SvgIcon("/icons/Alipay-logo.svg"));
        header.add(connectAlipayButton);

        header.addClassNames(
                LumoUtility.Display.FLEX,
                LumoUtility.JustifyContent.BETWEEN,
                LumoUtility.AlignItems.CENTER
        );

        layout.add(header);
        layout.add(new Hr());


        // Display Alipay account balance and recent transactions if connected
        try {
            User currentUser = authenticationService.getCurrentUser();
            if (currentUser.getAlipayToken() != null) {
                try {

                    String balance = alipayService.getAccountBalance(currentUser.getAlipayToken());
                    layout.add(new Paragraph("Alipay Account Balance: " + balance));

//                    List<Transaction> transactions = alipayService.getRecentTransactions(currentUser.getAlipayToken());
//                    Grid<Transaction> transactionGrid = new Grid<>(Transaction.class);
//                    transactionGrid.setItems(transactions);
//                    transactionGrid.setColumns("date", "amount", "description");
//                    layout.add(transactionGrid);
                } catch (Exception e) {
                    if (e.getMessage().contains("Application is not online")) {
                        Span developmentNote = new Span(e.getMessage());
                        developmentNote.getStyle().set("color", "var(--lumo-secondary-text-color)");
                        layout.add(developmentNote);
                    } else {
                        logger.error("Failed to fetch Alipay account balance or transactions", e);
                        Notification.show("Failed to fetch Alipay account balance or transactions: " + e.getMessage(),
                                5000, Notification.Position.BOTTOM_CENTER);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Failed to get current user", e);
            Notification.show("Failed to get user information: " + e.getMessage(),
                    5000, Notification.Position.BOTTOM_END);
        }

        getContent().add(layout);
    }

    private String getAlipayAuthUrl() {
        String encodedRedirectUri = URLEncoder.encode(redirectUri, StandardCharsets.UTF_8);
        return "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm" +
                "?app_id=" + appId +
                "&scope=auth_user" +
                "&redirect_uri=" + encodedRedirectUri +
                "&state=init";  // Adding state parameter for security
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

    public String getLoggedInFirstName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return userDetails.getFirstName();
        } else {
            return getLoggedInUsername().substring(0, 1).toUpperCase() + getLoggedInUsername().substring(1);
        }
    }
}
