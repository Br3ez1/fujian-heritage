package com.fujian.heritage.controller;

import com.fujian.heritage.common.Result;
import com.fujian.heritage.entity.Activity;
import com.fujian.heritage.entity.Post;
import com.fujian.heritage.service.CommunityService;
import com.fujian.heritage.mapper.PostMapper;
import com.fujian.heritage.mapper.ActivityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/community")
@CrossOrigin(origins = "*")
public class CommunityController {

    @Autowired
    private CommunityService communityService;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private ActivityMapper activityMapper;

    // 获取帖子列表
    @GetMapping("/posts")
    public Result<List<Post>> listPosts() {
        return Result.success(communityService.getPostList());
    }

    // 获取研学活动列表
    @GetMapping("/activities")
    public Result<List<Activity>> listActivities() {
        return Result.success(communityService.getActivityList());
    }

    // 报名接口
    @PostMapping("/signup")
    public Result<String> signup(@RequestBody Map<String, Long> params) {
        Long activityId = params.get("activityId");
        Long userId = params.get("userId");

        try {
            communityService.signupActivity(activityId, userId);
            return Result.success("报名成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // 新增：获取我的报名记录
    // GET /community/signup/my?userId=1
    @GetMapping("/signup/my")
    public Result<List<Map<String, Object>>> getMySignups(@RequestParam Long userId) {
        try {
            List<Map<String, Object>> list = communityService.getMySignups(userId);
            return Result.success(list);
        } catch (Exception e) {
            return Result.error("获取失败：" + e.getMessage());
        }
    }

    // 发布帖子接口
    @PostMapping("/post/add")
    public Result<String> addPost(@RequestBody Post post) {
        try {
            boolean success = communityService.savePost(post);
            return success ? Result.success("发布成功") : Result.error("发布失败");
        } catch (Exception e) {
            return Result.error("发布异常：" + e.getMessage());
        }
    }

    // 删除帖子接口
    @DeleteMapping("/post/{id}")
    public Result<String> deletePost(@PathVariable Long id) {
        try {
            int rows = postMapper.deleteById(id);
            if (rows > 0) {
                return Result.success("删除成功");
            } else {
                return Result.error("删除失败，帖子可能不存在");
            }
        } catch (Exception e) {
            return Result.error("系统异常：" + e.getMessage());
        }
    }

    // 发布研学活动接口
    @PostMapping("/activity/add")
    public Result<String> addActivity(@RequestBody Activity activity) {
        try {
            int rows = activityMapper.insert(activity);
            return rows > 0 ? Result.success("活动发布成功") : Result.error("发布失败");
        } catch (Exception e) {
            return Result.error("发布异常：" + e.getMessage());
        }
    }

    // 删除研学活动接口
    @DeleteMapping("/activity/{id}")
    public Result<String> deleteActivity(@PathVariable Long id) {
        try {
            int rows = activityMapper.deleteById(id);
            if (rows > 0) {
                return Result.success("活动删除成功");
            } else {
                return Result.error("删除失败，活动可能不存在");
            }
        } catch (Exception e) {
            return Result.error("系统异常：" + e.getMessage());
        }
    }
}