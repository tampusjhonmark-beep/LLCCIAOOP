import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FAQDAO {

    public static class FAQ {
        private int id;
        private String question, answer, category;

        public FAQ(int id,String question,String answer,String category) {
            this.id=id; this.question=question; this.answer=answer; this.category=category;
        }

        public int getId(){return id;}
        public String getQuestion(){return question;}
        public String getAnswer(){return answer;}
        public String getCategory(){return category;}
    }

    public List<FAQ> getAllFAQs() {
        List<FAQ> list=new ArrayList<>();
        String sql="SELECT id,question,answer,category FROM faqs ORDER BY id";
        try(Connection c=DatabaseConnection.getConnection();
            PreparedStatement s=c.prepareStatement(sql);
            ResultSet r=s.executeQuery()) {
            while(r.next()) {
                list.add(new FAQ(r.getInt("id"),r.getString("question"),
                    r.getString("answer"),r.getString("category")));
            }
        } catch(SQLException e) {
            System.out.println("Error loading FAQs: "+e.getMessage());
        }
        return list;
    }
}
