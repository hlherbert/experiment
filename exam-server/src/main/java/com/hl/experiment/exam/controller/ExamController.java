package com.hl.experiment.exam.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 考试控制器
 */
@RestController
public class ExamController {

    private String name;
    private String age;

    /**
     * 获取学生
     * @return 学生
     */
    @GetMapping("/student")
    public String getStudent() {
        return name;
    }

    /**
     * 修改学生
     * @param student 学生
     * @return 学生姓名
     */
    @PutMapping("/student")
    public String editStudent(@RequestBody Student student) {
        name = student.getName();
        return name;
    }

    /**
     * 新增Student
     * @param name this is name
     * @param age this is age
     */
    @PostMapping("/student")
    public void addStudent(@RequestParam("name") String name,
                           @RequestParam("age") String age,
                           @RequestParam() Student stud) {
        this.name = name;
        this.age = age;
    }
}
