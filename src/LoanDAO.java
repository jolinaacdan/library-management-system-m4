import java.sql.*;
import java.time.LocalDate;

public class LoanDAO {

    public void borrowItem(Member member, String itemId) {
        ItemDAO itemDAO = new ItemDAO();
        LibraryItem item = itemDAO.findItem(itemId);
        if (item == null) { System.out.println("  Item not found."); return; }
        if (!item.isAvailable()) { System.out.println("  Item is currently unavailable."); return; }

        String borrowDate = LocalDate.now().toString();
        String dueDate = LocalDate.now().plusDays(7).toString();
        String sql = "INSERT INTO loans (library_card_number, item_id, borrow_date, due_date) VALUES (?,?,?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, member.getCardNumber());
            stmt.setString(2, itemId);
            stmt.setString(3, borrowDate);
            stmt.setString(4, dueDate);
            stmt.executeUpdate();
            itemDAO.updateAvailability(itemId, false);
            System.out.println("-------------------------------------");
            System.out.println("  Borrow Confirmed!");
            System.out.println("  Item  : " + item.getTitle());
            System.out.println("  Member: " + member.getName());
            System.out.println("  Due   : " + dueDate);
            System.out.println("-------------------------------------");
        } catch (SQLException e) {
            System.out.println("  Error processing borrow.");
        }
    }

    public void returnItem(Member member, String itemId) {
        String sql = "UPDATE loans SET is_returned = 1 WHERE library_card_number = ? AND item_id = ? AND is_returned = 0";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, member.getCardNumber());
            stmt.setString(2, itemId);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                new ItemDAO().updateAvailability(itemId, true);
                System.out.println("-------------------------------------");
                System.out.println("  Return Successful!");
                System.out.println("  Thank you for returning on time!");
                System.out.println("-------------------------------------");
            } else {
                System.out.println("  Item not found in your active loans.");
            }
        } catch (SQLException e) {
            System.out.println("  Error processing return.");
        }
    }
}