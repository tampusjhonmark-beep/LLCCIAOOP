import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrganizationDAO {

    public static class Organization {
        private int id;
        private String name, category, description, contactEmail, contactPhone;

        public Organization(int id, String name, String category, String description,
                            String contactEmail, String contactPhone) {
            this.id = id; this.name = name; this.category = category;
            this.description = description; this.contactEmail = contactEmail;
            this.contactPhone = contactPhone;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getCategory() { return category; }
        public String getDescription() { return description; }
        public String getContactEmail() { return contactEmail; }
        public String getContactPhone() { return contactPhone; }

        @Override public String toString() { return name; }
    }

    public List<Organization> getAllOrganizations() {
        List<Organization> list = new ArrayList<>();
        String sql = "SELECT id,name,category,description,contact_email,contact_phone " +
                     "FROM organizations ORDER BY id";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql);
             ResultSet r = s.executeQuery()) {
            while (r.next()) {
                list.add(new Organization(
                    r.getInt("id"), r.getString("name"), r.getString("category"),
                    r.getString("description"), r.getString("contact_email"),
                    r.getString("contact_phone")));
            }
        } catch (SQLException e) {
            System.out.println("Error loading organizations: " + e.getMessage());
        }
        return list;
    }
}
