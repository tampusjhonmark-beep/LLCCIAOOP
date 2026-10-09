import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CalendarEventDAO {

    public static class CalendarEvent {
        private int id;
        private String title, description, category;
        private Date eventDate;

        public CalendarEvent(int id,String title,Date eventDate,String description,String category) {
            this.id=id; this.title=title; this.eventDate=eventDate;
            this.description=description; this.category=category;
        }

        public int getId(){return id;}
        public String getTitle(){return title;}
        public Date getEventDate(){return eventDate;}
        public String getDescription(){return description;}
        public String getCategory(){return category;}
        @Override public String toString(){return title;}
    }

    public List<CalendarEvent> getAllCalendarEvents() {
        List<CalendarEvent> list=new ArrayList<>();
        String sql="SELECT id,title,event_date,description,category " +
                   "FROM calendar_events ORDER BY event_date,id";
        try(Connection c=DatabaseConnection.getConnection();
            PreparedStatement s=c.prepareStatement(sql);
            ResultSet r=s.executeQuery()) {
            while(r.next()) {
                list.add(new CalendarEvent(r.getInt("id"),r.getString("title"),
                    r.getDate("event_date"),r.getString("description"),r.getString("category")));
            }
        } catch(SQLException e) {
            System.out.println("Error loading calendar events: "+e.getMessage());
        }
        return list;
    }
}
