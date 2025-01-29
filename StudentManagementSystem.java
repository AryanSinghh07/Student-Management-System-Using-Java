import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class StudentManagementSystem {
    private JFrame loginFrame, registerFrame, mainFrame;
    private JTextField usernameField, registerUsernameField;
    private JPasswordField passwordField, registerPasswordField;
    private File userFile = new File("users.csv");

    private JTextField nameField, contactField, emailField, rollnoField, branchField, searchField;
    private JTable table;
    private DefaultTableModel tableModel;
    private final File dataFile = new File("students.csv");
    private final File attendanceFile = new File("attendance.csv");
    private final File resultFile = new File("results.csv"); // New file for results

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentManagementSystem::new);
    }

    public StudentManagementSystem() {
        initializeUserFile();
        initializeAttendanceFile();
        initializeResultFile(); // Initialize the result file
        showLoginFrame();
    }

    private void initializeUserFile() {
        try {
            if (!userFile.exists()) {
                try (PrintWriter writer = new PrintWriter(new FileWriter(userFile))) {
                    writer.println("Username,Password");
                    writer.println("admin,admin"); // Default admin account
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error initializing user file: " + e.getMessage());
        }
    }

    private void initializeAttendanceFile() {
        try {
            if (!attendanceFile.exists()) {
                try (PrintWriter writer = new PrintWriter(new FileWriter(attendanceFile))) {
                    writer.println("Student ID,Name,Date,Attendance");
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error initializing attendance file: " + e.getMessage());
        }
    }

    private void initializeResultFile() {
        try {
            if (!resultFile.exists()) {
                try (PrintWriter writer = new PrintWriter(new FileWriter(resultFile))) {
                    writer.println("Student ID,Subject1,Subject2,Subject3,Subject4,Subject5");
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error initializing result file: " + e.getMessage());
        }
    }

    private void showLoginFrame() {
        loginFrame = new JFrame("Login");
        loginFrame.setSize(400, 300);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLayout(new GridLayout(4, 2, 10, 10));

        loginFrame.add(new JLabel("Username:"));
        usernameField = new JTextField();
        loginFrame.add(usernameField);

        loginFrame.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        loginFrame.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> loginUser ());
        loginFrame.add(loginButton);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> showRegisterFrame());
        loginFrame.add(registerButton);

        loginFrame.setVisible(true);
    }

    private void showRegisterFrame() {
        registerFrame = new JFrame("Register");
        registerFrame.setSize(400, 300);
        registerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        registerFrame.setLayout(new GridLayout(4, 2, 10, 10));

        registerFrame.add(new JLabel("Username:"));
        registerUsernameField = new JTextField();
        registerFrame.add(registerUsernameField);

        registerFrame.add(new JLabel("Password:"));
        registerPasswordField = new JPasswordField();
        registerFrame.add(registerPasswordField);

        JButton registerSubmitButton = new JButton("Submit");
        registerSubmitButton.addActionListener(e -> registerUser ());
        registerFrame.add(registerSubmitButton);

        registerFrame.setVisible(true);
    }

    private void registerUser () {
        String username = registerUsernameField.getText();
        String password = new String(registerPasswordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(registerFrame, "Please fill all fields!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userFile, true))) {
            writer.write(username + "," + password);
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(registerFrame, "Error saving user data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(registerFrame, "User  registered successfully!");
        registerFrame.dispose();
    }

    private void loginUser () {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try (BufferedReader reader = new BufferedReader(new FileReader(userFile))) {
            String line;
            boolean loggedIn = false;

            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials[0].equals(username) && credentials[1].equals(password)) {
                    loggedIn = true;
                    break;
                }
            }

            if (loggedIn) {
                JOptionPane.showMessageDialog(loginFrame, "Login successful!");
                loginFrame.dispose();
                initializeFile();
                initializeMainGUI();
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Invalid username or password!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(loginFrame, "Error reading user data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initializeFile() {
        try {
            if (!dataFile.exists()) {
                try (PrintWriter writer = new PrintWriter(new FileWriter(dataFile))) {
                    writer.println("Student ID,Name,Contact,Email,Rollno,Branch");
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error initializing data file: " + e.getMessage());
        }
    }

    private void initializeMainGUI() {
        mainFrame = new JFrame("Student Management System");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1000, 600);
        mainFrame.setLayout(new BorderLayout());

        // Title Panel
        JLabel titleLabel = new JLabel("Student Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 18));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(28, 40, 51));
        titleLabel.setForeground(Color.WHITE);
        mainFrame.add(titleLabel, BorderLayout.NORTH);

        // Left Panel: Input Form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.setPreferredSize(new Dimension(300, 0));

        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Contact:"));
        contactField = new JTextField();
        formPanel.add(contactField);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("Rollno:"));
        rollnoField = new JTextField();
        formPanel.add(rollnoField);

        formPanel.add(new JLabel("Branch:"));
        branchField = new JTextField();
        formPanel.add(branchField);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> registerStudent());
        formPanel.add(submitButton);

        mainFrame.add(formPanel, BorderLayout.WEST);

        // Center Panel: Action Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(8, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.setBackground(Color.LIGHT_GRAY);

        searchField = new JTextField();
        buttonPanel.add(new JLabel("Enter name to Search:"));
        buttonPanel.add(searchField);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchStudent());
        buttonPanel.add(searchButton);

        JButton viewButton = new JButton("View All");
        viewButton.addActionListener(e -> displayAllStudents());
        buttonPanel.add(viewButton);

        JButton editButton = new JButton("Edit");
        editButton.addActionListener(e -> editStudent());
        buttonPanel.add(editButton);

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteStudent());
        buttonPanel.add(deleteButton);

        JButton markAttendanceButton = new JButton("Mark Attendance");
        markAttendanceButton.addActionListener(e -> markAttendance());
        buttonPanel.add(markAttendanceButton);

        JButton manageResultsButton = new JButton("Manage Results");
        manageResultsButton.addActionListener(e -> manageResults());
        buttonPanel.add(manageResultsButton);

        JButton viewMarksheetsButton = new JButton("View Marksheets");
        viewMarksheetsButton.addActionListener(e -> showMarksheetsPage());
        buttonPanel.add(viewMarksheetsButton);
        viewMarksheetsButton.addActionListener(e -> showMarksheetsPage());

        mainFrame.add(buttonPanel, BorderLayout.CENTER);

        // Right Panel: Table Display
        JPanel tablePanel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(new String[]{"Student ID", "Name", "Contact", "Email", "Rollno", "Branch"}, 0);
        table = new JTable(tableModel);

        JScrollPane tableScrollPane = new JScrollPane(table);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);
        mainFrame.add(tablePanel, BorderLayout.EAST);

        // Display All Students on Startup
        displayAllStudents();

        mainFrame.setVisible(true);
    }

    private void showMarksheetsPage() {
        JFrame marksheetFrame = new JFrame("Student Marksheets");
        marksheetFrame.setSize(800, 600);
        marksheetFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        marksheetFrame.setLayout(new BorderLayout());

        // Title Panel
        JLabel titleLabel = new JLabel("Student Marksheets", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 18));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(28, 40, 51));
        titleLabel.setForeground(Color.WHITE);
        marksheetFrame.add(titleLabel, BorderLayout.NORTH);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        DefaultTableModel marksheetTableModel = new DefaultTableModel(new String[]{"Student ID", "Name", "Subject 1", "Subject 2", "Subject 3", "Subject 4", "Subject 5"}, 0);
        JTable marksheetTable = new JTable(marksheetTableModel);

        JScrollPane tableScrollPane = new JScrollPane(marksheetTable);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);
        marksheetFrame.add(tablePanel, BorderLayout.CENTER);

        // Load Marksheet Data
        loadMarksheetData(marksheetTableModel);

        marksheetFrame.setVisible(true);
    }

    private void loadMarksheetData(DefaultTableModel marksheetTableModel) {
        try (BufferedReader reader = new BufferedReader(new FileReader(resultFile))) {
            String line;
            boolean isHeader = true;
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                String[] data = line.split(",");
                String studentId = data[0];
                String studentName = getStudentNameById(studentId);
                marksheetTableModel.addRow(new Object[]{studentId, studentName, data[1], data[2], data[3], data[4], data[5]});
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading marksheet data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getStudentNameById(String studentId) {
        try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
            String line;
            boolean isHeader = true;
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                String[] data = line.split(",");
                if (data[0].equals(studentId)) {
                    return data[1];
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error fetching student name: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return "Unknown";
    }

    private void editStudent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(mainFrame, "Select a record to edit!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get the selected student's data
        String studentId = (String) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);
        String contact = (String) tableModel.getValueAt(selectedRow, 2);
        String email = (String) tableModel.getValueAt(selectedRow, 3);
        String rollno = (String) tableModel.getValueAt(selectedRow, 4);
        String branch = (String) tableModel.getValueAt(selectedRow, 5);

        // Create a new dialog for editing
        JDialog editDialog = new JDialog(mainFrame, "Edit Student", true);
        editDialog.setSize(300, 300);
        editDialog.setLayout(new GridLayout(6, 2,  10, 10));

        editDialog.add(new JLabel("Name:"));
        JTextField editNameField = new JTextField(name);
        editDialog.add(editNameField);

        editDialog.add(new JLabel("Contact:"));
        JTextField editContactField = new JTextField(contact);
        editDialog.add(editContactField);

        editDialog.add(new JLabel("Email:"));
        JTextField editEmailField = new JTextField(email);
        editDialog.add(editEmailField);

        editDialog.add(new JLabel("Rollno:"));
        JTextField editRollnoField = new JTextField(rollno);
        editDialog.add(editRollnoField);

        editDialog.add(new JLabel("Branch:"));
        JTextField editBranchField = new JTextField(branch);
        editDialog.add(editBranchField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            // Update the student data
            try {
                String updatedName = editNameField.getText();
                String updatedContact = editContactField.getText();
                String updatedEmail = editEmailField.getText();
                String updatedRollno = editRollnoField.getText();
                String updatedBranch = editBranchField.getText();

                if (updatedName.isEmpty() || updatedContact.isEmpty() || updatedEmail.isEmpty() || updatedRollno.isEmpty() || updatedBranch.isEmpty()) {
                    JOptionPane.showMessageDialog(editDialog, "Please fill all fields!", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Update the data file
                File tempFile = new File("temp.csv");
                try (BufferedReader reader = new BufferedReader(new FileReader(dataFile));
                     PrintWriter writer = new PrintWriter(new FileWriter(tempFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith(studentId + ",")) {
                            writer.println(studentId + "," + updatedName + "," + updatedContact + "," + updatedEmail + "," + updatedRollno + "," + updatedBranch);
                        } else {
                            writer.println(line);
                        }
                    }
                }

                dataFile.delete();
                tempFile.renameTo(dataFile);

                JOptionPane.showMessageDialog(mainFrame, "Student data updated successfully!");
                displayAllStudents();
                editDialog.dispose();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(mainFrame, "Error updating student data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        editDialog.add(saveButton);
        editDialog.setVisible(true);
    }

    private void markAttendance() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(mainFrame, "Select a student to mark attendance!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String studentId = (String) tableModel.getValueAt(selectedRow, 0);
        String studentName = (String) tableModel.getValueAt(selectedRow, 1);
        String date = new java.util.Date().toString();
        String[] options = {"Present", "Absent"};
        int response = JOptionPane.showOptionDialog(mainFrame, "Mark attendance for " + studentName + ":", "Attendance",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (response == -1) {
            return; // No selection
        }

        String attendance = options[response];
        try (PrintWriter writer = new PrintWriter(new FileWriter(attendanceFile, true))) {
            writer.println(studentId + "," + studentName + "," + date + "," + attendance);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(mainFrame, "Error marking attendance: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        JOptionPane.showMessageDialog(mainFrame, "Attendance marked for " + studentName + " as " + attendance + "!");
    }

    private void registerStudent() {
        try {
            String name = nameField.getText();
            String contact = contactField.getText();
            String email = emailField.getText();
            String rollno = rollnoField.getText();
            String branch = branchField.getText();

            if (name.isEmpty() || contact.isEmpty() || email.isEmpty() || rollno.isEmpty() || branch.isEmpty()) {
                JOptionPane.showMessageDialog(mainFrame, "Please fill all fields!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int studentId = (int) (System.currentTimeMillis() / 1000);

            try (PrintWriter writer = new PrintWriter(new FileWriter(dataFile, true))) {
                writer.println(studentId + "," + name + "," + contact + "," + email + "," + rollno + "," + branch );
            }

            JOptionPane.showMessageDialog(mainFrame, "Student registered successfully!");
            displayAllStudents();
            resetForm();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(mainFrame, "Error saving student data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetForm() {
        nameField.setText("");
        contactField.setText("");
        emailField.setText("");
        rollnoField.setText("");
        branchField.setText("");
        searchField.setText("");
        displayAllStudents();
    }

    private void deleteStudent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(mainFrame, "Select a record to delete!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String studentId = (String) tableModel.getValueAt(selectedRow, 0);
            File tempFile = new File("temp.csv");
            try (BufferedReader reader = new BufferedReader(new FileReader(dataFile));
                 PrintWriter writer = new PrintWriter(new FileWriter(tempFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.startsWith(studentId + ",")) {
                        writer.println(line);
                    }
                }
            }

            dataFile.delete();
            tempFile.renameTo(dataFile);

            JOptionPane.showMessageDialog(mainFrame, "Record deleted successfully!");
            displayAllStudents();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(mainFrame, "Error deleting student data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchStudent() {
        String searchText = searchField.getText();
        if (searchText.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Enter a name to search!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            tableModel.setRowCount(0);
            try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
                String line;
                boolean isHeader = true;
                while ((line = reader.readLine()) != null) {
                    if (isHeader) {
                        isHeader = false;
                        continue;
                    }
                    String[] data = line.split(",");
                    if (data[1].toLowerCase().contains(searchText.toLowerCase())) {
                        tableModel.addRow(data);
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(mainFrame, "Error searching student data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayAllStudents() {
        try {
            tableModel.setRowCount(0);
            try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
                String line;
                boolean isHeader = true;
                while ((line = reader.readLine()) != null) {
                    if (isHeader) {
                        isHeader = false;
                        continue;
                    }
                    tableModel.addRow(line.split(","));
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(mainFrame, "Error displaying student data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void manageResults() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(mainFrame, "Select a student to manage results!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String studentId = (String) tableModel.getValueAt(selectedRow, 0);
        String studentName = (String) tableModel.getValueAt(selectedRow, 1);

        // Check if the student already has results
        String[] existingMarks = getExistingMarks(studentId);

        // Create a dialog for entering/editing marks
        JDialog resultDialog = new JDialog(mainFrame, "Manage Results for " + studentName, true);
        resultDialog.setSize(400, 300);
        resultDialog.setLayout(new GridLayout(6, 2, 10, 10));

        resultDialog.add(new JLabel("Subject 1:"));
        JTextField subject1Field = new JTextField(existingMarks != null ? existingMarks[1] : "");
        resultDialog.add(subject1Field);

        resultDialog.add(new JLabel("Subject 2:"));
        JTextField subject2Field = new JTextField(existingMarks != null ? existingMarks[2] : "");
        resultDialog.add(subject2Field);

        resultDialog.add(new JLabel("Subject 3:"));
        JTextField subject3Field = new JTextField(existingMarks != null ? existingMarks[3] : "");
        resultDialog.add(subject3Field);

resultDialog.add(new JLabel("Subject 4:"));
JTextField subject4Field = new JTextField(existingMarks != null ? existingMarks[4] : "");
resultDialog.add(subject4Field);

        resultDialog.add(new JLabel("Subject 5:"));
        JTextField subject5Field = new JTextField(existingMarks != null ? existingMarks[5] : "");
        resultDialog.add(subject5Field);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String subject1 = subject1Field.getText();
            String subject2 = subject2Field.getText();
            String subject3 = subject3Field.getText();
            String subject4 = subject4Field.getText();
            String subject5 = subject5Field.getText();

            if (subject1.isEmpty() || subject2.isEmpty() || subject3.isEmpty() || subject4.isEmpty() || subject5.isEmpty()) {
                JOptionPane.showMessageDialog(resultDialog, "Please fill all fields!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            saveResults(studentId, subject1, subject2, subject3, subject4, subject5);
            resultDialog.dispose();
        });
        resultDialog.add(saveButton);

        resultDialog.setVisible(true);
    }

    private String[] getExistingMarks(String studentId) {
        try (BufferedReader reader = new BufferedReader(new FileReader(resultFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(studentId)) {
                    return data; // Return the marks for the student
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(mainFrame, "Error reading results: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null; // No existing marks found
    }

    private void saveResults(String studentId, String subject1, String subject2, String subject3, String subject4, String subject5) {
        try {
            File tempFile = new File("temp_results.csv");
            try (BufferedReader reader = new BufferedReader(new FileReader(resultFile));
                 PrintWriter writer = new PrintWriter(new FileWriter(tempFile))) {
                String line;
                boolean updated = false;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data[0].equals(studentId)) {
                        writer.println(studentId + "," + subject1 + "," + subject2 + "," + subject3 + "," + subject4 + "," + subject5);
                        updated = true; // Mark as updated
                    } else {
                        writer.println(line);
                    }
                }
                if (!updated) {
                    // If no existing record, add a new one
                    writer.println(studentId + "," + subject1 + "," + subject2 + "," + subject3 + "," + subject4 + "," + subject5);
                }
            }

            resultFile.delete();
            tempFile.renameTo(resultFile);

            JOptionPane.showMessageDialog(mainFrame, "Results saved successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(mainFrame, "Error saving results: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}