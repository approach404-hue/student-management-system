package com.yujie.studentapi.controller;

import com.yujie.studentapi.common.Result;
import com.yujie.studentapi.entity.Student;
import com.yujie.studentapi.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "学生管理接口")
@RestController
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Operation(summary = "查询所有学生")
    @GetMapping("/students")
    public Result<List<Student>> getStudents() {
        return Result.success(studentService.getStudents());
    }

    @Operation(summary = "根据 ID 查询学生")
    @GetMapping("/students/{id}")
    public Result<Student> getStudentById(@PathVariable Integer id) {
        return Result.success(studentService.getStudentById(id));
    }

    @Operation(summary = "新增学生")
    @PostMapping("/students")
    public Result<Void> addStudent(@RequestBody @Valid Student student) {
        studentService.addStudent(student);
        return Result.success("添加成功");
    }

    @Operation(summary = "修改学生")
    @PutMapping("/students/{id}")
    public Result<Void> updateStudent(@PathVariable Integer id, @RequestBody @Valid Student student) {
        studentService.updateStudent(id, student);
        return Result.success("修改成功");
    }

    @Operation(summary = "删除学生")
    @DeleteMapping("/students/{id}")
    public Result<Void> deleteStudent(@PathVariable Integer id) {
        studentService.deleteStudent(id);
        return Result.success("删除成功");
    }

    @Operation(summary = "分页条件查询学生")
    @GetMapping("/students/page")
    public Result<Page<Student>> pageStudents(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String major) {

        Page<Student> page = studentService.pageStudents(pageNum, pageSize, name, major);

        return Result.success(page);
    }
}