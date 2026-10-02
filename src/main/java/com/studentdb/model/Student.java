package com.studentdb.model;

public class Student {
    private int id;
    private String rollNumber;
    private String fullName;
    private String dob;
    private String gender;
    private String email;
    private String phone;
    private String department;
    private int yearOfStudy;
    private String address;
    private double cgpa;

    public Student() {}

    public Student(int id, String rollNumber, String fullName, String dob, String gender, String email,
                   String phone, String department, int yearOfStudy, String address, double cgpa) {
        this.id = id;
        this.rollNumber = rollNumber;
        this.fullName = fullName;
        this.dob = dob;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.yearOfStudy = yearOfStudy;
        this.address = address;
        this.cgpa = cgpa;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

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

    public int getYearOfStudy() { return yearOfStudy; }
    public void setYearOfStudy(int yearOfStudy) { this.yearOfStudy = yearOfStudy; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public double getCgpa() { return cgpa; }
    public void setCgpa(double cgpa) { this.cgpa = cgpa; }
}
