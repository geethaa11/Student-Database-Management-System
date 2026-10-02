package com.studentdb.dao;

import com.studentdb.database.DatabaseConnection;
import com.studentdb.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentDAO {

    public void addStudent(Student student) throws SQLException {
        String sql = "INSERT INTO students (roll_number, full_name, dob, gender, email, phone, department, year_of_study, address, cgpa) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            setStudentParams(pstmt, student);
            pstmt.executeUpdate();
        }
    }

    public void updateStudent(Student student) throws SQLException {
        String sql = "UPDATE students SET roll_number=?, full_name=?, dob=?, gender=?, email=?, phone=?, department=?, year_of_study=?, address=?, cgpa=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            setStudentParams(pstmt, student);
            pstmt.setInt(11, student.getId());
            pstmt.executeUpdate();
        }
    }

    public void deleteStudent(int id) throws SQLException {
        String sql = "DELETE FROM students WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public List<Student> getAllStudents() throws SQLException {
        return getStudentsByQuery("SELECT * FROM students");
    }

    public List<Student> searchStudents(String query) throws SQLException {
        String sql = "SELECT * FROM students WHERE full_name LIKE ? OR roll_number LIKE ? OR department LIKE ?";
        String param = "%" + query + "%";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, param);
            pstmt.setString(2, param);
            pstmt.setString(3, param);
            return fetchStudentsFromResultSet(pstmt.executeQuery());
        }
    }

    public boolean rollNumberExists(String rollNumber, int excludeId) throws SQLException {
        String sql = "SELECT count(*) FROM students WHERE roll_number = ? AND id != ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, rollNumber);
            pstmt.setInt(2, excludeId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public int getTotalStudents() throws SQLException {
        String sql = "SELECT count(*) FROM students";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public int getTotalDepartments() throws SQLException {
        String sql = "SELECT count(DISTINCT department) FROM students";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public Map<String, Integer> getDepartmentDistribution() throws SQLException {
        Map<String, Integer> distribution = new HashMap<>();
        String sql = "SELECT department, count(*) as count FROM students GROUP BY department";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                distribution.put(rs.getString("department"), rs.getInt("count"));
            }
        }
        return distribution;
    }

    private void setStudentParams(PreparedStatement pstmt, Student student) throws SQLException {
        pstmt.setString(1, student.getRollNumber());
        pstmt.setString(2, student.getFullName());
        pstmt.setString(3, student.getDob());
        pstmt.setString(4, student.getGender());
        pstmt.setString(5, student.getEmail());
        pstmt.setString(6, student.getPhone());
        pstmt.setString(7, student.getDepartment());
        pstmt.setInt(8, student.getYearOfStudy());
        pstmt.setString(9, student.getAddress());
        pstmt.setDouble(10, student.getCgpa());
    }

    private List<Student> getStudentsByQuery(String sql) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return fetchStudentsFromResultSet(rs);
        }
    }

    private List<Student> fetchStudentsFromResultSet(ResultSet rs) throws SQLException {
        List<Student> students = new ArrayList<>();
        while (rs.next()) {
            students.add(new Student(
                    rs.getInt("id"),
                    rs.getString("roll_number"),
                    rs.getString("full_name"),
                    rs.getString("dob"),
                    rs.getString("gender"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("department"),
                    rs.getInt("year_of_study"),
                    rs.getString("address"),
                    rs.getDouble("cgpa")
            ));
        }
        return students;
    }
}
