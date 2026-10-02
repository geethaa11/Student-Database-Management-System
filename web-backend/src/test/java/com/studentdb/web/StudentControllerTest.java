package com.studentdb.web;

import com.studentdb.web.model.Student;
import com.studentdb.web.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    public void setup() {
        studentRepository.deleteAll();
    }

    @Test
    public void testCreateStudent() throws Exception {
        String studentJson = "{\"rollNumber\":\"CS01\", \"fullName\":\"John Doe\", \"department\":\"Computer Science\", \"yearOfStudy\":2, \"cgpa\":8.5}";
        
        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("John Doe"));
    }

    @Test
    public void testDuplicateRollNumber() throws Exception {
        Student s = new Student();
        s.setRollNumber("CS01");
        s.setFullName("John");
        s.setDepartment("CS");
        studentRepository.save(s);

        String studentJson = "{\"rollNumber\":\"CS01\", \"fullName\":\"Jane Doe\", \"department\":\"Computer Science\"}";
        
        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Roll number already exists"));
    }
}
