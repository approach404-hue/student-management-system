package com.yujie.studentapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yujie.studentapi.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}