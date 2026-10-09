import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CampusInfoDAO {

    public static class CampusInfo {
        private int id;
        private String title, description, location, phone, email, website;

        public CampusInfo(int id,String title,String description,String location,
                          String phone,String email,String website) {
            this.id=id; this.title=title; this.description=description; this.location=location;
            this.phone=phone; this.email=email; this.website=website;
        }

        public int getId(){return id;}
        public String getTitle(){return title;}
        public String getDescription(){return description;}
        public String getLocation(){return location;}
        public String getPhone(){return phone;}
        public String getEmail(){return email;}
        public String getWebsite(){return website;}
    }

    public List<CampusInfo> getAllCampusInfo() {
        List<CampusInfo> list=new ArrayList<>();
        String sql="SELECT id,title,description,location,phone,email,website " +
                   "FROM campus_info ORDER BY id";
        try(Connection c=DatabaseConnection.getConnection();
            PreparedStatement s=c.prepareStatement(sql);
            ResultSet r=s.executeQuery()) {
            while(r.next()) {
                list.add(new CampusInfo(r.getInt("id"),r.getString("title"),
                    r.getString("description"),r.getString("location"),
                    r.getString("phone"),r.getString("email"),r.getString("website")));
            }
        } catch(SQLException e) {
            System.out.println("Error loading campus information: "+e.getMessage());
        }
        return list;
    }
}
