package com.studentdb.controller;

import com.studentdb.dao.StudentDAO;
import com.studentdb.model.Student;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class StudentController {

    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, String> colRoll;
    @FXML private TableColumn<Student, String> colName;
    @FXML private TableColumn<Student, String> colDept;
    @FXML private TableColumn<Student, Double> colCgpa;

    @FXML private TextField searchField;

    @FXML private TextField rollField;
    @FXML private TextField nameField;
    @FXML private DatePicker dobPicker;
    @FXML private ComboBox<String> genderCombo;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private ComboBox<String> deptCombo;
    @FXML private TextField yearField;
    @FXML private TextArea addressArea;
    @FXML private TextField cgpaField;

    @FXML private Label statusLabel;

    private StudentDAO studentDAO;
    private ObservableList<Student> studentList;
    private Student selectedStudent = null;

    public StudentController() {
        this.studentDAO = new StudentDAO();
        this.studentList = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        colRoll.setCellValueFactory(new PropertyValueFactory<>("rollNumber"));
        colName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colDept.setCellValueFactory(new PropertyValueFactory<>("department"));
        colCgpa.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getCgpa()).asObject());

        studentTable.setItems(studentList);

        genderCombo.setItems(FXCollections.observableArrayList("Male", "Female", "Other"));
        deptCombo.setItems(FXCollections.observableArrayList("Computer Science", "Electrical", "Mechanical", "Civil", "Business"));

        studentTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                populateForm(newSel);
            }
        });

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            searchStudents(newVal);
        });

        refreshTable();
    }

    public void refreshTable() {
        try {
            List<Student> students = studentDAO.getAllStudents();
            studentList.setAll(students);
            statusLabel.setText("Records loaded successfully.");
            statusLabel.setStyle("-fx-text-fill: green;");
        } catch (SQLException e) {
            e.printStackTrace();
            statusLabel.setText("Error loading records.");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    private void searchStudents(String query) {
        if (query == null || query.trim().isEmpty()) {
            refreshTable();
            return;
        }
        try {
            List<Student> students = studentDAO.searchStudents(query.trim());
            studentList.setAll(students);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateForm(Student s) {
        selectedStudent = s;
        rollField.setText(s.getRollNumber());
        nameField.setText(s.getFullName());
        if (s.getDob() != null && !s.getDob().isEmpty()) {
            try {
                dobPicker.setValue(java.time.LocalDate.parse(s.getDob()));
            } catch (Exception e) {}
        } else {
            dobPicker.setValue(null);
        }
        genderCombo.setValue(s.getGender());
        emailField.setText(s.getEmail());
        phoneField.setText(s.getPhone());
        deptCombo.setValue(s.getDepartment());
        yearField.setText(String.valueOf(s.getYearOfStudy()));
        addressArea.setText(s.getAddress());
        cgpaField.setText(String.valueOf(s.getCgpa()));
    }

    @FXML
    public void clearForm() {
        selectedStudent = null;
        rollField.clear();
        nameField.clear();
        dobPicker.setValue(null);
        genderCombo.setValue(null);
        emailField.clear();
        phoneField.clear();
        deptCombo.setValue(null);
        yearField.clear();
        addressArea.clear();
        cgpaField.clear();
        studentTable.getSelectionModel().clearSelection();
        statusLabel.setText("");
    }

    @FXML
    private void handleSave() {
        if (!validateInput()) {
            return;
        }

        try {
            int excludeId = (selectedStudent == null) ? -1 : selectedStudent.getId();
            if (studentDAO.rollNumberExists(rollField.getText().trim(), excludeId)) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Roll Number already exists!");
                return;
            }

            Student s = new Student();
            if (selectedStudent != null) {
                s.setId(selectedStudent.getId());
            }
            s.setRollNumber(rollField.getText().trim());
            s.setFullName(nameField.getText().trim());
            s.setDob(dobPicker.getValue() != null ? dobPicker.getValue().toString() : "");
            s.setGender(genderCombo.getValue());
            s.setEmail(emailField.getText().trim());
            s.setPhone(phoneField.getText().trim());
            s.setDepartment(deptCombo.getValue());
            s.setYearOfStudy(Integer.parseInt(yearField.getText().trim()));
            s.setAddress(addressArea.getText().trim());
            s.setCgpa(Double.parseDouble(cgpaField.getText().trim()));

            if (selectedStudent == null) {
                studentDAO.addStudent(s);
                statusLabel.setText("Student added successfully!");
            } else {
                studentDAO.updateStudent(s);
                statusLabel.setText("Student updated successfully!");
            }
            statusLabel.setStyle("-fx-text-fill: green;");
            refreshTable();
            clearForm();
            
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to save student.");
        }
    }

    @FXML
    private void handleDelete() {
        if (selectedStudent == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a student to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Confirmation");
        confirm.setHeaderText(null);
        confirm.setContentText("Are you sure you want to delete student: " + selectedStudent.getFullName() + "?");
        Optional<ButtonType> result = confirm.showAndWait();
        
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                studentDAO.deleteStudent(selectedStudent.getId());
                statusLabel.setText("Student deleted successfully!");
                statusLabel.setStyle("-fx-text-fill: green;");
                refreshTable();
                clearForm();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to delete student.");
            }
        }
    }

    private boolean validateInput() {
        if (rollField.getText().trim().isEmpty() || nameField.getText().trim().isEmpty() ||
            deptCombo.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Roll Number, Name, and Department are required.");
            return false;
        }

        String email = emailField.getText().trim();
        if (!email.isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid email format.");
            return false;
        }

        String phone = phoneField.getText().trim();
        if (!phone.isEmpty() && !phone.matches("\\d{7,15}")) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid phone number.");
            return false;
        }

        try {
            int year = Integer.parseInt(yearField.getText().trim());
            if (year < 1 || year > 5) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Year of study must be between 1 and 5.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Year of study must be an integer.");
            return false;
        }

        try {
            double cgpa = Double.parseDouble(cgpaField.getText().trim());
            if (cgpa < 0.0 || cgpa > 10.0) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "CGPA must be between 0.0 and 10.0.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "CGPA must be a valid number.");
            return false;
        }

        return true;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
