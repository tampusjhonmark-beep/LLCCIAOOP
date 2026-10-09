import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentDAO {

    public static class DocumentItem {
        private int id;
        private String title, category, description, filePath, documentUrl;

        public DocumentItem(int id,String title,String category,String description,
                            String filePath,String documentUrl) {
            this.id=id; this.title=title; this.category=category; this.description=description;
            this.filePath=filePath; this.documentUrl=documentUrl;
        }

        public int getId(){return id;}
        public String getTitle(){return title;}
        public String getCategory(){return category;}
        public String getDescription(){return description;}
        public String getFilePath(){return filePath;}
        public String getDocumentUrl(){return documentUrl;}
        @Override public String toString(){return title;}
    }

    public List<DocumentItem> getAllDocuments() {
        List<DocumentItem> list=new ArrayList<>();
        String sql="SELECT id,title,category,description,file_path,document_url " +
                   "FROM documents ORDER BY id";
        try(Connection c=DatabaseConnection.getConnection();
            PreparedStatement s=c.prepareStatement(sql);
            ResultSet r=s.executeQuery()) {
            while(r.next()) {
                list.add(new DocumentItem(r.getInt("id"),r.getString("title"),
                    r.getString("category"),r.getString("description"),
                    r.getString("file_path"),r.getString("document_url")));
            }
        } catch(SQLException e) {
            System.out.println("Error loading documents: "+e.getMessage());
        }
        return list;
    }
}
