
package bw.ac.cse202.banking.dao;

import bw.ac.cse202.banking.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankingDAO {

    // Save a new customer
    public void saveCustomer(Customer customer) throws SQLException {

        String sql = """
            INSERT INTO customers
            (id, first_name, surname, address, phone, employed)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (Connection connection = Database.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, customer.getId());
            statement.setString(2, customer.getFirstName());
            statement.setString(3, customer.getSurname());
            statement.setString(4, customer.getAddress());
            statement.setString(5, customer.getPhone());
            statement.setInt(6, customer.isEmployed() ? 1 : 0);

            statement.executeUpdate();
        }
    }

    // Save a bank account
    public void saveAccount(Account account) throws SQLException {

        String employerName = null;
        String employerAddress = null;

        if (account instanceof ChequeAccount) {
            ChequeAccount cheque = (ChequeAccount) account;
            employerName = cheque.getEmployerName();
            employerAddress = cheque.getEmployerAddress();
        }

        String sql = """
            INSERT INTO accounts
            (id, customer_id, account_number, account_type,
             balance, branch, state, employer_name, employer_address)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection connection = Database.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, account.getId());
            statement.setInt(2, account.getCustomerId());
            statement.setString(3, account.getAccountNumber());
            statement.setString(4, account.getAccountType());
            statement.setDouble(5, account.getBalance());
            statement.setString(6, account.getBranch());
            statement.setString(7, account.getState().name());
            statement.setString(8, employerName);
            statement.setString(9, employerAddress);

            statement.executeUpdate();
        }
    }

    // Update an account balance and state
    public void updateAccount(Account account) throws SQLException {

        String sql = """
            UPDATE accounts
            SET balance = ?, state = ?
            WHERE account_number = ?
            """;

        try (Connection connection = Database.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setDouble(1, account.getBalance());
            statement.setString(2, account.getState().name());
            statement.setString(3, account.getAccountNumber());

            statement.executeUpdate();
        }
    }

    // Record a transaction
    public void recordTransaction(String accountNumber,
                                  String transactionType,
                                  double amount) throws SQLException {

        String sql = """
            INSERT INTO transactions
            (account_number, transaction_type, amount)
            VALUES (?, ?, ?)
            """;

        try (Connection connection = Database.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, accountNumber);
            statement.setString(2, transactionType);
            statement.setDouble(3, amount);

            statement.executeUpdate();
        }
    }

    // Retrieve all customers
    public List<Customer> getAllCustomers() throws SQLException {

        List<Customer> customers = new ArrayList<>();

        String sql = "SELECT * FROM customers";

        try (Connection connection = Database.connect();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(sql)) {

            while (result.next()) {

                Customer customer = new Customer(
                    result.getInt("id"),
                    result.getString("first_name"),
                    result.getString("surname"),
                    result.getString("address"),
                    result.getString("phone"),
                    result.getInt("employed") == 1
                );

                customers.add(customer);
            }
        }

        return customers;
    }

    // Retrieve all accounts
    public List<Account> getAllAccounts() throws SQLException {

        List<Account> accounts = new ArrayList<>();

        String sql = "SELECT * FROM accounts";

        try (Connection connection = Database.connect();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(sql)) {

            while (result.next()) {

                int id = result.getInt("id");
                int customerId = result.getInt("customer_id");
                String accountNumber =
                    result.getString("account_number");
                double balance = result.getDouble("balance");
                String branch = result.getString("branch");
                String type = result.getString("account_type");

                Account account;

                if (type.equals("Savings Account")) {

                    account = new SavingsAccount(
                        id, customerId, accountNumber, balance, branch
                    );

                } else if (type.equals("Investment Account")) {

                    account = new InvestmentAccount(
                        id, customerId, accountNumber, balance, branch
                    );

                } else if (type.equals("Cheque Account")) {

                    Customer customer = findCustomer(
                        customerId
                    );

                    account = new ChequeAccount(
                        id,
                        customerId,
                        accountNumber,
                        balance,
                        branch,
                        customer,
                        result.getString("employer_name"),
                        result.getString("employer_address")
                    );

                } else {
                    continue;
                }

                account.changeState(
                    AccountState.valueOf(result.getString("state"))
                );

                accounts.add(account);
            }
        }

        return accounts;
    }

    // Find a customer by ID
    public Customer findCustomer(int customerId) throws SQLException {

        String sql = "SELECT * FROM customers WHERE id = ?";

        try (Connection connection = Database.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, customerId);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {

                    return new Customer(
                        result.getInt("id"),
                        result.getString("first_name"),
                        result.getString("surname"),
                        result.getString("address"),
                        result.getString("phone"),
                        result.getInt("employed") == 1
                    );
                }
            }
        }

        return null;
    }

    // Retrieve transaction history
    public List<String> getTransactions(String accountNumber)
            throws SQLException {

        List<String> transactions = new ArrayList<>();

        String sql = """
            SELECT * FROM transactions
            WHERE account_number = ?
            ORDER BY id DESC
            """;

        try (Connection connection = Database.connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, accountNumber);

            try (ResultSet result = statement.executeQuery()) {

                while (result.next()) {

                    String transaction =
                        result.getString("transaction_type")
                        + " | BWP "
                        + result.getDouble("amount")
                        + " | "
                        + result.getString("transaction_date");

                    transactions.add(transaction);
                }
            }
        }

        return transactions;
    }
}
