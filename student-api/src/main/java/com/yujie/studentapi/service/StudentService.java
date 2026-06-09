package com.yujie.studentapi.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yujie.studentapi.entity.Student;
import com.yujie.studentapi.exception.BusinessException;
import com.yujie.studentapi.mapper.StudentMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentMapper studentMapper;

    public StudentService(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    public List<Student> getStudents() {
        return studentMapper.selectList(null);
    }

    public Student getStudentById(Integer id) {
        Student student = studentMapper.selectById(id);

        if (student == null) {
            throw new BusinessException("没有找到 id 为 " + id + " 的学生");
        }

        return student;
    }

    public void addStudent(Student student) {
        checkAvatar(student.getAvatar());
        studentMapper.insert(student);
    }

    public void updateStudent(Integer id, Student student) {
        Student oldStudent = studentMapper.selectById(id);

        if (oldStudent == null) {
            throw new BusinessException("修改失败：没有找到 id 为 " + id + " 的学生");
        }

        checkAvatar(student.getAvatar());

        student.setId(id);
        studentMapper.updateById(student);
    }

    public void deleteStudent(Integer id) {
        Student oldStudent = studentMapper.selectById(id);

        if (oldStudent == null) {
            throw new BusinessException("删除失败：没有找到 id 为 " + id + " 的学生");
        }

        studentMapper.deleteById(id);
    }

    public Page<Student> pageStudents(Integer pageNum, Integer pageSize, String name, String major) {
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }

        Page<Student> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();

        if (name != null && !name.trim().isEmpty()) {
            wrapper.like(Student::getName, name);
        }

        if (major != null && !major.trim().isEmpty()) {
            wrapper.like(Student::getMajor, major);
        }

        wrapper.orderByDesc(Student::getId);

        return studentMapper.selectPage(page, wrapper);
    }

    private void checkAvatar(String avatar) {
        if (avatar == null || avatar.trim().isEmpty()) {
            return;
        }

        String trimmedAvatar = avatar.trim();

        if (!trimmedAvatar.startsWith("/uploads/")) {
            throw new BusinessException("头像地址不合法");
        }

        if (trimmedAvatar.contains("..")) {
            throw new BusinessException("头像地址不合法");
        }

        String lowerAvatar = trimmedAvatar.toLowerCase();

        if (!lowerAvatar.endsWith(".jpg")
                && !lowerAvatar.endsWith(".jpeg")
                && !lowerAvatar.endsWith(".png")
                && !lowerAvatar.endsWith(".gif")
                && !lowerAvatar.endsWith(".webp")) {
            throw new BusinessException("头像格式不合法");
        }
    }
}