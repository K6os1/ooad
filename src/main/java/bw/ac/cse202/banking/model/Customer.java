
package bw.ac.cse202.banking.model;

public class Customer {

    private int id;
    private String firstName;
    private String surname;
    private String address;
    private String phone;
    private boolean employed;

    public Customer(int id, String firstName, String surname,
                    String address, String phone, boolean employed) {

        this.id = id;
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
        this.phone = phone;
        this.employed = employed;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isEmployed() {
        return employed;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFullName() {
        return firstName + " " + surname;
    }
}
