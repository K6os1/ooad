
package bw.ac.cse202.banking.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String URL = "jdbc:sqlite:banking.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() {

        String customersTable = """
            CREATE TABLE IF NOT EXISTS customers (
                id INTEGER PRIMARY KEY,
                first_name TEXT NOT NULL,
                surname TEXT NOT NULL,
                address TEXT NOT NULL,
                phone TEXT NOT NULL,
                employed INTEGER NOT NULL
            )
            """;

        String accountsTable = """
            CREATE TABLE IF NOT EXISTS accounts (
                id INTEGER PRIMARY KEY,
                customer_id INTEGER NOT NULL,
                account_number TEXT NOT NULL UNIQUE,
                account_type TEXT NOT NULL,
                balance REAL NOT NULL,
                branch TEXT NOT NULL,
                state TEXT NOT NULL,
                employer_name TEXT,
                employer_address TEXT,
                FOREIGN KEY (customer_id) REFERENCES customers(id)
            )
            """;

        String transactionsTable = """
            CREATE TABLE IF NOT EXISTS transactions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                account_number TEXT NOT NULL,
                transaction_type TEXT NOT NULL,
                amount REAL NOT NULL,
                transaction_date TEXT DEFAULT CURRENT_TIMESTAMP
            )
            """;

        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {

            statement.execute(customersTable);
            statement.execute(accountsTable);
            statement.execute(transactionsTable);

            System.out.println("Database initialized successfully.");

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}
