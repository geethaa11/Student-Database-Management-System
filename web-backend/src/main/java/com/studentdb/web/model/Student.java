package com.studentdb.web.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "students")
public class Student {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "roll_number", unique = true, nullable = false)
    @NotBlank(message = "Roll number is required")
    private String rollNumber;

    @Column(name = "full_name", nullable = false)
    @NotBlank(message = "Full name is required")
    private String fullName;

    @Column(name = "dob")
    private String dob;

    @Column(name = "gender")
    private String gender;

    @Column(name = "email")
    @Email(message = "Invalid email format")
    private String email;

    @Column(name = "phone")
    @Pattern(regexp = "^$|\\d{7,15}", message = "Invalid phone number")
    private String phone;

    @Column(name = "department")
    @NotBlank(message = "Department is required")
    private String department;

    @Column(name = "year_of_study")
    @Min(value = 1, message = "Year must be at least 1")
    @Max(value = 5, message = "Year cannot exceed 5")
    private Integer yearOfStudy;

    @Column(name = "address")
    private String address;

    @Column(name = "cgpa")
    @Min(value = 0, message = "CGPA must be at least 0.0")
    @Max(value = 10, message = "CGPA cannot exceed 10.0")
    private Double cgpa;

    // Getters and Setters

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public Integer getYearOfStudy() { return yearOfStudy; }
    public void setYearOfStudy(Integer yearOfStudy) { this.yearOfStudy = yearOfStudy; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Double getCgpa() { return cgpa; }
    public void setCgpa(Double cgpa) { this.cgpa = cgpa; }
}
