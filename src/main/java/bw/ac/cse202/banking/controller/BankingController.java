
package bw.ac.cse202.banking.controller;

import bw.ac.cse202.banking.dao.BankingDAO;
import bw.ac.cse202.banking.dao.Database;
import bw.ac.cse202.banking.model.*;

import java.sql.SQLException;
import java.util.List;

public class BankingController {

    private final BankingDAO dao;

    public BankingController() {
        dao = new BankingDAO();
        Database.initializeDatabase();
    }

    // Register a new customer
    public void registerCustomer(Customer customer)
            throws SQLException {

        dao.saveCustomer(customer);

        System.out.println(
            "Customer registered successfully."
        );
    }

    // Open a savings account
    public SavingsAccount openSavingsAccount(
            int id,
            Customer customer,
            String accountNumber,
            double openingBalance,
            String branch) throws SQLException {

        SavingsAccount account = new SavingsAccount(
            id,
            customer.getId(),
            accountNumber,
            openingBalance,
            branch
        );

        dao.saveAccount(account);

        if (openingBalance > 0) {
            dao.recordTransaction(
                accountNumber,
                "OPENING DEPOSIT",
                openingBalance
            );
        }

        return account;
    }

    // Open an investment account
    public InvestmentAccount openInvestmentAccount(
            int id,
            Customer customer,
            String accountNumber,
            double openingBalance,
            String branch) throws SQLException {

        if (openingBalance < 500) {
            throw new IllegalArgumentException(
                "Minimum investment opening balance is BWP 500."
            );
        }

        InvestmentAccount account = new InvestmentAccount(
            id,
            customer.getId(),
            accountNumber,
            openingBalance,
            branch
        );

        dao.saveAccount(account);

        dao.recordTransaction(
            accountNumber,
            "OPENING DEPOSIT",
            openingBalance
        );

        return account;
    }

    // Open a cheque account
    public ChequeAccount openChequeAccount(
            int id,
            Customer customer,
            String accountNumber,
            double openingBalance,
            String branch,
            String employerName,
            String employerAddress) throws SQLException {

        ChequeAccount account = new ChequeAccount(
            id,
            customer.getId(),
            accountNumber,
            openingBalance,
            branch,
            customer,
            employerName,
            employerAddress
        );

        dao.saveAccount(account);

        if (openingBalance > 0) {
            dao.recordTransaction(
                accountNumber,
                "OPENING DEPOSIT",
                openingBalance
            );
        }

        return account;
    }

    // Deposit money
    public void deposit(Account account, double amount)
            throws SQLException {

        account.deposit(amount);

        dao.updateAccount(account);

        dao.recordTransaction(
            account.getAccountNumber(),
            "DEPOSIT",
            amount
        );

        System.out.println("Deposit successful.");
    }

    // Withdraw money
    public void withdraw(Account account, double amount)
            throws SQLException {

        if (!(account instanceof Withdrawable)) {
            throw new IllegalArgumentException(
                "Withdrawals are not allowed on this account."
            );
        }

        Withdrawable withdrawable = (Withdrawable) account;

        if (!withdrawable.withdraw(amount)) {
            throw new IllegalArgumentException(
                "Withdrawal rejected. Check your balance and account status."
            );
        }

        dao.updateAccount(account);

        dao.recordTransaction(
            account.getAccountNumber(),
            "WITHDRAWAL",
            amount
        );

        System.out.println("Withdrawal successful.");
    }

    // Pay monthly interest
    public void payMonthlyInterest(List<Account> accounts)
            throws SQLException {

        for (Account account : accounts) {

            if (account instanceof InterestCalculator) {

                double interest =
                    ((InterestCalculator) account).calculateInterest();

                account.applyMonthlyInterest();

                dao.updateAccount(account);

                dao.recordTransaction(
                    account.getAccountNumber(),
                    "MONTHLY INTEREST",
                    interest
                );
            }
        }

        System.out.println(
            "Monthly interest payment completed."
        );
    }

    // View all customers
    public List<Customer> getCustomers()
            throws SQLException {

        return dao.getAllCustomers();
    }

    // View all accounts
    public List<Account> getAccounts()
            throws SQLException {

        return dao.getAllAccounts();
    }

    // View transactions for an account
    public List<String> getTransactions(String accountNumber)
            throws SQLException {

        return dao.getTransactions(accountNumber);
    }

    // Change an account's state
    public void changeAccountState(
            Account account,
            AccountState newState) throws SQLException {

        account.changeState(newState);

        dao.updateAccount(account);
    }
}
