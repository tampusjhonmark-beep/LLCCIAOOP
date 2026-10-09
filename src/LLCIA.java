import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
 * LLCCIA - Lapu-Lapu City College Information Assistant
 *
 * File name : LLCIA.java
 * Main class: LLCIA
 * Java      : 8+
 *
 * NOTE: Keep your existing CourseDAO.java in the same project/package.
 * If the MySQL database cannot be reached (or has no courses), the system
 * automatically falls back to the built-in course list so the app still works.
 */
public class LLCIA extends JFrame {

    // ===================== THEME =====================
    private static final Color NAVY       = new Color(16, 42, 70);
    private static final Color NAVY_DEEP  = new Color(11, 29, 51);
    private static final Color NAVY_SOFT  = new Color(36, 70, 108);
    private static final Color GOLD       = new Color(193, 137, 47);
    private static final Color GOLD_SOFT  = new Color(231, 197, 138);
    private static final Color GOLD_PALE  = new Color(246, 234, 209);
    private static final Color TEAL       = new Color(47, 110, 106);
    private static final Color ROSE       = new Color(163, 73, 47);
    private static final Color CREAM      = new Color(246, 243, 234);
    private static final Color PAPER      = Color.WHITE;
    private static final Color INK        = new Color(28, 36, 49);
    private static final Color INK_SOFT   = new Color(92, 102, 117);
    private static final Color LINE       = new Color(227, 221, 206);

    // Text widths (px) used by wrapped labels so text is always fully visible
    private static final int W_FULL = 720;
    private static final int W2 = 350;   // inside a 2-column card
    private static final int W3 = 215;   // inside a 3-column card
    private static final int W4 = 150;   // inside a 4-column card

    private static final String FONT = "SansSerif";

    // ===================== STATE =====================
    private final CardLayout contentLayout = new CardLayout();
    private final JPanel content = new JPanel(contentLayout);
    private final JTextField globalSearch = new JTextField();
    private final JLabel statusLabel = new JLabel("Ready");
    private final Map<String, NavButton> navButtons = new LinkedHashMap<>();

    private JPanel searchHolder;
    private JPanel courseHolder;
    private final Map<String, AppButton> filterButtons = new LinkedHashMap<>();
    private String activeCategory = "All";
    private ChatDialog chatDialog;
    private boolean coursesFromDatabase = false;

    private final List<Course> courses = new ArrayList<>();
    private final List<Organization> organizations = new ArrayList<>();
    private final List<EventItem> events = new ArrayList<>();
    private final List<Office> offices = new ArrayList<>();
    private final List<DocumentItem> documents = new ArrayList<>();
    private final List<CalendarItem> calendar = new ArrayList<>();
    private final List<FAQ> faqs = new ArrayList<>();
    private final List<String> announcements = Arrays.asList(
        "Enrollment schedules are posted per year level \u2014 check the Registrar or ask LLCCIA.",
        "Scholarship applications are reviewed every semester. Prepare your documents early.",
        "Campus Clash and department days are held throughout the school year \u2014 see the Events section."
    );

    public LLCIA() {
        loadData();
        loadCoursesFromDatabase();
        setupFrame();
        buildUI();
        showPage("Home");
        statusLabel.setText(coursesFromDatabase
                ? "Connected \u2022 " + courses.size() + " course(s) loaded from MySQL"
                : "Offline mode \u2022 showing built-in course list (database not available)");
    }

    // ===================== DATA =====================
    private void loadData() {
        String HTM = "College of Hospitality and Tourism Management";
        String EDU = "College of Education";
        String TECH = "College of Technology";

        courses.add(new Course("HM", "Hospitality Management", HTM,
                "Prepares students for careers in hotels, resorts, and food service operations.",
                new String[]{}, new String[]{"Hotel Operations Staff", "Food & Beverage Service", "Front Office / Guest Relations", "Events & Catering"}));
        courses.add(new Course("TM", "Tourism Management", HTM,
                "Focuses on travel, tour operations, and destination management.",
                new String[]{}, new String[]{"Tour Operations", "Travel Agency Staff", "Airline & Cruise Services", "Destination / Local Tourism Office"}));
        courses.add(new Course("BEED", "Bachelor of Elementary Education", EDU,
                "Prepares future teachers for the elementary education level.",
                new String[]{}, new String[]{"Elementary School Teacher", "Instructional Materials Development", "Educational Support Roles"}));
        courses.add(new Course("EDUC", "Education (Secondary)", EDU,
                "Prepares future teachers for the secondary education level, with a choice of major.",
                new String[]{"English", "Filipino", "Social Studies (Soc-Stud)", "Mathematics"},
                new String[]{"Secondary School Teacher", "Subject-Area Specialist", "Curriculum & Instructional Support"}));
        courses.add(new Course("BIT", "Bachelor of Industrial Technology", TECH,
                "Provides technical and practical preparation for industrial technology careers.",
                new String[]{"Computer Technology", "Electronics"},
                new String[]{"Computer Technician", "Electronics Technician", "Technical Support", "Industrial Technology Roles"}));

        organizations.add(new Organization("SCES", "Student Council and Executive Services", "Student Organizations", GOLD,
                "Student leadership and student representation activities."));
        organizations.add(new Organization("BCH", "Bachelor of Hospitality Club", "Academic Organizations", TEAL,
                "Organization for Hospitality Management students and activities."));
        organizations.add(new Organization("LCW", "LLCC Writers", "Student Organizations", ROSE,
                "Student writing, publication, and literary activities."));
        organizations.add(new Organization("ROTC", "Reserve Officers' Training Corps", "Student Organizations", NAVY_SOFT,
                "Training and activities associated with the ROTC program."));
        organizations.add(new Organization("SELECT", "Student Education Leaders", "Academic Organizations", GOLD,
                "Student leadership and activities related to education."));
        organizations.add(new Organization("HOTEL", "Hospitality and Tourism Organization", "Academic Organizations", TEAL,
                "Activities supporting Hospitality and Tourism students."));
        organizations.add(new Organization("Pi-thons", "Pi-thons", "Technology Organizations", NAVY_SOFT,
                "Technology-focused student activities and collaboration."));
        organizations.add(new Organization("Alle", "Alle", "Student Organizations", ROSE,
                "Student organization and campus engagement activities."));
        organizations.add(new Organization("Salawikafil", "Salawikafil", "Academic Organizations", GOLD,
                "Academic and cultural student activities."));
        organizations.add(new Organization("Sages", "Sages", "Student Organizations", TEAL,
                "Student participation, service, and campus activities."));

        events.add(new EventItem("Intramurals", "Sports and student activities held among departments/programs.", "Campus Event"));
        events.add(new EventItem("ROTC Events", "ROTC-related activities and training events.", "Student Event"));
        events.add(new EventItem("IT Days", "Activities and events associated with technology students.", "Department Day"));
        events.add(new EventItem("Educ Days", "Education-related activities and student events.", "Department Day"));
        events.add(new EventItem("HTM Days", "Hospitality and Tourism Management activities.", "Department Day"));
        events.add(new EventItem("ALCU (Sports)", "Sports activities connected with ALCU events.", "Sports"));
        events.add(new EventItem("Acquaintance Party", "Student social and campus community activity.", "Campus Event"));
        events.add(new EventItem("Christmas Party", "Year-end student/community celebration.", "Campus Event"));
        events.add(new EventItem("Campus Clash", "Campus-wide student activities and competitions.", "Campus Event"));

        offices.add(new Office("Registrar", "Enrollment, student records, transcripts, and registrar documents.", "Registrar's Office"));
        offices.add(new Office("Clinic", "Basic campus health and first-aid support.", "School Clinic"));
        offices.add(new Office("SOA", "Student-related assistance and academic/student affairs support.", "Student/Academic Affairs"));
        offices.add(new Office("Dean\u2019s Office", "Academic administration and program concerns.", "Dean's Office"));
        offices.add(new Office("Library", "Library resources, study spaces, and information services.", "Library"));

        documents.add(new DocumentItem("Scholarship Papers", "Documents commonly needed when applying for scholarship assistance."));
        documents.add(new DocumentItem("Transferee Papers", "Documents for students applying as transferees."));
        documents.add(new DocumentItem("Enrollment Papers", "Papers associated with enrollment and registration."));
        documents.add(new DocumentItem("Student Papers", "Common student forms and school paperwork."));
        documents.add(new DocumentItem("Requirements Papers", "Common school requirements and supporting papers."));
        documents.add(new DocumentItem("Transcript of Records", "Guide/reference for requesting a transcript of records."));
        documents.add(new DocumentItem("Graduation Requirements", "Reference for common graduation-related requirements."));
        documents.add(new DocumentItem("Registrar Document Guide", "Guide for registrar-related document requests."));
        documents.add(new DocumentItem("Enrollment Guide", "General guide for enrollment steps and preparation."));
        documents.add(new DocumentItem("1st year requirments", "2by2 id, medical requirments, nso,report card"));

        calendar.add(new CalendarItem("Enrollment Period", "Enrollment schedules are posted per year level."));
        calendar.add(new CalendarItem("Start of Classes", "Beginning of the academic term."));
        calendar.add(new CalendarItem("Midterm Examinations", "Midterm examination period."));
        calendar.add(new CalendarItem("Final Examinations", "Final examination period."));
        calendar.add(new CalendarItem("Intramurals/Campus Events Week", "Period for campus sports and student activities."));
        calendar.add(new CalendarItem("Graduation", "Graduation-related activities and ceremonies."));

        faqs.add(new FAQ("Student portal issues", "If the student portal is not working, verify your account details and contact the appropriate school office for assistance.", "portal", "login", "account"));
        faqs.add(new FAQ("Student ID", "For Student ID concerns, coordinate with the designated school office and bring the required identification or supporting documents.", "id", "identification"));
        faqs.add(new FAQ("Add/drop subject", "Ask the Registrar or the appropriate academic office about the current add/drop process and deadlines.", "add", "drop", "subject"));
        faqs.add(new FAQ("Cross-enrollment", "Coordinate with the Registrar or academic office regarding current cross-enrollment requirements and approval.", "cross", "cross-enrollment"));
        faqs.add(new FAQ("School fees", "Check the current official school fee information or coordinate with the appropriate office for the latest amount.", "fee", "fees", "tuition", "payment"));
        faqs.add(new FAQ("Lost school document", "Report the lost document to the appropriate office and ask about replacement requirements.", "lost", "missing", "replace"));
        faqs.add(new FAQ("1st year requirments", " The requirments are: 2by2 id, medical requirments, nso,report card"));
        
        
    }

