package com.yujie.studentapi.dto;

public class UpdateRoleRequest {

    private String role;//前端传输给后端的，json

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}