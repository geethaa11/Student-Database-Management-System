package com.studentdb.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MainController {

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private VBox sidebar;

    private Parent dashboardView;
    private DashboardController dashboardController;

    private Parent studentsView;
    private StudentController studentController;
    
    private Parent aboutView;

    @FXML
    public void initialize() {
        try {
            FXMLLoader dashboardLoader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
            dashboardView = dashboardLoader.load();
            dashboardController = dashboardLoader.getController();

            FXMLLoader studentsLoader = new FXMLLoader(getClass().getResource("/fxml/students.fxml"));
            studentsView = studentsLoader.load();
            studentController = studentsLoader.getController();

            FXMLLoader aboutLoader = new FXMLLoader(getClass().getResource("/fxml/about.fxml"));
            aboutView = aboutLoader.load();

            // Set dashboard as default
            showDashboard();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showDashboard() {
        dashboardController.refreshStatistics();
        mainBorderPane.setCenter(dashboardView);
    }

    @FXML
    private void showStudents() {
        studentController.refreshTable();
        mainBorderPane.setCenter(studentsView);
    }

    @FXML
    private void showAddStudent() {
        studentController.refreshTable();
        studentController.clearForm();
        mainBorderPane.setCenter(studentsView);
    }

    @FXML
    private void showAbout() {
        mainBorderPane.setCenter(aboutView);
    }
}
