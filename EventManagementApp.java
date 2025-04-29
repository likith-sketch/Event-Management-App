import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class EventManagementApp extends JFrame {
    // Database credentials
    static final String DB_URL = "jdbc:mysql://localhost:3306/EventManagement"; // Replace `eventdb` with your DB name
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "Likith@123";

    Connection conn;

    // Constructor
    public EventManagementApp() {
        setTitle("Event Management System");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Try to connect to DB
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Connected to database.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage());
            System.exit(1);
        }

        // UI Components
        JLabel title = new JLabel("Event Management System", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        JButton addUserBtn = new JButton("Add User");
        JButton addEventBtn = new JButton("Add Event");
        JButton viewUsersBtn = new JButton("View All Users");
        JButton viewEventsBtn = new JButton("View All Events");

        panel.add(addUserBtn);
        panel.add(addEventBtn);
        panel.add(viewUsersBtn);
        panel.add(viewEventsBtn);
        add(panel, BorderLayout.CENTER);

        // Button Actions
        addUserBtn.addActionListener(e -> showAddUserDialog());
        addEventBtn.addActionListener(e -> showAddEventDialog());
        viewUsersBtn.addActionListener(e -> showUserList());
        viewEventsBtn.addActionListener(e -> showEventList());

        setVisible(true);
    }

    private void showAddUserDialog() {
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField passField = new JTextField();

        Object[] message = {
                "Name:", nameField,
                "Email:", emailField,
                "Password:", passField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add User", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String sql = "INSERT INTO User (name, email, password) VALUES (?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, nameField.getText());
                stmt.setString(2, emailField.getText());
                stmt.setString(3, passField.getText());
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "User added successfully.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void showAddEventDialog() {
        JTextField eventNameField = new JTextField();
        JTextField descriptionField = new JTextField();

        Object[] message = {
                "Event Name:", eventNameField,
                "Description:", descriptionField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add Event", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String sql = "INSERT INTO Event (event_name, description) VALUES (?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, eventNameField.getText());
                stmt.setString(2, descriptionField.getText());
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Event added successfully.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void showUserList() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM User");

            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("ID: ").append(rs.getInt("user_id"))
                  .append(", Name: ").append(rs.getString("name"))
                  .append(", Email: ").append(rs.getString("email")).append("\n");
            }

            JOptionPane.showMessageDialog(this, new JScrollPane(new JTextArea(sb.toString(), 15, 30)), "All Users", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error retrieving users: " + e.getMessage());
        }
    }

    private void showEventList() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Event");

            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("ID: ").append(rs.getInt("event_id"))
                  .append(", Name: ").append(rs.getString("event_name"))
                  .append(", Desc: ").append(rs.getString("description")).append("\n");
            }

            JOptionPane.showMessageDialog(this, new JScrollPane(new JTextArea(sb.toString(), 15, 30)), "All Events", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error retrieving events: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Load MySQL JDBC Driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "MySQL JDBC Driver not found.");
            return;
        }

        SwingUtilities.invokeLater(() -> new EventManagementApp());
    }
}
