import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseDataTest {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("       LLCIA DATABASE DATA TEST       ");
        System.out.println("======================================");

        try {

            Connection connection =
                    DatabaseConnection.getConnection();

            System.out.println("\nDATABASE CONNECTION: SUCCESS");
            System.out.println("--------------------------------------");

            // =========================================
            // COURSES
            // =========================================

            System.out.println("\nCOURSES:");

            Statement courseStatement =
                    connection.createStatement();

            ResultSet courseResult =
                    courseStatement.executeQuery(
                            "SELECT id, code, name, college FROM courses"
                    );

            int courseCount = 0;

            while (courseResult.next()) {

                courseCount++;

                System.out.println(
                        courseResult.getInt("id")
                        + " | "
                        + courseResult.getString("code")
                        + " | "
                        + courseResult.getString("name")
                        + " | "
                        + courseResult.getString("college")
                );
            }

            System.out.println("Total courses: " + courseCount);

            courseResult.close();
            courseStatement.close();


            // =========================================
            // ORGANIZATIONS
            // =========================================

            System.out.println("\nORGANIZATIONS:");

            Statement organizationStatement =
                    connection.createStatement();

            ResultSet organizationResult =
                    organizationStatement.executeQuery(
                            "SELECT id, name, category FROM organizations"
                    );

            int organizationCount = 0;

            while (organizationResult.next()) {

                organizationCount++;

                System.out.println(
                        organizationResult.getInt("id")
                        + " | "
                        + organizationResult.getString("name")
                        + " | "
                        + organizationResult.getString("category")
                );
            }

            System.out.println(
                    "Total organizations: "
                    + organizationCount
            );

            organizationResult.close();
            organizationStatement.close();


            // =========================================
            // EVENTS
            // =========================================

            System.out.println("\nEVENTS:");

            Statement eventStatement =
                    connection.createStatement();

            ResultSet eventResult =
                    eventStatement.executeQuery(
                            "SELECT id, title, event_date, location FROM events"
                    );

            int eventCount = 0;

            while (eventResult.next()) {

                eventCount++;

                System.out.println(
                        eventResult.getInt("id")
                        + " | "
                        + eventResult.getString("title")
                        + " | "
                        + eventResult.getDate("event_date")
                        + " | "
                        + eventResult.getString("location")
                );
            }

            System.out.println(
                    "Total events: "
                    + eventCount
            );

            eventResult.close();
            eventStatement.close();


            // =========================================
            // OFFICES
            // =========================================

            System.out.println("\nOFFICES:");

            Statement officeStatement =
                    connection.createStatement();

            ResultSet officeResult =
                    officeStatement.executeQuery(
                            "SELECT id, office_name, location FROM offices"
                    );

            int officeCount = 0;

            while (officeResult.next()) {

                officeCount++;

                System.out.println(
                        officeResult.getInt("id")
                        + " | "
                        + officeResult.getString("office_name")
                        + " | "
                        + officeResult.getString("location")
                );
            }

            System.out.println(
                    "Total offices: "
                    + officeCount
            );

            officeResult.close();
            officeStatement.close();


            // =========================================
            // FAQ
            // =========================================

            System.out.println("\nFAQS:");

            Statement faqStatement =
                    connection.createStatement();

            ResultSet faqResult =
                    faqStatement.executeQuery(
                            "SELECT id, question, category FROM faqs"
                    );

            int faqCount = 0;

            while (faqResult.next()) {

                faqCount++;

                System.out.println(
                        faqResult.getInt("id")
                        + " | "
                        + faqResult.getString("question")
                        + " | "
                        + faqResult.getString("category")
                );
            }

            System.out.println(
                    "Total FAQs: "
                    + faqCount
            );

            faqResult.close();
            faqStatement.close();


            // =========================================
            // DOCUMENTS
            // =========================================

            System.out.println("\nDOCUMENTS:");

            Statement documentStatement =
                    connection.createStatement();

            ResultSet documentResult =
                    documentStatement.executeQuery(
                            "SELECT id, title, category FROM documents"
                    );

            int documentCount = 0;

            while (documentResult.next()) {

                documentCount++;

                System.out.println(
                        documentResult.getInt("id")
                        + " | "
                        + documentResult.getString("title")
                        + " | "
                        + documentResult.getString("category")
                );
            }

            System.out.println(
                    "Total documents: "
                    + documentCount
            );

            documentResult.close();
            documentStatement.close();


            // =========================================
            // CALENDAR
            // =========================================

            System.out.println("\nCALENDAR EVENTS:");

            Statement calendarStatement =
                    connection.createStatement();

            ResultSet calendarResult =
                    calendarStatement.executeQuery(
                            "SELECT id, title, event_date, category "
                            + "FROM calendar_events"
                    );

            int calendarCount = 0;

            while (calendarResult.next()) {

                calendarCount++;

                System.out.println(
                        calendarResult.getInt("id")
                        + " | "
                        + calendarResult.getString("title")
                        + " | "
                        + calendarResult.getDate("event_date")
                        + " | "
                        + calendarResult.getString("category")
                );
            }

            System.out.println(
                    "Total calendar events: "
                    + calendarCount
            );

            calendarResult.close();
            calendarStatement.close();


            // =========================================
            // CHATBOT KNOWLEDGE
            // =========================================

            System.out.println("\nCHATBOT KNOWLEDGE:");

            Statement chatbotStatement =
                    connection.createStatement();

            ResultSet chatbotResult =
                    chatbotStatement.executeQuery(
                            "SELECT id, keyword, category "
                            + "FROM chatbot_knowledge"
                    );

            int chatbotCount = 0;

            while (chatbotResult.next()) {

                chatbotCount++;

                System.out.println(
                        chatbotResult.getInt("id")
                        + " | "
                        + chatbotResult.getString("keyword")
                        + " | "
                        + chatbotResult.getString("category")
                );
            }

            System.out.println(
                    "Total chatbot knowledge: "
                    + chatbotCount
            );

            chatbotResult.close();
            chatbotStatement.close();


            // =========================================
            // CAMPUS INFORMATION
            // =========================================

            System.out.println("\nCAMPUS INFORMATION:");

            Statement campusStatement =
                    connection.createStatement();

            ResultSet campusResult =
                    campusStatement.executeQuery(
                            "SELECT id, title, location "
                            + "FROM campus_info"
                    );

            int campusCount = 0;

            while (campusResult.next()) {

                campusCount++;

                System.out.println(
                        campusResult.getInt("id")
                        + " | "
                        + campusResult.getString("title")
                        + " | "
                        + campusResult.getString("location")
                );
            }

            System.out.println(
                    "Total campus information: "
                    + campusCount
            );

            campusResult.close();
            campusStatement.close();


            // =========================================
            // FINAL RESULT
            // =========================================

            System.out.println("\n======================================");
            System.out.println("          DATABASE TEST DONE          ");
            System.out.println("======================================");

            System.out.println("Courses: " + courseCount);
            System.out.println("Organizations: " + organizationCount);
            System.out.println("Events: " + eventCount);
            System.out.println("Offices: " + officeCount);
            System.out.println("FAQs: " + faqCount);
            System.out.println("Documents: " + documentCount);
            System.out.println("Calendar Events: " + calendarCount);
            System.out.println("Chatbot Knowledge: " + chatbotCount);
            System.out.println("Campus Information: " + campusCount);

            System.out.println("======================================");

            connection.close();

        } catch (Exception e) {

            System.out.println("\nDATABASE DATA TEST FAILED!");
            System.out.println("--------------------------------------");
            System.out.println("Error: " + e.getMessage());
            System.out.println("--------------------------------------");

            e.printStackTrace();
        }
    }
}