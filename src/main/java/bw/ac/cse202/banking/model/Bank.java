
package bw.ac.cse202.banking.model;

import java.util.ArrayList;
import java.util.List;

public class Bank {

    private String bankName;
    private String branch;
    private List<Customer> customers;

    public Bank(String bankName, String branch) {
        this.bankName = bankName;
        this.branch = branch;
        this.customers = new ArrayList<>();
    }

    public void registerCustomer(Customer customer) {
        customers.add(customer);
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public String getBankName() {
        return bankName;
    }

    public String getBranch() {
        return branch;
    }
}
