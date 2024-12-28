package org.vaadin.example.views;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.charts.model.style.SolidColor;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.icon.VaadinIcon;
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
import org.vaadin.example.components.ExpenseDrawer;
import org.vaadin.example.model.Expense;
import org.vaadin.example.model.SpendingCategory;
import org.vaadin.example.model.User;
import org.vaadin.example.model.Transaction;
import org.vaadin.example.security.CustomUserDetails;
import org.vaadin.example.service.AlipayService;
import org.vaadin.example.service.AuthenticationService;
import org.vaadin.example.service.ExpenseService;

import java.time.LocalDate;
import java.util.*;

import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

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
    private final ExpenseService expenseService;

    private static final Logger logger = LoggerFactory.getLogger(DashboardView.class);

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
    public DashboardView(@Value("${alipay.appId}") String appId,
                         @Value("${alipay.redirectUri}") String redirectUri,
                         AlipayService alipayService,
                         AuthenticationService authenticationService,
                         ExpenseService expenseService) {
        this.appId = appId;
        this.redirectUri = redirectUri;
        this.alipayService = alipayService;
        this.authenticationService = authenticationService;
        this.expenseService = expenseService;

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
            logger.info("Redirecting to Alipay auth URL: {}", authUrl);
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

//        Button addExpenseButton = new Button("Add Expense", event -> {
//            ExpenseDrawer drawer = new ExpenseDrawer();
//            drawer.open();
//        });
//
//        layout.add(addExpenseButton);

        // Create the widgets
        Div currentSavingsWidget = createCurrentSavingsWidget();
        Div transactionPieChartWidget = createTransactionPieChartWidget();
        Div spendingPerformanceWidget = createSpendingPerformanceWidget();

// Add the widgets to a horizontal layout
        HorizontalLayout widgetsLayout = new HorizontalLayout(currentSavingsWidget, transactionPieChartWidget, spendingPerformanceWidget);
        widgetsLayout.addClassName("widgets-layout");

// Add the widgets layout to the main layout
        layout.add(widgetsLayout);

        layout.add(new Hr());

        Div spendingTrendsChartWidget = createSpendingTrendsChartWidget();
        HorizontalLayout widgetsLayout2 = new HorizontalLayout(spendingTrendsChartWidget);
        widgetsLayout2.addClassName("widgets-layout");
        layout.add(widgetsLayout2);


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
                                5000, Notification.Position.BOTTOM_END);
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

    private Div createCurrentSavingsWidget() {
        Div widget = new Div();
        widget.addClassName("widget");

        H3 title = new H3("Current Savings");
        User currentUser = authenticationService.getCurrentUser();
        double currentSavings = currentUser.getCurrentSavings();
        String preferredCurrency = currentUser.getPreferredCurrency();
        String currencySymbol = CURRENCY_SYMBOLS.getOrDefault(preferredCurrency, "$");

        // Format the current savings amount with commas
        String formattedSavings = String.format("%,.2f", currentSavings);

        // Display the currency symbol followed by the formatted amount
        Paragraph savingsDisplay = new Paragraph(currencySymbol + " *** ");

        // eye toggle button
        Button toggleButton = new Button(new Icon(VaadinIcon.EYE));
        toggleButton.addClickListener(event -> {
            if ((currencySymbol + " *** ").equals(savingsDisplay.getText())) {
                savingsDisplay.setText(currencySymbol + " " + formattedSavings);
                toggleButton.setIcon(new Icon(VaadinIcon.EYE_SLASH));
            } else {
                savingsDisplay.setText((currencySymbol + " *** "));
                toggleButton.setIcon(new Icon(VaadinIcon.EYE));
            }
        });



        // Set initial icon to eye
        toggleButton.setIcon(new Icon(VaadinIcon.EYE));

        Button addButton = new Button("Add", event -> {
            // Handle add action
        });

        widget.add(title, savingsDisplay, toggleButton, addButton);
        return widget;
    }

    private Div createTransactionPieChartWidget() {
        Div widget = new Div();
        widget.addClassName("widget");

        H3 title = new H3("Current Transactions");

        // Create the pie chart
        Chart chart = new Chart(ChartType.PIE);
        Configuration conf = chart.getConfiguration();

        Tooltip tooltip = new Tooltip();
        tooltip.setValueDecimals(1);
        conf.setTooltip(tooltip);

        PlotOptionsPie plotOptions = new PlotOptionsPie();
        plotOptions.setAllowPointSelect(true);
        plotOptions.setCursor(Cursor.POINTER);
        plotOptions.setShowInLegend(true);
        conf.setPlotOptions(plotOptions);

        DataSeries series = new DataSeries();

        // Fetch expenses for the current user
        User currentUser = authenticationService.getCurrentUser();
        List<Expense> expenses = expenseService.getExpensesForUser(currentUser);

        // Group expenses by category and sum the amounts
        Map<String, Double> categoryTotals = new HashMap<>();
        for (Expense expense : expenses) {
            String categoryName = expense.getCategory().getName();
            categoryTotals.put(categoryName, categoryTotals.getOrDefault(categoryName, 0.0) + expense.getAmount());
        }

        // Add data to the series
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            DataSeriesItem item = new DataSeriesItem(entry.getKey(), entry.getValue());
            series.add(item);
        }

        conf.setSeries(series);
        chart.setVisibilityTogglingDisabled(true);

        widget.add(title, chart);
        return widget;
    }

    private Div createSpendingPerformanceWidget() {
        Div widget = new Div();
        widget.addClassName("widget");

        H3 title = new H3("Today's Spending Performance");

        // Create the solid gauge chart
        Chart chart = new Chart(ChartType.SOLIDGAUGE);
        Configuration conf = chart.getConfiguration();

        Pane pane = conf.getPane();
        pane.setSize("100%");           // For positioning tick labels
        pane.setCenter("50%", "70%");   // Move center lower
        pane.setStartAngle(-90);        // Make semi-circle
        pane.setEndAngle(90);

        Background bkg = new Background();
        bkg.setInnerRadius("60%");  // To make it an arc and not circle
        bkg.setOuterRadius("100%"); // Default - not necessary
        bkg.setShape(BackgroundShape.ARC);        // solid or arc
        pane.setBackground(bkg);

        // Fetch current user and today's spending
        User currentUser = authenticationService.getCurrentUser();
        double dailySpendingLimit = currentUser.getDailySpendingLimit();
        double todaySpending = expenseService.getTodaySpending(currentUser);

        // Set up the solid gauge chart
        conf.getChart().setType(ChartType.SOLIDGAUGE);

        YAxis yAxis = new YAxis();
        yAxis.setMin(0);
        yAxis.setMax(dailySpendingLimit);
        yAxis.setTitle("Spending");
        yAxis.getLabels().setFormat("{value}");
        conf.addyAxis(yAxis);

        PlotOptionsSolidgauge plotOptions = new PlotOptionsSolidgauge();
        DataSeries series = new DataSeries();
        DataSeriesItem item = new DataSeriesItem("Today",todaySpending);
        series.add(item);
        conf.setSeries(series);

        // Set color based on spending
        if (todaySpending < dailySpendingLimit * 0.9) {
            item.setColor(new SolidColor("#00FF00"));
        } else if (todaySpending <= dailySpendingLimit) {
            item.setColor(new SolidColor("#FFFF00"));
        } else {
            item.setColor(new SolidColor("#FF0000"));
        }

        widget.add(title, chart);
        return widget;
    }

    private Div createSpendingTrendsChartWidget() {
        Div widget = new Div();
        widget.addClassName("widget");

        H3 title = new H3("Spending Trends (Last 12 Days)");

        // Create the column chart
        Chart chart = new Chart(ChartType.COLUMN);
        Configuration conf = chart.getConfiguration();

        Tooltip tooltip = new Tooltip();
        tooltip.setShared(true);
        conf.setTooltip(tooltip);

        XAxis xAxis = new XAxis();
        xAxis.setCategories(getLast12Days().toArray(new String[0]));
        conf.addxAxis(xAxis);

        YAxis yAxis = new YAxis();
        yAxis.setTitle("Amount Spent");
        conf.addyAxis(yAxis);

        // Fetch expenses for the current user
        User currentUser = authenticationService.getCurrentUser();
        List<Expense> expenses = expenseService.getExpensesForUser(currentUser);

        // Group expenses by day and category, and sum the amounts
        Map<String, Map<String, Double>> dailyCategoryTotals = new HashMap<>();
        for (Expense expense : expenses) {
            String day = expense.getDate().toLocalDate().toString();
            String categoryName = expense.getCategory().getName();
            dailyCategoryTotals.putIfAbsent(day, new HashMap<>());
            Map<String, Double> categoryTotals = dailyCategoryTotals.get(day);
            categoryTotals.put(categoryName, categoryTotals.getOrDefault(categoryName, 0.0) + expense.getAmount());
        }

        // Add data to the series
        for (String category : getCategoryNames(expenses)) {
            DataSeries series = new DataSeries(category);
            for (String day : getLast12Days()) {
                double amount = dailyCategoryTotals.getOrDefault(day, new HashMap<>()).getOrDefault(category, 0.0);
                series.add(new DataSeriesItem(day, amount));
            }
            conf.addSeries(series);
        }

        widget.add(title, chart);
        widget.addClassName("widget-full-width");
        return widget;
    }

    private List<String> getLast12Days() {
        List<String> last12Days = new ArrayList<>();
        for (int i = 11; i >= 0; i--) {
            last12Days.add(LocalDate.now().minusDays(i).toString());
        }
        return last12Days;
    }

    private Set<String> getCategoryNames(List<Expense> expenses) {
        return expenses.stream().map(expense -> expense.getCategory().getName()).collect(Collectors.toSet());
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
