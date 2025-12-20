package com.fujian.heritage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fujian.heritage.entity.Activity;
import com.fujian.heritage.entity.ActivitySignup;
import com.fujian.heritage.entity.Post;
import com.fujian.heritage.mapper.ActivityMapper;
import com.fujian.heritage.mapper.ActivitySignupMapper;
import com.fujian.heritage.mapper.PostMapper;
import com.fujian.heritage.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommunityServiceImpl implements CommunityService {

    @Autowired
    private PostMapper postMapper;
    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private ActivitySignupMapper signupMapper;

    @Override
    public List<Post> getPostList() {
        // 按时间倒序查询
        List<Post> posts = postMapper.selectList(new QueryWrapper<Post>().orderByDesc("create_time"));
        // 模拟填充用户信息
        posts.forEach(p -> {
            p.setAuthor("用户ID:" + p.getUserId());
            p.setAvatar("https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png");
            p.setViews(100 + (int)(Math.random() * 1000));
            p.setComments(5 + (int)(Math.random() * 50));
        });
        return posts;
    }

    @Override
    public List<Activity> getActivityList() {
        return activityMapper.selectList(null);
    }

    @Override
    public boolean signupActivity(Long activityId, Long userId) {
        // 1. 检查是否重复报名
        QueryWrapper<ActivitySignup> query = new QueryWrapper<>();
        query.eq("activity_id", activityId).eq("user_id", userId);
        if (signupMapper.selectCount(query) > 0) {
            throw new RuntimeException("您已报名过该活动，请勿重复提交");
        }

        // 2. 插入报名记录
        ActivitySignup signup = new ActivitySignup();
        signup.setActivityId(activityId);
        signup.setUserId(userId);
        signup.setStatus(1); // 简化逻辑：直接设为报名成功(1)
        signup.setCreateTime(LocalDateTime.now());

        return signupMapper.insert(signup) > 0;
    }

    @Override
    public boolean savePost(Post post) {
        if (post.getCreateTime() == null) {
            post.setCreateTime(LocalDateTime.now());
        }
        if (post.getLikes() == null) {
            post.setLikes(0);
        }
        return postMapper.insert(post) > 0;
    }

    // 实现获取我的报名记录
    @Override
    public List<Map<String, Object>> getMySignups(Long userId) {
        // 1. 查出该用户的所有报名记录
        QueryWrapper<ActivitySignup> query = new QueryWrapper<>();
        query.eq("user_id", userId).orderByDesc("create_time");
        List<ActivitySignup> signups = signupMapper.selectList(query);

        // 2. 组装数据，补充活动标题
        List<Map<String, Object>> result = new ArrayList<>();
        for (ActivitySignup signup : signups) {
            Map<String, Object> map = new HashMap<>();
            map.put("signupId", signup.getSignupId());
            map.put("activityId", signup.getActivityId());
            map.put("createTime", signup.getCreateTime());
            map.put("status", signup.getStatus());

            // 查活动详情
            Activity activity = activityMapper.selectById(signup.getActivityId());
            if (activity != null) {
                map.put("activityTitle", activity.getTitle());
                map.put("activityTime", activity.getActivityTime());
                map.put("location", activity.getLocation());
            } else {
                map.put("activityTitle", "活动已下架");
            }
            result.add(map);
        }
        return result;
    }
}