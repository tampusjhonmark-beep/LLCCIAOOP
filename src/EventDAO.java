import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {

    public static class EventItem {
        private int id;
        private String title, location, category, description, status;
        private Date eventDate;
        private Time eventTime;

        public EventItem(int id, String title, Date eventDate, Time eventTime,
                         String location, String category, String description, String status) {
            this.id=id; this.title=title; this.eventDate=eventDate; this.eventTime=eventTime;
            this.location=location; this.category=category; this.description=description; this.status=status;
        }

        public int getId(){return id;}
        public String getTitle(){return title;}
        public Date getEventDate(){return eventDate;}
        public Time getEventTime(){return eventTime;}
        public String getLocation(){return location;}
        public String getCategory(){return category;}
        public String getDescription(){return description;}
        public String getStatus(){return status;}
        @Override public String toString(){return title;}
    }

    public List<EventItem> getAllEvents() {
        List<EventItem> list = new ArrayList<>();
        String sql = "SELECT id,title,event_date,event_time,location,category,description,status " +
                     "FROM events ORDER BY event_date,event_time,id";
        try (Connection c=DatabaseConnection.getConnection();
             PreparedStatement s=c.prepareStatement(sql);
             ResultSet r=s.executeQuery()) {
            while(r.next()) {
                list.add(new EventItem(r.getInt("id"),r.getString("title"),
                    r.getDate("event_date"),r.getTime("event_time"),r.getString("location"),
                    r.getString("category"),r.getString("description"),r.getString("status")));
            }
        } catch(SQLException e) {
            System.out.println("Error loading events: " + e.getMessage());
        }
        return list;
    }
}
