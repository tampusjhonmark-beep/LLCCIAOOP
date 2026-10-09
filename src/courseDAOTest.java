import java.util.List;

public class courseDAOTest {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("        COURSE DAO TEST          ");
        System.out.println("=================================");

        CourseDAO courseDAO = new CourseDAO();

        List<CourseDAO.Course> courses =
                courseDAO.getAllCourses();

        System.out.println(
                "Courses loaded: "
                + courses.size()
        );

        System.out.println("---------------------------------");

        for (CourseDAO.Course course : courses) {

            System.out.println(
                    course.getCode()
                    + " | "
                    + course.getName()
                    + " | "
                    + course.getCollege()
            );
        }

        System.out.println("---------------------------------");
        System.out.println("COURSE DAO TEST COMPLETE");
        System.out.println("=================================");
    }
}