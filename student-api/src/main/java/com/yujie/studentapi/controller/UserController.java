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
import io.jsonwebtoken.Claims;
import org.springframework.data.redis.core.StringRedisTemplate;
import com.yujie.studentapi.utils.JwtUtil;
import com.yujie.studentapi.dto.UserAddDTO;
import java.time.Duration;
import java.util.Date;
import com.yujie.studentapi.dto.ResetPasswordDTO;

import java.util.List;
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final StringRedisTemplate stringRedisTemplate;

    public UserController(UserService userService, StringRedisTemplate stringRedisTemplate) {
        this.userService = userService;
        this.stringRedisTemplate = stringRedisTemplate;
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
    @PostMapping("/logout")
    public Result<Void> logout(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return Result.error(401, "请先登录");
        }

        String token = authorization.substring(7);

        Claims claims = JwtUtil.parseToken(token);

        Date expiration = claims.getExpiration();

        long remainingMillis = expiration.getTime() - System.currentTimeMillis();

        if (remainingMillis > 0) {
            String blacklistKey = "jwt:blacklist:" + token;

            stringRedisTemplate.opsForValue().set(
                    blacklistKey,
                    "1",
                    Duration.ofMillis(remainingMillis)
            );
        }

        return Result.success("退出登录成功");
    }
    @PostMapping
    public Result<Void> addUser(@RequestBody @Valid UserAddDTO request) {
        userService.addUser(request);
        return Result.success("新增用户成功");
    }
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Integer id,
                                   HttpServletRequest httpRequest) {
        Integer currentUserId = (Integer) httpRequest.getAttribute("userId");

        userService.deleteUser(id, currentUserId);

        return Result.success("删除用户成功");
    }
    @PutMapping("/{id}/password")
    public Result<Void> resetPassword(@PathVariable Integer id,
                                      @RequestBody @Valid ResetPasswordDTO request) {
        userService.resetPassword(id, request.getPassword());
        return Result.success("密码重置成功");
    }
}