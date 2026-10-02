package com.studentdb.web.controller;

import com.studentdb.web.model.Student;
import com.studentdb.web.repository.StudentRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping
    public List<Student> getAllStudents(@RequestParam(required = false) String query) {
        if (query != null && !query.trim().isEmpty()) {
            return studentRepository.searchStudents(query);
        }
        return studentRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> createStudent(@Valid @RequestBody Student student) {
        if (studentRepository.existsByRollNumber(student.getRollNumber())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Roll number already exists"));
        }
        Student saved = studentRepository.save(student);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Integer id, @Valid @RequestBody Student student) {
        return studentRepository.findById(id).map(existing -> {
            if (studentRepository.existsByRollNumberAndIdNot(student.getRollNumber(), id)) {
                return ResponseEntity.badRequest().body(Map.of("error", "Roll number already exists"));
            }
            student.setId(id);
            Student saved = studentRepository.save(student);
            return ResponseEntity.ok((Object) saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Integer id) {
        if (!studentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        studentRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {
        long totalStudents = studentRepository.count();
        List<Object[]> dist = studentRepository.getDepartmentDistribution();
        
        long totalDepartments = dist.size();
        
        Map<String, Long> distribution = dist.stream()
            .collect(Collectors.toMap(
                row -> (String) row[0],
                row -> (Long) row[1]
            ));

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalStudents", totalStudents);
        stats.put("totalDepartments", totalDepartments);
        stats.put("distribution", distribution);
        
        return ResponseEntity.ok(stats);
    }
}
