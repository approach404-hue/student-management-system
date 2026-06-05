package com.yujie.studentapi.controller;
import com.yujie.studentapi.dto.LoginResponse;
import com.yujie.studentapi.common.Result;
import com.yujie.studentapi.entity.User;
import com.yujie.studentapi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import com.yujie.studentapi.dto.UpdateRoleRequest;
import com.yujie.studentapi.dto.UserResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Result<Void> register(@RequestBody @Valid User user) {
        userService.register(user);
        return Result.success("注册成功");
    }

    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody @Valid User user) {
        LoginResponse loginResponse = userService.login(user);
        return Result.success("登录成功", loginResponse);
    }
    @GetMapping
    public Result<List<UserResponse>> listUsers() {
        List<UserResponse> users = userService.listUsers();
        return Result.success("查询成功", users);
    }

    @PutMapping("/{id}/role")
    public Result<Void> updateUserRole(@PathVariable Integer id,
                                       @RequestBody UpdateRoleRequest request,
                                       HttpServletRequest httpRequest) {
        Integer currentUserId = (Integer) httpRequest.getAttribute("userId");

        userService.updateUserRole(id, request.getRole(), currentUserId);

        return Result.success("角色修改成功");
    }

}