
package bw.ac.cse202.banking.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class AccountView extends BorderPane {

    // Customer fields
    private TextField customerIdField;
    private TextField firstNameField;
    private TextField surnameField;
    private TextField addressField;
    private TextField phoneField;
    private ComboBox<String> employmentBox;

    // Account fields
    private TextField accountCustomerIdField;
    private TextField accountNumberField;
    private TextField openingBalanceField;
    private TextField branchField;
    private ComboBox<String> accountTypeBox;
    private TextField employerNameField;
    private TextField employerAddressField;

    // Transaction fields
    private TextField transactionAccountField;
    private TextField transactionAmountField;

    // Buttons
    private Button registerButton;
    private Button openAccountButton;
    private Button depositButton;
    private Button withdrawButton;
    private Button interestButton;
    private Button refreshButton;

    // Output
    private TextArea outputArea;

    public AccountView() {

        setPadding(new Insets(20));

        Label heading = new Label("BANKING MANAGEMENT SYSTEM");

        heading.setStyle(
            "-fx-font-size: 25px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #17365D;"
        );

        Label subtitle = new Label(
            "Customer and Account Management"
        );

        VBox header = new VBox(8, heading, subtitle);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(10, 10, 25, 10));

        setTop(header);

        TabPane tabPane = new TabPane();

        tabPane.getTabs().addAll(
            createCustomerTab(),
            createAccountTab(),
            createTransactionTab(),
            createOverviewTab()
        );

        setCenter(tabPane);
    }

    private Tab createCustomerTab() {

        GridPane grid = createGrid();

        customerIdField = new TextField();
        firstNameField = new TextField();
        surnameField = new TextField();
        addressField = new TextField();
        phoneField = new TextField();

        employmentBox = new ComboBox<>();
        employmentBox.getItems().addAll("Yes", "No");
        employmentBox.setPromptText("Select employment status");

        registerButton = new Button("Register Customer");

        grid.add(new Label("Customer ID:"), 0, 0);
        grid.add(customerIdField, 1, 0);

        grid.add(new Label("First Name:"), 0, 1);
        grid.add(firstNameField, 1, 1);

        grid.add(new Label("Surname:"), 0, 2);
        grid.add(surnameField, 1, 2);

        grid.add(new Label("Address:"), 0, 3);
        grid.add(addressField, 1, 3);

        grid.add(new Label("Phone:"), 0, 4);
        grid.add(phoneField, 1, 4);

        grid.add(new Label("Employed:"), 0, 5);
        grid.add(employmentBox, 1, 5);

        grid.add(registerButton, 1, 6);

        return createTab("Register Customer", grid);
    }

    private Tab createAccountTab() {

        GridPane grid = createGrid();

        accountCustomerIdField = new TextField();
        accountNumberField = new TextField();
        openingBalanceField = new TextField();
        branchField = new TextField();
        employerNameField = new TextField();
        employerAddressField = new TextField();

        accountTypeBox = new ComboBox<>();
        accountTypeBox.getItems().addAll(
            "Savings Account",
            "Investment Account",
            "Cheque Account"
        );

        accountTypeBox.setPromptText("Select account type");

        openAccountButton = new Button("Open Account");

        grid.add(new Label("Customer ID:"), 0, 0);
        grid.add(accountCustomerIdField, 1, 0);

        grid.add(new Label("Account Number:"), 0, 1);
        grid.add(accountNumberField, 1, 1);

        grid.add(new Label("Account Type:"), 0, 2);
        grid.add(accountTypeBox, 1, 2);

        grid.add(new Label("Opening Balance (BWP):"), 0, 3);
        grid.add(openingBalanceField, 1, 3);

        grid.add(new Label("Branch:"), 0, 4);
        grid.add(branchField, 1, 4);

        grid.add(new Label("Employer Name:"), 0, 5);
        grid.add(employerNameField, 1, 5);

        grid.add(new Label("Employer Address:"), 0, 6);
        grid.add(employerAddressField, 1, 6);

        grid.add(openAccountButton, 1, 7);

        Label note = new Label(
            "Investment accounts require a minimum opening balance of BWP 500. " +
            "Cheque accounts require an employed customer and employer details."
        );

        note.setWrapText(true);

        VBox content = new VBox(15, grid, note);
        content.setPadding(new Insets(10));

        return createTab("Open Account", content);
    }

    private Tab createTransactionTab() {

        GridPane grid = createGrid();

        transactionAccountField = new TextField();
        transactionAmountField = new TextField();

        depositButton = new Button("Deposit");
        withdrawButton = new Button("Withdraw");

        grid.add(new Label("Account Number:"), 0, 0);
        grid.add(transactionAccountField, 1, 0);

        grid.add(new Label("Amount (BWP):"), 0, 1);
        grid.add(transactionAmountField, 1, 1);

        HBox buttons = new HBox(
            15, depositButton, withdrawButton
        );

        grid.add(buttons, 1, 2);

        return createTab("Transactions", grid);
    }

    private Tab createOverviewTab() {

        VBox content = new VBox(12);
        content.setPadding(new Insets(15));

        Label title = new Label("Banking System Information");

        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        refreshButton = new Button("Refresh Information");
        interestButton = new Button("Pay Monthly Interest");

        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setWrapText(true);
        outputArea.setPrefHeight(400);

        content.getChildren().addAll(
            title,
            refreshButton,
            interestButton,
            outputArea
        );

        return createTab("Overview", content);
    }

    private GridPane createGrid() {

        GridPane grid = new GridPane();

        grid.setPadding(new Insets(25));
        grid.setHgap(15);
        grid.setVgap(15);

        return grid;
    }

    private Tab createTab(String title, javafx.scene.Node content) {

        Tab tab = new Tab(title, content);
        tab.setClosable(false);

        return tab;
    }

    // Customer information getters

    public String getCustomerId() {
        return customerIdField.getText();
    }

    public String getFirstName() {
        return firstNameField.getText();
    }

    public String getSurname() {
        return surnameField.getText();
    }

    public String getAddress() {
        return addressField.getText();
    }

    public String getPhone() {
        return phoneField.getText();
    }

    public String getEmploymentStatus() {
        return employmentBox.getValue();
    }

    // Account information getters

    public String getAccountCustomerId() {
        return accountCustomerIdField.getText();
    }

    public String getAccountNumber() {
        return accountNumberField.getText();
    }

    public String getAccountType() {
        return accountTypeBox.getValue();
    }

    public String getOpeningBalance() {
        return openingBalanceField.getText();
    }

    public String getBranch() {
        return branchField.getText();
    }

    public String getEmployerName() {
        return employerNameField.getText();
    }

    public String getEmployerAddress() {
        return employerAddressField.getText();
    }

    // Transaction getters

    public String getTransactionAccount() {
        return transactionAccountField.getText();
    }

    public String getTransactionAmount() {
        return transactionAmountField.getText();
    }

    // Button getters

    public Button getRegisterButton() {
        return registerButton;
    }

    public Button getOpenAccountButton() {
        return openAccountButton;
    }

    public Button getDepositButton() {
        return depositButton;
    }

    public Button getWithdrawButton() {
        return withdrawButton;
    }

    public Button getInterestButton() {
        return interestButton;
    }

    public Button getRefreshButton() {
        return refreshButton;
    }

    // Display information

    public void displayMessage(String message) {
        outputArea.appendText(message + "\n");
    }

    public void clearOutput() {
        outputArea.clear();
    }

    public void clearCustomerFields() {
        customerIdField.clear();
        firstNameField.clear();
        surnameField.clear();
        addressField.clear();
        phoneField.clear();
        employmentBox.setValue(null);
    }
}
