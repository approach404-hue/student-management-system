package com.yujie.studentapi.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yujie.studentapi.entity.Student;
import com.yujie.studentapi.exception.BusinessException;
import com.yujie.studentapi.mapper.StudentMapper;
import org.springframework.stereotype.Service;
import com.yujie.studentapi.dto.StudentExcelDTO;
import com.alibaba.excel.EasyExcel;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
    public List<StudentExcelDTO> exportStudents() {
        List<Student> students = studentMapper.selectList(null);

        return students.stream().map(student -> {
            StudentExcelDTO dto = new StudentExcelDTO();
            dto.setId(student.getId());
            dto.setName(student.getName());
            dto.setAge(student.getAge());
            dto.setMajor(student.getMajor());
            dto.setAvatar(student.getAvatar());
            return dto;
        }).toList();
    }
    @Transactional(rollbackFor = Exception.class)
    public int importStudents(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("导入文件不能为空");
        }

        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new BusinessException("文件名不能为空");
        }

        String lowerName = originalFilename.toLowerCase();

        if (!lowerName.endsWith(".xlsx")) {
            throw new BusinessException("只支持导入 .xlsx 文件");
        }

        try {
            List<StudentExcelDTO> excelStudents = EasyExcel.read(
                            file.getInputStream(),
                            StudentExcelDTO.class,
                            null
                    )
                    .sheet()
                    .doReadSync();

            if (excelStudents == null || excelStudents.isEmpty()) {
                throw new BusinessException("Excel 中没有可导入的数据");
            }

            List<Student> students = new ArrayList<>();

            Set<String> excelStudentKeys = new HashSet<>();

            for (int i = 0; i < excelStudents.size(); i++) {
                StudentExcelDTO dto = excelStudents.get(i);

                int rowNumber = i + 2;

                if (dto.getName() == null || dto.getName().trim().isEmpty()) {
                    throw new BusinessException("第 " + rowNumber + " 行：姓名不能为空");
                }

                if (dto.getAge() == null || dto.getAge() <= 0) {
                    throw new BusinessException("第 " + rowNumber + " 行：年龄必须大于 0");
                }

                if (dto.getMajor() == null || dto.getMajor().trim().isEmpty()) {
                    throw new BusinessException("第 " + rowNumber + " 行：专业不能为空");
                }

                String studentName = dto.getName().trim();
                Integer studentAge = dto.getAge();
                String studentMajor = dto.getMajor().trim();

                String key = studentName + "|" + studentAge + "|" + studentMajor;

                if (excelStudentKeys.contains(key)) {
                    throw new BusinessException("第 " + rowNumber + " 行：Excel 中存在重复学生：" + studentName);
                }

                excelStudentKeys.add(key);

                Long count = studentMapper.selectCount(
                        new LambdaQueryWrapper<Student>()
                                .eq(Student::getName, studentName)
                                .eq(Student::getAge, studentAge)
                                .eq(Student::getMajor, studentMajor)
                );

                if (count > 0) {
                    throw new BusinessException("第 " + rowNumber + " 行：学生已存在：" + studentName);
                }

                checkAvatar(dto.getAvatar());

                Student student = new Student();
                student.setName(studentName);
                student.setAge(studentAge);
                student.setMajor(studentMajor);
                student.setAvatar(dto.getAvatar() == null ? "" : dto.getAvatar().trim());

                students.add(student);
            }

            for (Student student : students) {
                studentMapper.insert(student);
            }

            return students.size();
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("Excel 导入失败：" + e.getMessage());
        }
    }
}