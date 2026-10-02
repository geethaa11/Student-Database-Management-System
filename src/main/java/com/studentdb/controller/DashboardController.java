package com.studentdb.controller;

import com.studentdb.dao.StudentDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.sql.SQLException;
import java.util.Map;

public class DashboardController {

    @FXML
    private Label totalStudentsLabel;
    @FXML
    private Label totalDepartmentsLabel;
    @FXML
    private VBox departmentDistContainer;

    private StudentDAO studentDAO;

    public DashboardController() {
        this.studentDAO = new StudentDAO();
    }

    @FXML
    public void initialize() {
        refreshStatistics();
    }

    public void refreshStatistics() {
        try {
            int total = studentDAO.getTotalStudents();
            int depts = studentDAO.getTotalDepartments();
            totalStudentsLabel.setText(String.valueOf(total));
            totalDepartmentsLabel.setText(String.valueOf(depts));

            departmentDistContainer.getChildren().clear();
            Map<String, Integer> dist = studentDAO.getDepartmentDistribution();
            for (Map.Entry<String, Integer> entry : dist.entrySet()) {
                Label l = new Label(entry.getKey() + ": " + entry.getValue());
                l.getStyleClass().add("dist-label");
                departmentDistContainer.getChildren().add(l);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
