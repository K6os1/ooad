
package bw.ac.cse202.banking.model;

public class InvestmentAccount extends Account
        implements InterestCalculator, Withdrawable {

    private static final double INTEREST_RATE = 0.05;
    private static final double MINIMUM_BALANCE = 500.00;

    public InvestmentAccount(int id, int customerId,
                             String accountNumber, double balance,
                             String branch) {

        super(id, customerId, accountNumber, balance, branch);

        if (balance < MINIMUM_BALANCE) {
            throw new IllegalArgumentException(
                "Investment account requires at least BWP 500."
            );
        }
    }

    @Override
    public double calculateInterest() {
        return balance * INTEREST_RATE;
    }

    @Override
    public void payInterest() {
        if (state == AccountState.ACTIVE) {
            balance += calculateInterest();
        }
    }

    @Override
    public boolean withdraw(double amount) {

        if (state != AccountState.ACTIVE) {
            return false;
        }

        return debitBalance(amount);
    }

    @Override
    public String getAccountType() {
        return "Investment Account";
    }
}
