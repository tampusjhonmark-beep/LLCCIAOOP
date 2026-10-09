import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChatbotKnowledgeDAO {

    public static class Knowledge {
        private int id;
        private String keyword, response, category;

        public Knowledge(int id,String keyword,String response,String category) {
            this.id=id; this.keyword=keyword; this.response=response; this.category=category;
        }

        public int getId(){return id;}
        public String getKeyword(){return keyword;}
        public String getResponse(){return response;}
        public String getCategory(){return category;}
    }

    public List<Knowledge> getAllKnowledge() {
        List<Knowledge> list=new ArrayList<>();
        String sql="SELECT id,keyword,response,category FROM chatbot_knowledge ORDER BY id";
        try(Connection c=DatabaseConnection.getConnection();
            PreparedStatement s=c.prepareStatement(sql);
            ResultSet r=s.executeQuery()) {
            while(r.next()) {
                list.add(new Knowledge(r.getInt("id"),r.getString("keyword"),
                    r.getString("response"),r.getString("category")));
            }
        } catch(SQLException e) {
            System.out.println("Error loading chatbot knowledge: "+e.getMessage());
        }
        return list;
    }

    public String findResponse(String message) {
        if(message==null || message.trim().isEmpty()) return null;
        String lower=message.toLowerCase();

        for(Knowledge item : getAllKnowledge()) {
            String[] keywords=item.getKeyword().toLowerCase().split("[,;|]");
            for(String keyword:keywords) {
                String k=keyword.trim();
                if(!k.isEmpty() && lower.contains(k)) return item.getResponse();
            }
        }
        return null;
    }
}
