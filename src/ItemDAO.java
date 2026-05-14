import java.sql.*;

public class ItemDAO {

    public void browseItems() {
        String sql = "SELECT * FROM items ORDER BY category";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("=====================================");
            System.out.println("          LIBRARY CATALOG            ");
            System.out.println("=====================================");
            String currentCat = "";
            boolean hasItems = false;
            while (rs.next()) {
                hasItems = true;
                String cat = rs.getString("category");
                if (!cat.equals(currentCat)) {
                    currentCat = cat;
                    System.out.println("\n  [ " + cat + " ]");
                }
                String status = rs.getInt("is_available") == 1 ? "Available" : "Borrowed";
                String mark = rs.getInt("is_available") == 1 ? "✓" : "✗";
                System.out.println("  " + mark + " [" + rs.getString("item_id") + "] "
                    + rs.getString("title") + " (" + status + ")");
            }
            if (!hasItems) System.out.println("  No items available.");
            System.out.println("\n=====================================");
        } catch (SQLException e) {
            System.out.println("  Error loading catalog.");
        }
    }

    public void searchItems(String keyword) {
        String sql = "SELECT * FROM items WHERE title LIKE ? OR category LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            System.out.println("=====================================");
            System.out.println("  Search Results for: \"" + keyword + "\"");
            System.out.println("=====================================");
            boolean found = false;
            while (rs.next()) {
                found = true;
                String status = rs.getInt("is_available") == 1 ? "Available" : "Borrowed";
                System.out.println("  [" + rs.getString("item_id") + "] "
                    + rs.getString("title") + " - " + rs.getString("category")
                    + " (" + status + ")");
            }
            if (!found) System.out.println("  No items found.");
            System.out.println("=====================================");
        } catch (SQLException e) {
            System.out.println("  Error searching items.");
        }
    }

    public LibraryItem findItem(String itemId) {
        String sql = "SELECT * FROM items WHERE item_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, itemId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                LibraryItem item = new LibraryItem(
                    rs.getString("item_id"),
                    rs.getString("title"),
                    rs.getString("category")
                );
                if (rs.getInt("is_available") == 0) item.setAvailable(false);
                return item;
            }
        } catch (SQLException e) {
            System.out.println("  Error finding item.");
        }
        return null;
    }

    public void updateAvailability(String itemId, boolean status) {
        String sql = "UPDATE items SET is_available = ? WHERE item_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, status ? 1 : 0);
            stmt.setString(2, itemId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("  Error updating item.");
        }
    }
}