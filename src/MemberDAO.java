import java.sql.*;

public class MemberDAO {

    public Member registerMember(String name, String contact, String address) {
        Member member = new Member(name, contact, address);
        String sql = "INSERT INTO members VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, member.getCardNumber());
            stmt.setString(2, member.getName());
            stmt.setString(3, contact);
            stmt.setString(4, address);
            stmt.executeUpdate();
            System.out.println("-------------------------------------");
            System.out.println("  Registration Successful!");
            System.out.println("  Welcome, " + name + "!");
            System.out.println("  Card No: " + member.getCardNumber());
            System.out.println("  Save this number to borrow items!");
            System.out.println("-------------------------------------");
        } catch (SQLException e) {
            System.out.println("  Registration failed. Please check your information.");
        }
        return member;
    }

    public Member findMember(String cardNumber) {
        String sql = "SELECT * FROM members WHERE library_card_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cardNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Member(
                    rs.getString("name"),
                    rs.getString("contact_number"),
                    rs.getString("address"),
                    rs.getString("library_card_number")
                );
            }
        } catch (SQLException e) {
            System.out.println("  Error finding member.");
        }
        return null;
    }
}