import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    public static class Course {

        private int id;
        private String code;
        private String name;
        private String college;
        private String major;
        private String description;
        private String careerDirections;

        public Course(
                int id,
                String code,
                String name,
                String college,
                String major,
                String description,
                String careerDirections
        ) {
            this.id = id;
            this.code = code;
            this.name = name;
            this.college = college;
            this.major = major;
            this.description = description;
            this.careerDirections = careerDirections;
        }

        public int getId() {
            return id;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public String getCollege() {
            return college;
        }

        public String getMajor() {
            return major;
        }

        public String getDescription() {
            return description;
        }

        public String getCareerDirections() {
            return careerDirections;
        }

        @Override
        public String toString() {
            return code + " - " + name;
        }
    }


    public List<Course> getAllCourses() {

        List<Course> courses = new ArrayList<>();

        String sql =
                "SELECT id, code, name, college, major, "
                + "description, career_directions "
                + "FROM courses "
                + "ORDER BY id";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet result =
                        statement.executeQuery()
        ) {

            while (result.next()) {

                Course course = new Course(

                        result.getInt("id"),

                        result.getString("code"),

                        result.getString("name"),

                        result.getString("college"),

                        result.getString("major"),

                        result.getString("description"),

                        result.getString("career_directions")
                );

                courses.add(course);
            }

        } catch (Exception e) {

            System.out.println(
                    "Error loading courses: "
                    + e.getMessage()
            );
        }

        return courses;
    }
}
