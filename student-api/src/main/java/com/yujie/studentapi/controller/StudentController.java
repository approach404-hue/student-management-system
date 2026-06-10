package com.yujie.studentapi.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yujie.studentapi.common.Result;
import com.yujie.studentapi.dto.StudentExcelDTO;
import com.yujie.studentapi.entity.Student;
import com.yujie.studentapi.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

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

    @Operation(summary = "导出学生 Excel")
    @GetMapping("/students/export")
    public void exportStudents(HttpServletResponse response) throws Exception {
        List<StudentExcelDTO> data = studentService.exportStudents();

        String fileName = URLEncoder.encode("学生信息.xlsx", StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        EasyExcel.write(outputStream, StudentExcelDTO.class)
                .autoCloseStream(false)
                .sheet("学生信息")
                .doWrite(data);

        byte[] bytes = outputStream.toByteArray();

        response.reset();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + fileName);
        response.setHeader("Cache-Control", "no-cache");
        response.setContentLength(bytes.length);

        response.getOutputStream().write(bytes);
        response.getOutputStream().flush();
    }

    @Operation(summary = "导入学生 Excel")
    @PostMapping("/students/import")
    public Result<Integer> importStudents(@RequestParam("file") MultipartFile file) {
        int count = studentService.importStudents(file);
        return Result.success("成功导入 " + count + " 条学生数据", count);
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
}