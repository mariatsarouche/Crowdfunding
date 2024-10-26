package com.example.SpirngSecEx.contoller;
import com.example.SpirngSecEx.model.Student;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StudentController {

    private List<Student> students = new ArrayList<>(List.of(
            new Student(1,"nikos", 89),
            new Student(2,"petros",55)
    ));

    @GetMapping("/students")
    public List<Student> getStudents(){
        return students;
    }



    @PostMapping("/students")
    public Student addStudent(@RequestBody Student student){
        students.add(student);
        return student;
    }

}
