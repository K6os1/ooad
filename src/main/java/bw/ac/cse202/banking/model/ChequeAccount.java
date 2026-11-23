
package bw.ac.cse202.banking.model;

public class ChequeAccount extends Account
        implements Withdrawable {

    private String employerName;
    private String employerAddress;

    public ChequeAccount(int id, int customerId,
                         String accountNumber, double balance,
                         String branch, Customer customer,
                         String employerName, String employerAddress) {

        super(id, customerId, accountNumber, balance, branch);

        if (!customer.isEmployed()) {
            throw new IllegalArgumentException(
                "Only employed customers can open cheque accounts."
            );
        }

        if (employerName == null || employerName.trim().isEmpty()
                || employerAddress == null
                || employerAddress.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Employer name and address are required."
            );
        }

        this.employerName = employerName;
        this.employerAddress = employerAddress;
    }

    @Override
    public boolean withdraw(double amount) {

        if (state != AccountState.ACTIVE) {
            return false;
        }

        return debitBalance(amount);
    }

    public String getEmployerName() {
        return employerName;
    }

    public String getEmployerAddress() {
        return employerAddress;
    }

    @Override
    public String getAccountType() {
        return "Cheque Account";
    }
}
