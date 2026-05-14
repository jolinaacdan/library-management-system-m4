import java.sql.*;

public class DatabaseSetup {
    public static void initialize() {
        String createMembers = """
            CREATE TABLE IF NOT EXISTS members (
                library_card_number TEXT PRIMARY KEY,
                name TEXT NOT NULL,
                contact_number TEXT,
                address TEXT
            )""";

        String createItems = """
            CREATE TABLE IF NOT EXISTS items (
                item_id TEXT PRIMARY KEY,
                title TEXT NOT NULL,
                category TEXT,
                is_available INTEGER DEFAULT 1
            )""";

        String createLoans = """
            CREATE TABLE IF NOT EXISTS loans (
                loan_id INTEGER PRIMARY KEY AUTOINCREMENT,
                library_card_number TEXT,
                item_id TEXT,
                borrow_date TEXT,
                due_date TEXT,
                is_returned INTEGER DEFAULT 0,
                FOREIGN KEY (library_card_number) REFERENCES members(library_card_number),
                FOREIGN KEY (item_id) REFERENCES items(item_id)
            )""";

        String createPayments = """
            CREATE TABLE IF NOT EXISTS payments (
                payment_id INTEGER PRIMARY KEY AUTOINCREMENT,
                library_card_number TEXT,
                payment_type TEXT,
                base_amount REAL,
                tax_amount REAL,
                discount_rate REAL,
                total_amount REAL,
                payment_date TEXT
            )""";

        String insertItems = """
            INSERT OR IGNORE INTO items VALUES
            ('ITEM-001','Introduction to Java','Programming',1),
            ('ITEM-002','Clean Code','Software Engineering',1),
            ('ITEM-003','Data Structures & Algorithms','Computer Science',1),
            ('ITEM-004','Object-Oriented Design','Programming',1),
            ('ITEM-005','The Pragmatic Programmer','Software Engineering',1)""";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createMembers);
            stmt.execute(createItems);
            stmt.execute(createLoans);
            stmt.execute(createPayments);
            stmt.execute(insertItems);
        } catch (SQLException e) {
            System.out.println("  Database setup error: " + e.getMessage());
        }
    }
}