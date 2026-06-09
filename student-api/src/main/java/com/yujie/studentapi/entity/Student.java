package com.yujie.studentapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
@Schema(description = "学生实体")
@TableName("student")
public class Student {

    @Schema(description = "学生ID")
    @TableId(type = IdType.AUTO)
    private Integer id;
    @Schema(description = "姓名")
    @NotBlank(message = "姓名不能为空")
    private String name;

    @Schema(description = "年龄")
    @NotNull(message = "年龄不能为空")
    @Min(value = 1, message = "年龄必须大于 0")
    private Integer age;

    @Schema(description = "专业")
    @NotBlank(message = "专业不能为空")
    private String major;

    private String avatar;
    public Student() {
    }

    public Student(Integer id, String name, Integer age, String major) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.major = major;
    }

    public Student(Integer id, String name, Integer age, String major, String avatar) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.major = major;
        this.avatar = avatar;
    }
    public String getAvatar() {return avatar;}

    public void setAvatar(String avatar) {this.avatar = avatar;}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }
}