    /** Loads courses from MySQL through your CourseDAO. Falls back to built-in data on any failure. */
    private void loadCoursesFromDatabase() {
        List<Course> loaded = new ArrayList<>();
        try {
            CourseDAO courseDAO = new CourseDAO();
            List<CourseDAO.Course> dbCourses = courseDAO.getAllCourses();
            if (dbCourses != null) {
                for (CourseDAO.Course db : dbCourses) {
                    String summary = db.getDescription();
                    if (summary == null || summary.trim().isEmpty()) summary = "No description available.";
                    String college = db.getCollege();
                    if (college == null || college.trim().isEmpty()) college = "Other";
                    loaded.add(new Course(db.getCode(), db.getName(), college.trim(), summary,
                            splitValues(db.getMajor()), splitValues(db.getCareerDirections())));
                }
            }
        } catch (Throwable t) {
            System.err.println("LLCCIA: could not load courses from MySQL - " + t);
        }
        if (!loaded.isEmpty()) {
            courses.clear();
            courses.addAll(loaded);
            coursesFromDatabase = true;
            System.out.println("LLCCIA loaded " + courses.size() + " course(s) from MySQL.");
        }
    }

    private String[] splitValues(String value) {
        if (value == null || value.trim().isEmpty()) return new String[0];
        String n = value.replace("\u2022", "\n").replace(";", "\n").replace("|", "\n")
                .replace(",", "\n").replace("\r", "");
        List<String> out = new ArrayList<>();
        for (String part : n.split("\n")) {
            part = part.trim();
            if (!part.isEmpty()) out.add(part);
        }
        return out.toArray(new String[0]);
    }

