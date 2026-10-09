import java.sql.Connection;

public class DatabaseTest {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("       LLCIA DATABASE TEST       ");
        System.out.println("=================================");

        try {

            Connection connection =
                    DatabaseConnection.getConnection();

            System.out.println("SUCCESS!");
            System.out.println("---------------------------------");
            System.out.println("Connected to: llcia_db");
            System.out.println("MySQL connection is working!");
            System.out.println("---------------------------------");

            connection.close();

        } catch (Exception e) {

            System.out.println("DATABASE CONNECTION FAILED!");
            System.out.println("---------------------------------");
            System.out.println("Error: " + e.getMessage());
            System.out.println("---------------------------------");

            e.printStackTrace();
        }
    }
}