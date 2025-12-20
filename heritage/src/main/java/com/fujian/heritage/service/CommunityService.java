package com.fujian.heritage.service;

import com.fujian.heritage.entity.Activity;
import com.fujian.heritage.entity.Post;
import java.util.List;
import java.util.Map;

public interface CommunityService {
    // 获取帖子列表
    List<Post> getPostList();

    // 获取活动列表
    List<Activity> getActivityList();

    // 报名活动
    boolean signupActivity(Long activityId, Long userId);

    // 保存帖子
    boolean savePost(Post post);

    // 新增：获取我的报名记录 (包含活动详情)
    List<Map<String, Object>> getMySignups(Long userId);
}