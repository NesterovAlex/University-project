package com.nesterov.university.controller;

import com.nesterov.university.model.Gender;
import com.nesterov.university.model.Student;
import com.nesterov.university.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService service) {
        this.studentService = service;
    }

    @GetMapping("/students")
    public String getAll(Model model) {
        List<Student> students = studentService.getAll();
        model.addAttribute("students", students);
        return "students";
    }

    @RequestMapping(value = "/students/edit/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Student findById(@PathVariable("id") long id, Model model) {
        Student student = studentService.get(id);
        model.addAttribute("student", student);

        return student;
    }

    @RequestMapping(value = "/update", method = {RequestMethod.PUT, RequestMethod.GET})
    public String update(@ModelAttribute("student") Student student, Model model) {
        model.addAttribute("student", student);
        System.out.println("ID" + student.getId());
        System.out.println("GROUPOID" + student.getGroupId());
        System.out.println("FIRSTNAME" + student.getFirstName());
        System.out.println("LASTNAME" + student.getLastName());
        System.out.println("Address" + student.getAddress());
        System.out.println("Email" + student.getEmail());
        System.out.println("Phone" + student.getPhone());
        System.out.println("Course" + student.getCourse());
        System.out.println("Faculty" + student.getFaculty());
        System.out.println("Gender" + student.getGender());
        System.out.println("Birth date" + student.getBithDate());
        studentService.update(student);
        return "redirect:/students";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String addStudent(@ModelAttribute("student") Student student, Model model) {
        model.addAttribute("student", student);
        studentService.create(student);
        return "redirect:/students";
    }

    @RequestMapping(value = "/students/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model) {
        Student student = studentService.get(id);
        model.addAttribute("student", student);
        studentService.delete(student.getId());
        return "redirect:/students";
    }
}
