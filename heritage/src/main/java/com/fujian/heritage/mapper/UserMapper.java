package com.fujian.heritage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fujian.heritage.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 可以在这里手写复杂 SQL，但基础功能不用写
}
