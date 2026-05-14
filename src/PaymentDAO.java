import java.sql.*;
import java.time.LocalDate;

public class PaymentDAO {

    public static void savePayment(String cardNumber, String type,
                                    double base, double tax,
                                    double discountRate, double total) {
        String sql = """
            INSERT INTO payments
            (library_card_number, payment_type, base_amount,
             tax_amount, discount_rate, total_amount, payment_date)
            VALUES (?, ?, ?, ?, ?, ?, ?)""";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cardNumber);
            stmt.setString(2, type);
            stmt.setDouble(3, base);
            stmt.setDouble(4, tax);
            stmt.setDouble(5, discountRate);
            stmt.setDouble(6, total);
            stmt.setString(7, LocalDate.now().toString());
            stmt.executeUpdate();
            System.out.println("  Payment record saved.");
        } catch (SQLException e) {
            System.out.println("  Error saving payment: " + e.getMessage());
        }
    }

    public static void showPaymentHistory(String cardNumber) {
        String sql = "SELECT * FROM payments WHERE library_card_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cardNumber);
            ResultSet rs = stmt.executeQuery();
            System.out.println("=====================================");
            System.out.println("        PAYMENT HISTORY              ");
            System.out.println("=====================================");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("  Type    : " + rs.getString("payment_type"));
                System.out.println("  Base    : PHP " + String.format("%.2f", rs.getDouble("base_amount")));
                System.out.println("  Tax     : PHP " + String.format("%.2f", rs.getDouble("tax_amount")));
                System.out.println("  Discount: " + rs.getDouble("discount_rate") + "%");
                System.out.println("  Total   : PHP " + String.format("%.2f", rs.getDouble("total_amount")));
                System.out.println("  Date    : " + rs.getString("payment_date"));
                System.out.println("  ------------------------------------");
            }
            if (!found) System.out.println("  No payment history found.");
            System.out.println("=====================================");
        } catch (SQLException e) {
            System.out.println("  Error loading history.");
        }
    }
}