package com.yujie.studentapi.service;

import com.yujie.studentapi.dto.UserResponse;

import java.util.List;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yujie.studentapi.dto.LoginResponse;
import com.yujie.studentapi.entity.User;
import com.yujie.studentapi.exception.BusinessException;
import com.yujie.studentapi.mapper.UserMapper;
import com.yujie.studentapi.utils.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserMapper userMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public void register(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());

        User oldUser = userMapper.selectOne(wrapper);

        if (oldUser != null) {
            throw new BusinessException("用户名已存在");
        }

        // 注册时：把明文密码加密后再存入数据库
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        user.setRole("USER");

        userMapper.insert(user);
    }

    public LoginResponse login(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());

        User dbUser = userMapper.selectOne(wrapper);

        if (dbUser == null) {
            throw new BusinessException("用户名不存在");
        }

        // 登录时：用 matches 判断明文密码和数据库里的加密密码是否匹配
        if (!passwordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
            throw new BusinessException("密码错误");
        }

        String token = JwtUtil.generateToken(
                dbUser.getId(),
                dbUser.getUsername(),
                dbUser.getRole()
        );

        return new LoginResponse(
                dbUser.getId(),
                dbUser.getUsername(),
                dbUser.getRole(),
                token
        );
    }
    public List<UserResponse> listUsers() {
        List<User> users = userMapper.selectList(null);

        return users.stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getRole()
                ))
                .collect(Collectors.toList());
    }

    public void updateUserRole(Integer id, String role, Integer currentUserId) {
        if (!"ADMIN".equals(role) && !"USER".equals(role)) {
            throw new BusinessException("角色只能是 ADMIN 或 USER");
        }

        User dbUser = userMapper.selectById(id);

        if (dbUser == null) {
            throw new BusinessException("用户不存在");
        }

        if (dbUser.getId().equals(currentUserId)) {
            throw new BusinessException("不能修改自己的角色");
        }

        dbUser.setRole(role);
        userMapper.updateById(dbUser);
    }
}