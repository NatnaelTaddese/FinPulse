package org.vaadin.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.model.Expense;
import org.vaadin.example.model.User;
import org.vaadin.example.service.AuthenticationService;
import org.vaadin.example.service.ExpenseService;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Route(value = "history", layout = MainLayout.class)
@PermitAll
public class HistoryView extends VerticalLayout {

    private final AuthenticationService authenticationService;
    private final ExpenseService expenseService;

    private Grid<Expense> expenseGrid;

    @Autowired
    public HistoryView(AuthenticationService authenticationService, ExpenseService expenseService) {
        this.authenticationService = authenticationService;
        this.expenseService = expenseService;

        initLayout();
        loadExpenseHistory();
    }

    private void initLayout() {
        H1 title = new H1("Expense History");
        title.addClassName("history-title");

        expenseGrid = new Grid<>(Expense.class, false);
        expenseGrid.addColumn(expense -> expense.getCategory().getName()).setHeader("Category");
        expenseGrid.addColumn(expense -> expense.getDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))).setHeader("Date");
        expenseGrid.addColumn(expense -> expense.getTime().format(DateTimeFormatter.ofPattern("HH:mm"))).setHeader("Time");
        expenseGrid.addColumn(Expense::getAmount).setHeader("Amount");
        expenseGrid.addColumn(new ComponentRenderer<>(this::createReceiptLink)).setHeader("Receipt");

        add(title, expenseGrid);
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }

    private void loadExpenseHistory() {
        User currentUser = authenticationService.getCurrentUser();
        List<Expense> userExpenses = expenseService.getExpensesByUser(currentUser);

        DataProvider<Expense, ?> dataProvider = DataProvider.fromCallbacks(
                query -> userExpenses.stream()
                        .skip(query.getOffset())
                        .limit(query.getLimit()),
                query -> userExpenses.size()
        );

        expenseGrid.setDataProvider(dataProvider);
    }

    private Button createReceiptLink(Expense expense) {
        //        if (expense.getReceiptAttachment() != null) {
//            Button receiptButton = new Button("View Receipt");
//            receiptButton.addClickListener(event -> {
//                // Implement logic to download or display the receipt attachment
//            });
//            return receiptButton;
//        }
        return new Button("No Receipt");
    }
}