    // ===================== FRAME =====================
    private void setupFrame() {
        setTitle("LLCCIA \u2014 Lapu-Lapu City College Information Assistant");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 700));
        setSize(1280, 820);
        setLocationRelativeTo(null);
        getContentPane().setBackground(CREAM);
    }

    private void buildUI() {
        setLayout(new BorderLayout());
        add(buildTopBar(), BorderLayout.NORTH);
        add(buildSidebar(), BorderLayout.WEST);
        content.setBackground(CREAM);
        createPages();
        add(content, BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout(15, 0));
        bar.setBackground(NAVY_DEEP);
        bar.setBorder(new CompoundBorder(new MatteBorder(0, 0, 3, 0, GOLD), new EmptyBorder(12, 20, 12, 20)));

        JPanel brand = new JPanel();
        brand.setOpaque(false);
        brand.setLayout(new BoxLayout(brand, BoxLayout.Y_AXIS));
        JLabel logo = new JLabel("LLCCIA");
        logo.setForeground(GOLD_SOFT);
        logo.setFont(new Font(FONT, Font.BOLD, 26));
        JLabel subtitle = new JLabel("LAPU-LAPU CITY COLLEGE INFORMATION ASSISTANT");
        subtitle.setForeground(Color.WHITE);
        subtitle.setFont(new Font(FONT, Font.BOLD, 11));
        brand.add(logo);
        brand.add(subtitle);

        globalSearch.setPreferredSize(new Dimension(340, 36));
        globalSearch.setFont(new Font(FONT, Font.PLAIN, 14));
        globalSearch.setBackground(Color.WHITE);
        globalSearch.setForeground(INK);
        globalSearch.setCaretColor(INK);
        globalSearch.setBorder(new CompoundBorder(new LineBorder(GOLD_SOFT, 1), new EmptyBorder(4, 10, 4, 10)));
        globalSearch.setToolTipText("Search courses, offices, documents, events, FAQs...");
        globalSearch.addActionListener(e -> performSearch(globalSearch.getText()));

        AppButton searchButton = new AppButton("Search", GOLD, NAVY_DEEP, null);
        searchButton.addActionListener(e -> performSearch(globalSearch.getText()));

        JPanel searchPanel = new JPanel(new BorderLayout(8, 0));
        searchPanel.setOpaque(false);
        searchPanel.add(globalSearch, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        bar.add(brand, BorderLayout.WEST);
        bar.add(searchPanel, BorderLayout.EAST);
        return bar;
    }

    private JPanel buildSidebar() {
        JPanel side = new JPanel();
        side.setBackground(NAVY);
        side.setPreferredSize(new Dimension(210, 0));
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBorder(new EmptyBorder(18, 12, 18, 12));

        JLabel menu = new JLabel("MENU");
        menu.setForeground(GOLD_SOFT);
        menu.setFont(new Font(FONT, Font.BOLD, 11));
        menu.setBorder(new EmptyBorder(0, 6, 8, 0));
        menu.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.add(menu);

        String[] nav = {"Home", "Courses", "Admission", "Organizations", "Events", "Offices", "Documents", "Calendar", "FAQ", "Campus"};
        for (String item : nav) {
            NavButton b = new NavButton(item);
            b.setAlignmentX(Component.LEFT_ALIGNMENT);
            b.addActionListener(e -> showPage(item));
            navButtons.put(item, b);
            side.add(b);
            side.add(Box.createVerticalStrut(4));
        }

        side.add(Box.createVerticalGlue());
        JLabel guide = new JLabel("VIRTUAL GUIDE");
        guide.setForeground(GOLD_SOFT);
        guide.setFont(new Font(FONT, Font.BOLD, 11));
        guide.setBorder(new EmptyBorder(0, 6, 8, 0));
        guide.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.add(guide);

        AppButton chat = new AppButton("Ask LLCCIA", GOLD, NAVY_DEEP, null);
        chat.setAlignmentX(Component.LEFT_ALIGNMENT);
        chat.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        chat.addActionListener(e -> openChat());
        side.add(chat);
        return side;
    }

    private JPanel buildStatusBar() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new MatteBorder(1, 0, 0, 0, LINE));
        statusLabel.setBorder(new EmptyBorder(6, 14, 6, 14));
        statusLabel.setForeground(INK_SOFT);
        statusLabel.setFont(new Font(FONT, Font.PLAIN, 12));
        p.add(statusLabel, BorderLayout.WEST);
        return p;
    }

    // ===================== PAGES =====================
    private void createPages() {
        buildHomePage();
        buildCoursesPage();
        buildAdmissionPage();
        buildOrganizationsPage();
        buildEventsPage();
        buildOfficesPage();
        buildDocumentsPage();
        buildCalendarPage();
        buildFaqPage();
        buildCampusPage();
        buildSearchPage();
    }

    /** Creates a scrollable page with a title header and returns the body panel to add content to. */
    private JPanel createPage(String name, String title, String subtitle) {
        JPanel body = new ScrollablePanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(CREAM);
        body.setBorder(new EmptyBorder(24, 28, 30, 28));
        body.add(pageHeader(title, subtitle));
        body.add(Box.createVerticalStrut(18));

        JScrollPane sp = new JScrollPane(body);
        sp.setBorder(null);
        sp.getVerticalScrollBar().setUnitIncrement(18);
        sp.getViewport().setBackground(CREAM);
        content.add(sp, name);
        return body;
    }

    private JPanel pageHeader(String title, String subtitle) {
        FitPanel h = new FitPanel(null);
        h.setLayout(new BoxLayout(h, BoxLayout.Y_AXIS));
        h.setOpaque(false);
        h.setAlignmentX(Component.LEFT_ALIGNMENT);
        h.add(text(title, 28, Font.BOLD, NAVY, W_FULL));
        h.add(Box.createVerticalStrut(4));
        h.add(text(subtitle, 14, Font.PLAIN, INK_SOFT, W_FULL));
        h.add(Box.createVerticalStrut(10));
        JPanel bar = new JPanel();
        bar.setBackground(GOLD);
        bar.setPreferredSize(new Dimension(70, 4));
        bar.setMaximumSize(new Dimension(70, 4));
        bar.setAlignmentX(Component.LEFT_ALIGNMENT);
        h.add(bar);
        return h;
    }

    // ---------- HOME ----------
    private void buildHomePage() {
        JPanel p = createPage("Home", "Home", "Everything you need to know about LLCC in one place.");

        p.add(heroPanel());
        p.add(Box.createVerticalStrut(18));

        // stats row
        FitPanel stats = new FitPanel(new GridLayout(1, 4, 14, 14));
        stats.setOpaque(false);
        stats.setAlignmentX(Component.LEFT_ALIGNMENT);
        stats.add(statTile(String.valueOf(courses.size()), "Courses", GOLD));
        stats.add(statTile(String.valueOf(organizations.size()), "Organizations", TEAL));
        stats.add(statTile(String.valueOf(events.size()), "Events", ROSE));
        stats.add(statTile(String.valueOf(offices.size()), "Offices", NAVY_SOFT));
        p.add(stats);
        p.add(Box.createVerticalStrut(18));

        // announcements
        Card ann = new Card(GOLD);
        ann.add(sectionLabel("ANNOUNCEMENTS"));
        ann.add(Box.createVerticalStrut(8));
        for (String a : announcements) {
            ann.add(text("\u2022  " + a, 14, Font.PLAIN, INK, W_FULL - 20));
            ann.add(Box.createVerticalStrut(6));
        }
        p.add(ann);
        p.add(Box.createVerticalStrut(22));

        p.add(text("Quick Access", 20, Font.BOLD, NAVY, W_FULL));
        p.add(Box.createVerticalStrut(2));
        p.add(text("Browse the most requested LLCC information.", 13, Font.PLAIN, INK_SOFT, W_FULL));
        p.add(Box.createVerticalStrut(10));

        FitPanel grid = new FitPanel(new GridLayout(0, 3, 14, 14));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);
        String[][] links = {
            {"Courses", "Programs, majors, and career paths"},
            {"Admission", "Admission steps and reminders"},
            {"Documents", "Document and requirement guides"},
            {"Organizations", "Student and academic organizations"},
            {"Offices", "Office directory and functions"},
            {"Campus", "Simple interactive campus guide"}
        };
        for (String[] x : links) grid.add(clickableCard(x[0], x[1]));
        p.add(grid);
        p.add(Box.createVerticalStrut(22));

        Card chat = new Card(TEAL);
        chat.add(sectionLabel("VIRTUAL GUIDE"));
        chat.add(Box.createVerticalStrut(6));
        chat.add(text("Ask LLCCIA about courses, admission, organizations, events, offices, documents, calendar, or FAQs.", 14, Font.PLAIN, INK, W_FULL - 20));
        chat.add(Box.createVerticalStrut(12));
        AppButton b = new AppButton("Open Chatbot", GOLD, NAVY_DEEP, null);
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.addActionListener(e -> openChat());
        chat.add(b);
        p.add(chat);
    }

    private JPanel heroPanel() {
        JPanel hero = new JPanel(new BorderLayout(20, 10)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, NAVY_DEEP, getWidth(), getHeight(), NAVY_SOFT));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(GOLD);
                g2.fillRect(0, getHeight() - 5, getWidth(), 5);
                g2.dispose();
            }
            @Override public Dimension getMaximumSize() {
                return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
            }
        };
        hero.setOpaque(false);
        hero.setAlignmentX(Component.LEFT_ALIGNMENT);
        hero.setBorder(new EmptyBorder(28, 30, 30, 30));

        JPanel t = new JPanel();
        t.setOpaque(false);
        t.setLayout(new BoxLayout(t, BoxLayout.Y_AXIS));
        t.add(text("Welcome to LLCCIA", 30, Font.BOLD, Color.WHITE, 560));
        t.add(Box.createVerticalStrut(4));
        t.add(text("Your Lapu-Lapu City College Information Assistant", 15, Font.BOLD, GOLD_SOFT, 560));
        t.add(Box.createVerticalStrut(10));
        t.add(text("Find campus information, programs, admission guides, organizations, events, offices, documents, and FAQs.", 14, Font.PLAIN, Color.WHITE, 560));
        hero.add(t, BorderLayout.CENTER);

        JPanel east = new JPanel(new GridBagLayout());
        east.setOpaque(false);
        AppButton ask = new AppButton("Ask LLCCIA", GOLD, NAVY_DEEP, null);
        ask.addActionListener(e -> openChat());
        east.add(ask);
        hero.add(east, BorderLayout.EAST);
        return hero;
    }

    private JPanel statTile(String number, String label, Color accent) {
        Card c = new Card(accent);
        c.add(text(number, 30, Font.BOLD, accent, W4));
        c.add(text(label, 13, Font.BOLD, INK_SOFT, W4));
        return c;
    }

    // ---------- COURSES ----------
    private void buildCoursesPage() {
        JPanel p = createPage("Courses", "Courses", "Programs, majors, summaries, and possible career paths.");

        Set<String> cats = new LinkedHashSet<>();
        for (Course c : courses) cats.add(c.category);
        List<String> keys = new ArrayList<>();
        keys.add("All");
        keys.addAll(cats);

        p.add(sectionLabel("FILTER BY COLLEGE"));
        p.add(Box.createVerticalStrut(6));
        FitPanel filters = new FitPanel(new GridLayout(0, 3, 8, 8));
        filters.setOpaque(false);
        filters.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (String key : keys) {
            AppButton b = new AppButton(key.replaceFirst("^College of ", ""), Color.WHITE, NAVY, LINE);
            b.addActionListener(e -> { activeCategory = key; refreshCourseList(); });
            filterButtons.put(key, b);
            filters.add(b);
        }
        p.add(filters);
        p.add(Box.createVerticalStrut(16));

        courseHolder = new FitPanel(new GridLayout(0, 2, 16, 16));
        courseHolder.setOpaque(false);
        courseHolder.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(courseHolder);
        refreshCourseList();
    }

    private void refreshCourseList() {
        for (Map.Entry<String, AppButton> e : filterButtons.entrySet()) {
            boolean on = e.getKey().equals(activeCategory);
            e.getValue().setStyle(on ? GOLD : Color.WHITE, on ? NAVY_DEEP : NAVY, on ? null : LINE);
        }
        courseHolder.removeAll();
        int count = 0;
        for (Course c : courses) {
            if (activeCategory.equals("All") || c.category.equals(activeCategory)) {
                courseHolder.add(courseCard(c));
                count++;
            }
        }
        if (count == 0) courseHolder.add(new Card(GOLD));
        courseHolder.revalidate();
        courseHolder.repaint();
    }

    private JPanel courseCard(Course c) {
        Card card = new Card(GOLD);
        card.add(pill(c.code, GOLD_PALE, NAVY));
        card.add(Box.createVerticalStrut(8));
        card.add(text(c.name, 18, Font.BOLD, NAVY, W2));
        card.add(Box.createVerticalStrut(2));
        card.add(text(c.category, 12, Font.BOLD, TEAL, W2));
        card.add(Box.createVerticalStrut(8));
        card.add(text(c.summary, 13, Font.PLAIN, INK, W2));

        if (c.majors.length > 0) {
            card.add(Box.createVerticalStrut(10));
            card.add(text("MAJORS", 11, Font.BOLD, GOLD, W2));
            card.add(Box.createVerticalStrut(3));
            card.add(text(bullets(c.majors), 13, Font.PLAIN, INK, W2));
        }
        if (c.careers.length > 0) {
            card.add(Box.createVerticalStrut(10));
            card.add(text("POSSIBLE CAREER PATHS", 11, Font.BOLD, GOLD, W2));
            card.add(Box.createVerticalStrut(3));
            card.add(text(bullets(c.careers), 13, Font.PLAIN, INK, W2));
        }
        card.add(Box.createVerticalStrut(12));
        AppButton ask = new AppButton("Ask LLCCIA", GOLD, NAVY_DEEP, null);
        ask.setAlignmentX(Component.LEFT_ALIGNMENT);
        ask.addActionListener(e -> askBot("Tell me about " + c.name));
        card.add(ask);
        return card;
    }

    private String bullets(String[] items) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.length; i++) {
            if (i > 0) sb.append("\n");
            sb.append("\u2022  ").append(items[i]);
        }
        return sb.toString();
    }

    // ---------- ADMISSION ----------
    private void buildAdmissionPage() {
        JPanel p = createPage("Admission", "Admission", "General admission information for new and returning students.");

        Card c = new Card(GOLD);
        c.add(sectionLabel("ADMISSION GUIDE"));
        c.add(Box.createVerticalStrut(6));
        c.add(text("Prepare your school requirements, identification, and other documents required by the current admission process.", 14, Font.PLAIN, INK, W_FULL - 20));
        c.add(Box.createVerticalStrut(8));
        c.add(text("For the latest deadlines, fees, forms, and official requirements, verify with the appropriate LLCC office.", 13, Font.PLAIN, INK_SOFT, W_FULL - 20));
        c.add(Box.createVerticalStrut(12));
        AppButton docs = new AppButton("View Documents", GOLD, NAVY_DEEP, null);
        docs.setAlignmentX(Component.LEFT_ALIGNMENT);
        docs.addActionListener(e -> showPage("Documents"));
        c.add(docs);
        p.add(c);
        p.add(Box.createVerticalStrut(18));

        p.add(text("Steps to Enroll", 20, Font.BOLD, NAVY, W_FULL));
        p.add(Box.createVerticalStrut(10));
        FitPanel steps = new FitPanel(new GridLayout(0, 2, 16, 16));
        steps.setOpaque(false);
        steps.setAlignmentX(Component.LEFT_ALIGNMENT);
        steps.add(infoCard("1", "Prepare", "Review the current requirements and documents."));
        steps.add(infoCard("2", "Apply", "Follow the current admission/enrollment process."));
        steps.add(infoCard("3", "Verify", "Check with the responsible office for current details."));
        steps.add(infoCard("4", "Enroll", "Complete the required registration steps."));
        p.add(steps);
    }

    // ---------- ORGANIZATIONS ----------
    private void buildOrganizationsPage() {
        JPanel p = createPage("Organizations", "Organizations", "Student and academic organizations listed in LLCCIA.");
        FitPanel grid = new FitPanel(new GridLayout(0, 2, 16, 16));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (Organization o : organizations) {
            Card c = new Card(o.accent);
            c.add(pill(o.code, GOLD_PALE, NAVY));
            c.add(Box.createVerticalStrut(8));
            c.add(text(o.name, 17, Font.BOLD, NAVY, W2));
            c.add(text(o.category, 12, Font.BOLD, o.accent, W2));
            c.add(Box.createVerticalStrut(6));
            c.add(text(o.description, 13, Font.PLAIN, INK, W2));
            grid.add(c);
        }
        p.add(grid);
    }

    // ---------- EVENTS ----------
    private void buildEventsPage() {
        JPanel p = createPage("Events", "Events", "Campus and department events included in LLCCIA.");
        FitPanel grid = new FitPanel(new GridLayout(0, 2, 16, 16));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (EventItem e : events) {
            Card c = new Card(ROSE);
            c.add(pill(e.type, GOLD_PALE, NAVY));
            c.add(Box.createVerticalStrut(8));
            c.add(text(e.name, 17, Font.BOLD, NAVY, W2));
            c.add(Box.createVerticalStrut(4));
            c.add(text(e.description, 13, Font.PLAIN, INK, W2));
            grid.add(c);
        }
        p.add(grid);
    }

    // ---------- OFFICES ----------
    private void buildOfficesPage() {
        JPanel p = createPage("Offices", "Office Directory", "Offices and the type of assistance associated with each.");
        FitPanel grid = new FitPanel(new GridLayout(0, 2, 16, 16));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (Office o : offices) {
            Card c = new Card(NAVY_SOFT);
            c.add(pill(o.role, GOLD_PALE, NAVY));
            c.add(Box.createVerticalStrut(8));
            c.add(text(o.name, 18, Font.BOLD, NAVY, W2));
            c.add(Box.createVerticalStrut(4));
            c.add(text(o.description, 13, Font.PLAIN, INK, W2));
            grid.add(c);
        }
        p.add(grid);
    }

    // ---------- DOCUMENTS ----------
    private void buildDocumentsPage() {
        JPanel p = createPage("Documents", "Documents", "Document and printable-paper guides listed by LLCCIA.");
        FitPanel grid = new FitPanel(new GridLayout(0, 2, 16, 16));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (DocumentItem d : documents) {
            Card c = new Card(TEAL);
            c.add(pill("DOCUMENT", GOLD_PALE, NAVY));
            c.add(Box.createVerticalStrut(8));
            c.add(text(d.name, 17, Font.BOLD, NAVY, W2));
            c.add(Box.createVerticalStrut(4));
            c.add(text(d.description, 13, Font.PLAIN, INK, W2));
            c.add(Box.createVerticalStrut(12));
            AppButton view = new AppButton("Open Guide", Color.WHITE, NAVY, NAVY);
            view.setAlignmentX(Component.LEFT_ALIGNMENT);
            view.addActionListener(e -> showDocumentGuide(d));
            c.add(view);
            grid.add(c);
        }
        p.add(grid);
    }

    private void showDocumentGuide(DocumentItem d) {
        JOptionPane.showMessageDialog(this,
                d.name + "\n\n" + d.description
                + "\n\nPlease verify the current official requirements with the responsible LLCC office before submission.",
                "LLCCIA Document Guide", JOptionPane.INFORMATION_MESSAGE);
    }

    // ---------- CALENDAR ----------
    private void buildCalendarPage() {
        JPanel p = createPage("Calendar", "Calendar", "Academic and campus calendar items included in LLCCIA.");
        int n = 1;
        for (CalendarItem x : calendar) {
            Card c = new Card(GOLD);
            c.setLayout(new BorderLayout(16, 0));
            JLabel num = new JLabel(String.valueOf(n++), SwingConstants.CENTER);
            num.setOpaque(true);
            num.setBackground(GOLD);
            num.setForeground(Color.WHITE);
            num.setFont(new Font(FONT, Font.BOLD, 16));
            num.setPreferredSize(new Dimension(40, 40));
            JPanel numWrap = new JPanel(new GridBagLayout());
            numWrap.setOpaque(false);
            numWrap.add(num);
            c.add(numWrap, BorderLayout.WEST);

            JPanel t = new JPanel();
            t.setOpaque(false);
            t.setLayout(new BoxLayout(t, BoxLayout.Y_AXIS));
            t.add(text(x.name, 16, Font.BOLD, NAVY, W_FULL - 80));
            t.add(Box.createVerticalStrut(3));
            t.add(text(x.description, 13, Font.PLAIN, INK, W_FULL - 80));
            c.add(t, BorderLayout.CENTER);
            p.add(c);
            p.add(Box.createVerticalStrut(10));
        }
    }

    // ---------- FAQ ----------
    private void buildFaqPage() {
        JPanel p = createPage("FAQ", "Frequently Asked Questions", "Common student questions and answers.");
        FitPanel grid = new FitPanel(new GridLayout(0, 2, 16, 16));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (FAQ f : faqs) {
            Card c = new Card(TEAL);
            c.add(pill("QUESTION", GOLD_PALE, NAVY));
            c.add(Box.createVerticalStrut(8));
            c.add(text(f.question, 16, Font.BOLD, NAVY, W2));
            c.add(Box.createVerticalStrut(6));
            c.add(text(f.answer, 13, Font.PLAIN, INK, W2));
            grid.add(c);
        }
        p.add(grid);
    }

    // ---------- CAMPUS ----------
    private void buildCampusPage() {
        JPanel p = createPage("Campus", "Campus", "A simple guide to key campus areas and the offices related to them.");
        String[][] spots = {
            {"Main Gate", "Entrance to the campus.", "Registrar"},
            {"Library", "Library resources, study spaces, and information services.", "Library"},
            {"Clinic", "Basic campus health and first-aid support.", "Clinic"},
            {"Student Area", "Student activities and campus engagement.", "SOA"}
        };
        FitPanel grid = new FitPanel(new GridLayout(0, 2, 16, 16));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (String[] s : spots) {
            Card c = new Card(NAVY_SOFT);
            c.add(pill("CAMPUS AREA", GOLD_PALE, NAVY));
            c.add(Box.createVerticalStrut(8));
            c.add(text(s[0], 18, Font.BOLD, NAVY, W2));
            c.add(Box.createVerticalStrut(4));
            c.add(text(s[1], 13, Font.PLAIN, INK, W2));
            c.add(Box.createVerticalStrut(4));
            c.add(text("Related office: " + s[2], 12, Font.BOLD, TEAL, W2));
            c.add(Box.createVerticalStrut(12));
            AppButton b = new AppButton("View Offices", Color.WHITE, NAVY, NAVY);
            b.setAlignmentX(Component.LEFT_ALIGNMENT);
            b.addActionListener(e -> showPage("Offices"));
            c.add(b);
            grid.add(c);
        }
        p.add(grid);
        p.add(Box.createVerticalStrut(14));
        p.add(text("Detailed locations should be updated with the official LLCC campus map before deployment.", 12, Font.PLAIN, INK_SOFT, W_FULL));
    }

    // ---------- SEARCH ----------
    private void buildSearchPage() {
        JPanel p = createPage("Search", "Search Results", "Results from courses, organizations, events, offices, documents, and FAQs.");
        searchHolder = new FitPanel(null);
        searchHolder.setLayout(new BoxLayout(searchHolder, BoxLayout.Y_AXIS));
        searchHolder.setOpaque(false);
        searchHolder.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(searchHolder);
    }

    private void performSearch(String query) {
        query = query == null ? "" : query.trim();
        if (query.isEmpty()) { showPage("Home"); return; }
        String q = query.toLowerCase(Locale.ENGLISH);

        searchHolder.removeAll();
        int count = 0;

        for (Course c : courses)
            if (matches(q, c.code, c.name, c.category, c.summary, String.join(" ", c.majors), String.join(" ", c.careers))) {
                addResult("COURSE", c.code + " \u2014 " + c.name, c.summary, "Courses"); count++; }
        for (Organization o : organizations)
            if (matches(q, o.code, o.name, o.category, o.description)) {
                addResult("ORGANIZATION", o.name, o.description, "Organizations"); count++; }
        for (EventItem e : events)
            if (matches(q, e.name, e.type, e.description)) {
                addResult("EVENT", e.name, e.description, "Events"); count++; }
        for (Office o : offices)
            if (matches(q, o.name, o.role, o.description)) {
                addResult("OFFICE", o.name, o.description, "Offices"); count++; }
        for (DocumentItem d : documents)
            if (matches(q, d.name, d.description)) {
                addResult("DOCUMENT", d.name, d.description, "Documents"); count++; }
        for (FAQ f : faqs)
            if (matches(q, f.question, f.answer)) {
                addResult("FAQ", f.question, f.answer, "FAQ"); count++; }

        JPanel summary = new FitPanel(null);
        summary.setLayout(new BoxLayout(summary, BoxLayout.Y_AXIS));
        summary.setOpaque(false);
        summary.setAlignmentX(Component.LEFT_ALIGNMENT);
        summary.add(text(count + " result(s) for \"" + query + "\"", 15, Font.BOLD, NAVY, W_FULL));
        summary.add(Box.createVerticalStrut(10));
        searchHolder.add(summary, 0);

        if (count == 0) {
            Card c = new Card(ROSE);
            c.add(text("No matching LLCCIA information was found. Try a course name, office, document, event, organization, or FAQ topic.", 14, Font.PLAIN, INK, W_FULL - 20));
            searchHolder.add(c);
        }
        searchHolder.revalidate();
        searchHolder.repaint();
        showPage("Search");
        statusLabel.setText(count + " search result(s) found");
    }

    private void addResult(String type, String title, String desc, String page) {
        Card c = new Card(GOLD);
        c.add(pill(type, GOLD_PALE, NAVY));
        c.add(Box.createVerticalStrut(6));
        c.add(text(title, 16, Font.BOLD, NAVY, W_FULL - 20));
        c.add(Box.createVerticalStrut(4));
        c.add(text(desc, 13, Font.PLAIN, INK, W_FULL - 20));
        c.add(Box.createVerticalStrut(10));
        AppButton b = new AppButton("Open " + page, Color.WHITE, NAVY, NAVY);
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.addActionListener(e -> showPage(page));
        c.add(b);
        searchHolder.add(c);
        searchHolder.add(Box.createVerticalStrut(10));
    }

    private boolean matches(String q, String... values) {
        for (String v : values) if (v != null && v.toLowerCase(Locale.ENGLISH).contains(q)) return true;
        return false;
    }

    // ===================== CHATBOT =====================
    private void openChat() {
        if (chatDialog == null) chatDialog = new ChatDialog();
        chatDialog.setVisible(true);
        chatDialog.toFront();
        chatDialog.focusInput();
    }

    private void askBot(String question) {
        openChat();
        chatDialog.send(question);
    }

    private class ChatDialog extends JDialog {
        private final JPanel messages = new ScrollablePanel();
        private final JScrollPane scroll;
        private final JTextField input = new JTextField();

        ChatDialog() {
            super(LLCIA.this, "LLCCIA Virtual Guide", false);
            setSize(560, 700);
            setLocationRelativeTo(LLCIA.this);
            getContentPane().setLayout(new BorderLayout());
            getContentPane().setBackground(CREAM);

            // header
            JPanel head = new JPanel(new BorderLayout());
            head.setBackground(NAVY);
            head.setBorder(new CompoundBorder(new MatteBorder(0, 0, 3, 0, GOLD), new EmptyBorder(12, 16, 12, 16)));
            JPanel titles = new JPanel();
            titles.setOpaque(false);
            titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));
            JLabel t1 = new JLabel("LLCCIA Virtual Guide");
            t1.setForeground(GOLD_SOFT);
            t1.setFont(new Font(FONT, Font.BOLD, 18));
            JLabel t2 = new JLabel("Lapu-Lapu City College Information Assistant");
            t2.setForeground(Color.WHITE);
            t2.setFont(new Font(FONT, Font.PLAIN, 12));
            titles.add(t1);
            titles.add(t2);
            head.add(titles, BorderLayout.CENTER);
            AppButton clear = new AppButton("Clear", Color.WHITE, NAVY, null);
            clear.addActionListener(e -> {
                messages.removeAll();
                addMessage("Chat cleared. How can I help you?", false);
            });
            head.add(clear, BorderLayout.EAST);
            add(head, BorderLayout.NORTH);

            // messages
            messages.setLayout(new BoxLayout(messages, BoxLayout.Y_AXIS));
            messages.setBackground(CREAM);
            messages.setBorder(new EmptyBorder(14, 14, 14, 14));
            scroll = new JScrollPane(messages);
            scroll.setBorder(null);
            scroll.getVerticalScrollBar().setUnitIncrement(16);
            scroll.getViewport().setBackground(CREAM);
            add(scroll, BorderLayout.CENTER);

            // bottom: quick chips + input
            JPanel bottom = new JPanel(new BorderLayout(0, 8));
            bottom.setBackground(Color.WHITE);
            bottom.setBorder(new CompoundBorder(new MatteBorder(1, 0, 0, 0, LINE), new EmptyBorder(10, 12, 12, 12)));

            JPanel chips = new JPanel(new GridLayout(2, 4, 6, 6));
            chips.setOpaque(false);
            String[] quick = {"Courses", "Admission", "Organizations", "Events", "Offices", "Documents", "Calendar", "FAQ"};
            for (String s : quick) {
                AppButton chip = new AppButton(s, GOLD_PALE, NAVY, GOLD_SOFT);
                chip.setFont(new Font(FONT, Font.BOLD, 11));
                chip.addActionListener(e -> send(s.equals("FAQ") ? "Show me the FAQ topics" : "Tell me about " + s.toLowerCase()));
                chips.add(chip);
            }
            bottom.add(chips, BorderLayout.NORTH);

            JPanel row = new JPanel(new BorderLayout(8, 0));
            row.setOpaque(false);
            input.setFont(new Font(FONT, Font.PLAIN, 14));
            input.setBackground(Color.WHITE);
            input.setForeground(INK);
            input.setCaretColor(INK);
            input.setBorder(new CompoundBorder(new LineBorder(LINE, 1), new EmptyBorder(6, 10, 6, 10)));
            AppButton sendBtn = new AppButton("Send", GOLD, NAVY_DEEP, null);
            sendBtn.addActionListener(e -> sendFromInput());
            input.addActionListener(e -> sendFromInput());
            row.add(input, BorderLayout.CENTER);
            row.add(sendBtn, BorderLayout.EAST);
            bottom.add(row, BorderLayout.CENTER);
            add(bottom, BorderLayout.SOUTH);

            addMessage("Hello! I can help you find information about LLCC courses, admission, organizations, events, offices, documents, calendar, and FAQs.", false);
        }

        void focusInput() { input.requestFocusInWindow(); }

        private void sendFromInput() {
            String q = input.getText().trim();
            if (!q.isEmpty()) { input.setText(""); send(q); }
        }

        void send(String q) {
            addMessage(q, true);
            addMessage(chatAnswer(q), false);
        }

        private void addMessage(String msg, boolean user) {
            JPanel bubble = new JPanel();
            bubble.setLayout(new BoxLayout(bubble, BoxLayout.Y_AXIS));
            bubble.setBackground(user ? NAVY : Color.WHITE);
            bubble.setBorder(new CompoundBorder(new LineBorder(user ? NAVY : LINE, 1), new EmptyBorder(10, 12, 10, 12)));
            JLabel who = new JLabel(user ? "You" : "LLCCIA");
            who.setFont(new Font(FONT, Font.BOLD, 11));
            who.setForeground(user ? GOLD_SOFT : GOLD);
            who.setAlignmentX(Component.LEFT_ALIGNMENT);
            bubble.add(who);
            bubble.add(Box.createVerticalStrut(3));
            bubble.add(text(msg, 13, Font.PLAIN, user ? Color.WHITE : INK, 340));

            FitPanel rowPanel = new FitPanel(new FlowLayout(user ? FlowLayout.RIGHT : FlowLayout.LEFT, 0, 0));
            rowPanel.setOpaque(false);
            rowPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
            rowPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            rowPanel.add(bubble);
            messages.add(rowPanel);
            messages.revalidate();
            messages.repaint();
            SwingUtilities.invokeLater(() -> {
                JScrollBar sb = scroll.getVerticalScrollBar();
                sb.setValue(sb.getMaximum());
            });
        }
    }

    // ---------- chatbot brain ----------
    private Set<String> words(String q) {
        Set<String> s = new HashSet<>();
        for (String t : q.split("[^a-z0-9]+")) if (!t.isEmpty()) s.add(t);
        return s;
    }

    private boolean has(Set<String> w, String... keys) {
        for (String k : keys) if (w.contains(k)) return true;
        return false;
    }

    private String chatAnswer(String raw) {
        String q = raw.toLowerCase(Locale.ENGLISH).trim();
        Set<String> w = words(q);

        // 1) specific course
        for (Course c : courses)
            if (w.contains(c.code.toLowerCase(Locale.ENGLISH)) || q.contains(c.name.toLowerCase(Locale.ENGLISH)))
                return courseAnswer(c);

        // 2) specific organization
        for (Organization o : organizations)
            if (q.contains(o.name.toLowerCase(Locale.ENGLISH)) || w.contains(o.code.toLowerCase(Locale.ENGLISH))
                    || (o.code.length() > 3 && q.contains(o.code.toLowerCase(Locale.ENGLISH))))
                return o.name + " (" + o.code + ") \u2014 " + o.description + " Category: " + o.category + ".";

        // 3) specific office
        for (Office o : offices) {
            String key = o.name.split("[^A-Za-z]")[0].toLowerCase(Locale.ENGLISH);
            if (w.contains(key)) return o.name + " \u2014 " + o.description;
        }

        // 4) specific event
        for (EventItem e : events)
            if (q.contains(e.name.toLowerCase(Locale.ENGLISH)))
                return e.name + " (" + e.type + ") \u2014 " + e.description;

        // 5) specific FAQ
        for (FAQ f : faqs) {
            if (q.contains(f.question.toLowerCase(Locale.ENGLISH))) return f.answer;
            for (String k : f.keywords) if (w.contains(k)) return f.question + ": " + f.answer;
        }

        // 6) general topics
        if (has(w, "course", "courses", "program", "programs", "offered", "majors", "major")) {
            if (courses.isEmpty()) return "No course information is currently available.";
            StringBuilder sb = new StringBuilder("LLCC courses available:\n");
            for (Course c : courses) sb.append("\u2022 ").append(c.code).append(" \u2014 ").append(c.name).append("\n");
            sb.append("\nOpen the Courses page, or ask about a specific course such as \"Tell me about ")
              .append(courses.get(0).code).append("\".");
            return sb.toString();
        }
        if (has(w, "admission", "admit", "enroll", "enrollment", "apply", "requirement", "requirements"))
            return "For admission/enrollment, review the current requirements and follow the current school process. Always verify deadlines, fees, forms, and requirements with the appropriate LLCC office (usually the Registrar).";
        if (has(w, "organization", "organizations", "club", "clubs", "org")) {
            StringBuilder sb = new StringBuilder("Listed organizations:\n");
            for (Organization o : organizations) sb.append("\u2022 ").append(o.code).append(" \u2014 ").append(o.name).append("\n");
            return sb.toString().trim();
        }
        if (has(w, "event", "events", "intramural", "intramurals", "party", "clash")) {
            StringBuilder sb = new StringBuilder("Listed events:\n");
            for (EventItem e : events) sb.append("\u2022 ").append(e.name).append("\n");
            return sb.toString().trim();
        }
        if (has(w, "office", "offices", "where")) {
            StringBuilder sb = new StringBuilder("Office directory:\n");
            for (Office o : offices) sb.append("\u2022 ").append(o.name).append(" \u2014 ").append(o.description).append("\n");
            return sb.toString().trim();
        }
        if (has(w, "document", "documents", "transcript", "scholarship", "papers", "forms")
                || (w.contains("graduation") && has(w, "requirement", "requirements"))) {
            StringBuilder sb = new StringBuilder("Document guides:\n");
            for (DocumentItem d : documents) sb.append("\u2022 ").append(d.name).append("\n");
            return sb.toString().trim();
        }
        if (has(w, "calendar", "schedule", "midterm", "midterms", "final", "finals", "graduation", "exam", "exams")) {
            StringBuilder sb = new StringBuilder("Calendar items:\n");
            for (CalendarItem c : calendar) sb.append("\u2022 ").append(c.name).append(" \u2014 ").append(c.description).append("\n");
            return sb.toString().trim();
        }
        if (has(w, "faq", "faqs", "question", "questions", "topics")) {
            StringBuilder sb = new StringBuilder("Frequently asked topics:\n");
            for (FAQ f : faqs) sb.append("\u2022 ").append(f.question).append("\n");
            sb.append("\nAsk about any of these for the answer.");
            return sb.toString();
        }
        if (has(w, "hello", "hi", "hey"))
            return "Hello! Ask me about courses, admission, organizations, events, offices, documents, calendar, or FAQs.";
        if (has(w, "thanks", "thank"))
            return "You're welcome! Let me know if you need anything else about LLCC.";

        return "I can help with LLCC courses, admission, organizations, events, offices, documents, calendar, and FAQs. Try asking: \"What courses are offered?\", \"Where is the Registrar?\", or \"What documents are listed?\"";
    }

    private String courseAnswer(Course c) {
        StringBuilder sb = new StringBuilder();
        sb.append(c.name).append(" (").append(c.code).append(")\n");
        sb.append(c.category).append("\n\n").append(c.summary);
        if (c.majors.length > 0) sb.append("\n\nMajors: ").append(String.join(", ", c.majors));
        if (c.careers.length > 0) sb.append("\n\nPossible careers: ").append(String.join(", ", c.careers));
        return sb.toString();
    }

    // ===================== HELPERS =====================
    private void showPage(String name) {
        contentLayout.show(content, name);
        for (Map.Entry<String, NavButton> e : navButtons.entrySet()) e.getValue().setActive(e.getKey().equals(name));
        statusLabel.setText("LLCCIA \u2022 " + name);
    }

    /** Wrapped, always-visible text label (HTML with fixed width so it never gets cut off). */
    private JLabel text(String s, int size, int style, Color color, int width) {
        JLabel l = new JLabel("<html><div style='width:" + width + "px'>" + escape(s).replace("\n", "<br>") + "</div></html>");
        l.setFont(new Font(FONT, style, size));
        l.setForeground(color);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JLabel sectionLabel(String s) {
        JLabel l = new JLabel(s);
        l.setFont(new Font(FONT, Font.BOLD, 12));
        l.setForeground(GOLD);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JLabel pill(String text, Color bg, Color fg) {
        JLabel l = new JLabel(text);
        l.setOpaque(true);
        l.setBackground(bg);
        l.setForeground(fg);
        l.setFont(new Font(FONT, Font.BOLD, 11));
        l.setBorder(new EmptyBorder(4, 9, 4, 9));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JPanel infoCard(String n, String title, String desc) {
        Card c = new Card(GOLD);
        c.add(pill("STEP " + n, GOLD_PALE, NAVY));
        c.add(Box.createVerticalStrut(8));
        c.add(text(title, 17, Font.BOLD, NAVY, W2));
        c.add(Box.createVerticalStrut(4));
        c.add(text(desc, 13, Font.PLAIN, INK, W2));
        return c;
    }

    private JPanel clickableCard(String title, String desc) {
        Card c = new Card(GOLD);
        c.add(text(title, 17, Font.BOLD, NAVY, W3));
        c.add(Box.createVerticalStrut(4));
        c.add(text(desc, 12, Font.PLAIN, INK_SOFT, W3));
        c.add(Box.createVerticalStrut(8));
        c.add(text("Open \u2192", 12, Font.BOLD, GOLD, W3));
        c.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        c.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { showPage(title); }
            @Override public void mouseEntered(MouseEvent e) { c.setBackground(GOLD_PALE); }
            @Override public void mouseExited(MouseEvent e) { c.setBackground(PAPER); }
        });
        return c;
    }

    private static String escape(String s) {
        return s == null ? "" : s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    // ===================== CUSTOM COMPONENTS =====================

    /** Panel whose maximum height equals its preferred height (prevents stretching inside BoxLayout). */
    static class FitPanel extends JPanel {
        FitPanel(LayoutManager lm) { super(lm); }
        @Override public Dimension getMaximumSize() {
            return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
        }
    }

    /** White card with a coloured top accent strip. */
    static class Card extends FitPanel {
        Card(Color accent) {
            super(null);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setOpaque(true);
            setBackground(PAPER);
            setBorder(new CompoundBorder(new LineBorder(LINE, 1),
                    new CompoundBorder(new MatteBorder(4, 0, 0, 0, accent), new EmptyBorder(14, 16, 16, 16))));
            setAlignmentX(Component.LEFT_ALIGNMENT);
        }
    }

    /** Page panel that always matches the width of the scroll viewport (so layouts never need a horizontal scrollbar). */
    static class ScrollablePanel extends JPanel implements Scrollable {
        public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
        public int getScrollableUnitIncrement(Rectangle r, int o, int d) { return 18; }
        public int getScrollableBlockIncrement(Rectangle r, int o, int d) { return 80; }
        public boolean getScrollableTracksViewportWidth() { return true; }
        public boolean getScrollableTracksViewportHeight() { return false; }
    }

    /** Fully custom-painted button so colours/text are always visible on every look-and-feel. */
    static class AppButton extends JButton {
        private Color bg, fg, border;
        private boolean hover;

        AppButton(String text, Color bg, Color fg, Color border) {
            super(text);
            this.bg = bg; this.fg = fg; this.border = border;
            setFont(new Font(FONT, Font.BOLD, 12));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                @Override public void mouseExited(MouseEvent e) { hover = false; repaint(); }
            });
        }

        void setStyle(Color bg, Color fg, Color border) {
            this.bg = bg; this.fg = fg; this.border = border; repaint();
        }

        private static Color shade(Color c) {
            int lum = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
            if (lum > 200) return new Color(Math.max(0, c.getRed() - 18), Math.max(0, c.getGreen() - 18), Math.max(0, c.getBlue() - 18));
            return new Color(Math.min(255, c.getRed() + 25), Math.min(255, c.getGreen() + 25), Math.min(255, c.getBlue() + 25));
        }

        @Override public Dimension getPreferredSize() {
            FontMetrics fm = getFontMetrics(getFont());
            return new Dimension(fm.stringWidth(getText()) + 32, 34);
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            g2.setColor(hover ? shade(bg) : bg);
            g2.fillRoundRect(0, 0, w - 1, h - 1, 10, 10);
            if (border != null) {
                g2.setColor(border);
                g2.drawRoundRect(0, 0, w - 1, h - 1, 10, 10);
            }
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            g2.setColor(fg);
            int x = (w - fm.stringWidth(getText())) / 2;
            int y = (h - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(getText(), x, y);
            g2.dispose();
        }
    }

    /** Sidebar navigation button with active/hover states. */
    static class NavButton extends JButton {
        private boolean active, hover;

        NavButton(String text) {
            super(text);
            setFont(new Font(FONT, Font.BOLD, 14));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setHorizontalAlignment(SwingConstants.LEFT);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                @Override public void mouseExited(MouseEvent e) { hover = false; repaint(); }
            });
        }

        void setActive(boolean a) { active = a; repaint(); }

        @Override public Dimension getPreferredSize() { return new Dimension(186, 42); }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            if (active || hover) {
                g2.setColor(active ? NAVY_SOFT : new Color(26, 56, 90));
                g2.fillRoundRect(0, 0, w, h, 10, 10);
            }
            if (active) {
                g2.setColor(GOLD);
                g2.fillRoundRect(0, 6, 5, h - 12, 4, 4);
            }
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            g2.setColor(active ? GOLD_SOFT : Color.WHITE);
            g2.drawString(getText(), 18, (h - fm.getHeight()) / 2 + fm.getAscent());
            g2.dispose();
        }
    }

    // ===================== MODELS =====================
    static class Course {
        final String code, name, category, summary;
        final String[] majors, careers;
        Course(String code, String name, String category, String summary, String[] majors, String[] careers) {
            this.code = code == null ? "" : code;
            this.name = name == null ? "" : name;
            this.category = category == null ? "Other" : category;
            this.summary = summary == null ? "" : summary;
            this.majors = majors; this.careers = careers;
        }
    }
    static class Organization {
        final String code, name, category, description; final Color accent;
        Organization(String code, String name, String category, Color accent, String description) {
            this.code = code; this.name = name; this.category = category; this.accent = accent; this.description = description; }
    }
    static class EventItem {
        final String name, description, type;
        EventItem(String name, String description, String type) { this.name = name; this.description = description; this.type = type; }
    }
    static class Office {
        final String name, description, role;
        Office(String name, String description, String role) { this.name = name; this.description = description; this.role = role; }
    }
    static class DocumentItem {
        final String name, description;
        DocumentItem(String name, String description) { this.name = name; this.description = description; }
    }
    static class CalendarItem {
        final String name, description;
        CalendarItem(String name, String description) { this.name = name; this.description = description; }
    }
    static class FAQ {
        final String question, answer; final String[] keywords;
        FAQ(String question, String answer, String... keywords) { this.question = question; this.answer = answer; this.keywords = keywords; }
    }

    // ===================== MAIN =====================
    public static void main(String[] args) {
        try {
            // Cross-platform look & feel so our colours are never overridden by the OS theme
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) { }
        SwingUtilities.invokeLater(() -> new LLCIA().setVisible(true));
    }
}