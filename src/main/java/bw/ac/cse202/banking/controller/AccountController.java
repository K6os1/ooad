
package bw.ac.cse202.banking.controller;

import bw.ac.cse202.banking.model.*;

import bw.ac.cse202.banking.view.AccountView;

import java.sql.SQLException;
import java.util.List;

public class AccountController {

    private final AccountView view;
    private final BankingController bankingController;

    public AccountController(AccountView view) {

        this.view = view;
        this.bankingController = new BankingController();

        // Connect the buttons to their actions
        view.getRegisterButton().setOnAction(
            event -> registerCustomer()
        );

        view.getOpenAccountButton().setOnAction(
            event -> openAccount()
        );

        view.getDepositButton().setOnAction(
            event -> depositMoney()
        );

        view.getWithdrawButton().setOnAction(
            event -> withdrawMoney()
        );

        view.getInterestButton().setOnAction(
            event -> payInterest()
        );

        view.getRefreshButton().setOnAction(
            event -> refreshInformation()
        );
    }

    // Register a customer
    private void registerCustomer() {

        try {

            if (view.getCustomerId().isBlank()
                    || view.getFirstName().isBlank()
                    || view.getSurname().isBlank()
                    || view.getAddress().isBlank()
                    || view.getPhone().isBlank()
                    || view.getEmploymentStatus() == null) {

                showMessage("Please complete all customer fields.");
                return;
            }

            int id = Integer.parseInt(view.getCustomerId());

            boolean employed =
                view.getEmploymentStatus().equals("Yes");

            Customer customer = new Customer(
                id,
                view.getFirstName(),
                view.getSurname(),
                view.getAddress(),
                view.getPhone(),
                employed
            );

            bankingController.registerCustomer(customer);

            showMessage(
                "Customer registered: " + customer.getFullName()
            );

            view.clearCustomerFields();

        } catch (NumberFormatException e) {

            showMessage("Customer ID must be a valid number.");

        } catch (SQLException e) {

            showMessage("Database error: " + e.getMessage());

        } catch (Exception e) {

            showMessage("Error: " + e.getMessage());
        }
    }

    // Open a bank account
    private void openAccount() {

        try {

            if (view.getAccountCustomerId().isBlank()
                    || view.getAccountNumber().isBlank()
                    || view.getAccountType() == null
                    || view.getOpeningBalance().isBlank()
                    || view.getBranch().isBlank()) {

                showMessage("Please complete all required account fields.");
                return;
            }

            int customerId =
                Integer.parseInt(view.getAccountCustomerId());

            double balance =
                Double.parseDouble(view.getOpeningBalance());

            if (balance < 0) {
                showMessage("Opening balance cannot be negative.");
                return;
            }

            Customer customer = findCustomer(customerId);

            if (customer == null) {
                showMessage("Customer ID was not found.");
                return;
            }

            int accountId = getNextAccountId();

            String accountNumber = view.getAccountNumber();
            String branch = view.getBranch();

            switch (view.getAccountType()) {

                case "Savings Account":

                    bankingController.openSavingsAccount(
                        accountId,
                        customer,
                        accountNumber,
                        balance,
                        branch
                    );

                    break;

                case "Investment Account":

                    bankingController.openInvestmentAccount(
                        accountId,
                        customer,
                        accountNumber,
                        balance,
                        branch
                    );

                    break;

                case "Cheque Account":

                    bankingController.openChequeAccount(
                        accountId,
                        customer,
                        accountNumber,
                        balance,
                        branch,
                        view.getEmployerName(),
                        view.getEmployerAddress()
                    );

                    break;

                default:

                    showMessage("Invalid account type.");
                    return;
            }

            showMessage(
                view.getAccountType()
                + " opened successfully. Account number: "
                + accountNumber
            );

        } catch (NumberFormatException e) {

            showMessage(
                "Customer ID must be a whole number and balance must be numeric."
            );

        } catch (SQLException e) {

            showMessage("Database error: " + e.getMessage());

        } catch (Exception e) {

            showMessage("Account opening failed: " + e.getMessage());
        }
    }

