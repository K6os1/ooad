
package bw.ac.cse202.banking.model;

public class SavingsAccount extends Account
        implements InterestCalculator {

    private static final double INTEREST_RATE = 0.0005;

    public SavingsAccount(int id, int customerId,
                          String accountNumber, double balance,
                          String branch) {

        super(id, customerId, accountNumber, balance, branch);
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
    public String getAccountType() {
        return "Savings Account";
    }
}
