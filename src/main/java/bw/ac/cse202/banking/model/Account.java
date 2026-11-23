
package bw.ac.cse202.banking.model;

public abstract class Account {

    protected int id;
    protected int customerId;
    protected String accountNumber;
    protected double balance;
    protected String branch;
    protected AccountState state;

    public Account(int id, int customerId, String accountNumber,
                   double balance, String branch) {

        this.id = id;
        this.customerId = customerId;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.branch = branch;
        this.state = AccountState.ACTIVE;
    }

    public void deposit(double amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException(
                "Deposit amount must be greater than zero."
            );
        }

        if (state != AccountState.ACTIVE) {
            throw new IllegalStateException(
                "This account is not active."
            );
        }

        balance += amount;
    }

    protected boolean debitBalance(double amount) {

        if (amount <= 0 || amount > balance) {
            return false;
        }

        balance -= amount;
        return true;
    }

    public void applyMonthlyInterest() {
        if (this instanceof InterestCalculator) {
            ((InterestCalculator) this).payInterest();
        }
    }

    public void changeState(AccountState newState) {
        this.state = newState;
    }

    public int getId() {
        return id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public String getBranch() {
        return branch;
    }

    public AccountState getState() {
        return state;
    }

    public abstract String getAccountType();
}
