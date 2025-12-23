package com.fujian.heritage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fujian.heritage.entity.ActivitySignup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface ActivitySignupMapper extends BaseMapper<ActivitySignup> {

    // 关联查询：报名信息 + 用户名 + 活动标题
    @Select("SELECT s.signup_id as signupId, s.status, s.create_time as createTime, " +
            "u.nickname as userName, a.title as activityTitle " +
            "FROM biz_activity_signup s " +
            "LEFT JOIN sys_user u ON s.user_id = u.user_id " +
            "LEFT JOIN biz_activity a ON s.activity_id = a.activity_id " +
            "ORDER BY s.create_time DESC")
    List<Map<String, Object>> selectSignupListWithDetails();

    // 在 heritage/mapper/ActivitySignupMapper.java 中增加
    @Select("SELECT s.signup_id as signupId, s.status, s.create_time as createTime, " +
            "u.nickname as userName, u.username as userAccount " + // 多查一个账号方便核对
            "FROM biz_activity_signup s " +
            "LEFT JOIN sys_user u ON s.user_id = u.user_id " +
            "WHERE s.activity_id = #{activityId} " + // 关键：按活动ID过滤
            "ORDER BY s.create_time DESC")
    List<Map<String, Object>> selectSignupsByActivityId(@Param("activityId") Long activityId);}
