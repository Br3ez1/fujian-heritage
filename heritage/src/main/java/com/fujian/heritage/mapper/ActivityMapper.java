package com.fujian.heritage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fujian.heritage.entity.Activity;
import com.fujian.heritage.entity.ActivitySignup;
import com.fujian.heritage.entity.Post;
import org.apache.ibatis.annotations.Mapper;

// 为了方便复制，我把三个 Mapper 放在这一个代码块说明里
// 请分别创建三个文件：PostMapper.java, ActivityMapper.java, ActivitySignupMapper.java


@Mapper
public interface ActivityMapper extends BaseMapper<Activity> {}

