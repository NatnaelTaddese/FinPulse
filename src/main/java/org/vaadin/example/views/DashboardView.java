package org.vaadin.example.views;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaadin.example.security.CustomUserDetails;
import org.vaadin.example.service.AlipayService;

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

    private static final Logger logger = LoggerFactory.getLogger(DashboardView.class);

    public DashboardView() {
        VerticalLayout layout = new VerticalLayout();

        // Get query parameters
        Location location = UI.getCurrent().getInternals().getActiveViewLocation();
        Map<String, List<String>> parameters = location.getQueryParameters().getParameters();

        // Check for error or success messages
        if (parameters.containsKey("error")) {
            String error = parameters.get("error").get(0);
            Notification.show("Error: " + error, 5000, Notification.Position.TOP_CENTER);
        } else if (parameters.containsKey("success")) {
            Notification.show("Successfully connected to Alipay!", 5000, Notification.Position.TOP_CENTER);
        }

        H2 appName = new H2("Welcome back, " + getLoggedInUsername());
        layout.add(appName);
        layout.add(new Hr());

        Button connectAlipayButton = new Button("Connect Alipay", event -> {
            String authUrl = getAlipayAuthUrl();
            logger.info("Redirecting to Alipay auth URL: " + authUrl);
            getUI().ifPresent(ui -> ui.getPage().setLocation(authUrl));
        });

        layout.add(connectAlipayButton);
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
}