    // Deposit money
    private void depositMoney() {

        try {

            Account account = findAccount(
                view.getTransactionAccount()
            );

            if (account == null) {
                showMessage("Account number was not found.");
                return;
            }

            double amount = Double.parseDouble(
                view.getTransactionAmount()
            );

            bankingController.deposit(account, amount);

            showMessage(
                "Deposit successful. New balance: BWP "
                + account.getBalance()
            );

        } catch (NumberFormatException e) {

            showMessage("Enter a valid deposit amount.");

        } catch (SQLException e) {

            showMessage("Database error: " + e.getMessage());

        } catch (Exception e) {

            showMessage("Deposit failed: " + e.getMessage());
        }
    }

    // Withdraw money
    private void withdrawMoney() {

        try {

            Account account = findAccount(
                view.getTransactionAccount()
            );

            if (account == null) {
                showMessage("Account number was not found.");
                return;
            }

            double amount = Double.parseDouble(
                view.getTransactionAmount()
            );

            bankingController.withdraw(account, amount);

            showMessage(
                "Withdrawal successful. New balance: BWP "
                + account.getBalance()
            );

        } catch (NumberFormatException e) {

            showMessage("Enter a valid withdrawal amount.");

        } catch (SQLException e) {

            showMessage("Database error: " + e.getMessage());

        } catch (Exception e) {

            showMessage("Withdrawal failed: " + e.getMessage());
        }
    }

    // Pay monthly interest
    private void payInterest() {

        try {

            List<Account> accounts =
                bankingController.getAccounts();

            bankingController.payMonthlyInterest(accounts);

            showMessage("Monthly interest has been processed.");

            refreshInformation();

        } catch (SQLException e) {

            showMessage("Database error: " + e.getMessage());

        } catch (Exception e) {

            showMessage("Interest payment failed: " + e.getMessage());
        }
    }

    // Display customers and accounts
    private void refreshInformation() {

        try {

            view.clearOutput();

            List<Customer> customers =
                bankingController.getCustomers();

            List<Account> accounts =
                bankingController.getAccounts();

            view.displayMessage("========== CUSTOMERS ==========");

            for (Customer customer : customers) {

                view.displayMessage(
                    "ID: " + customer.getId()
                    + " | Name: " + customer.getFullName()
                    + " | Phone: " + customer.getPhone()
                    + " | Employed: " + customer.isEmployed()
                );
            }

            view.displayMessage("");
            view.displayMessage("========== ACCOUNTS ==========");

            for (Account account : accounts) {

                view.displayMessage(
                    "Account Number: " + account.getAccountNumber()
                    + " | Type: " + account.getAccountType()
                    + " | Balance: BWP " + account.getBalance()
                    + " | State: " + account.getState()
                );
            }

            if (customers.isEmpty() && accounts.isEmpty()) {
                view.displayMessage("No records found.");
            }

        } catch (SQLException e) {

            showMessage("Could not retrieve information: " + e.getMessage());
        }
    }

    // Find a customer using the customer ID
    private Customer findCustomer(int customerId)
            throws SQLException {

        for (Customer customer : bankingController.getCustomers()) {

            if (customer.getId() == customerId) {
                return customer;
            }
        }

        return null;
    }

    // Find an account using its account number
    private Account findAccount(String accountNumber)
            throws SQLException {

        if (accountNumber == null || accountNumber.isBlank()) {
            return null;
        }

        for (Account account : bankingController.getAccounts()) {

            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }

        return null;
    }

    // Generate the next account ID
    private int getNextAccountId() throws SQLException {

        int highestId = 0;

        for (Account account : bankingController.getAccounts()) {

            if (account.getId() > highestId) {
                highestId = account.getId();
            }
        }

        return highestId + 1;
    }

    private void showMessage(String message) {
        view.displayMessage(message);
    }
}
