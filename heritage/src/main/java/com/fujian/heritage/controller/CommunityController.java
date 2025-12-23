package com.fujian.heritage.controller;

import com.fujian.heritage.common.Result;
import com.fujian.heritage.entity.*;
import com.fujian.heritage.mapper.*;
import com.fujian.heritage.service.CommunityService;
import com.fujian.heritage.service.RecommendationService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/community")
@CrossOrigin(origins = "*")
public class CommunityController {

    @Autowired
    private CommunityService communityService;
    @Autowired
    private RecommendationService recommendationService; // 注入推荐算法服务

    @Autowired
    private PostMapper postMapper;
    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private ActivitySignupMapper activitySignupMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private PostLikeMapper postLikeMapper; // 注入点赞记录Mapper

    // === 1. 推荐算法接口 (新增) ===
    @GetMapping("/recommend")
    public Result<List<Post>> getRecommendations(@RequestParam Long userId) {
        // 1. 调用算法计算推荐的帖子ID
        List<Long> postIds = recommendationService.recommend(userId);

        if (postIds == null || postIds.isEmpty()) {
            return Result.success(Collections.emptyList());
        }

        // 2. 根据ID查出帖子详情
        List<Post> posts = postMapper.selectBatchIds(postIds);
        return Result.success(posts);
    }

    // === 2. 点赞接口 (修正版：必须记录是谁点的赞) ===
    @PostMapping("/post/like/{postId}")
    public Result<Integer> likePost(@PathVariable Long postId, @RequestParam Long userId) {
        // 1. 检查是否重复点赞
        QueryWrapper<PostLike> query = new QueryWrapper<>();
        query.eq("user_id", userId).eq("post_id", postId);
        if (postLikeMapper.selectCount(query) > 0) {
            return Result.error("您已经点过赞了");
        }

        // 2. 插入点赞记录 (这是算法的数据源！)
        PostLike like = new PostLike();
        like.setUserId(userId);
        like.setPostId(postId);
        like.setCreateTime(java.time.LocalDateTime.now());
        postLikeMapper.insert(like);

        // 3. 更新帖子表显示用的数字
        Post post = postMapper.selectById(postId);
        if (post != null) {
            post.setLikes(post.getLikes() == null ? 1 : post.getLikes() + 1);
            postMapper.updateById(post);
            return Result.success(post.getLikes());
        }
        return Result.error("帖子不存在");
    }

    // === 3. 其他原有接口 (保持不变) ===

    @GetMapping("/post/comments/{postId}")
    public Result<List<Comment>> getComments(@PathVariable Long postId) {
        return Result.success(commentMapper.selectCommentsByPostId(postId));
    }

    @PostMapping("/post/comment/add")
    public Result<String> addComment(@RequestBody Comment comment) {
        if(comment.getUserId() == null || comment.getPostId() == null) return Result.error("参数错误");
        commentMapper.insert(comment);
        return Result.success("评论成功");
    }

    @GetMapping("/posts")
    public Result<List<Post>> listPosts() { return Result.success(communityService.getPostList()); }

    @GetMapping("/activities")
    public Result<List<Activity>> listActivities() { return Result.success(communityService.getActivityList()); }

    @PostMapping("/signup")
    public Result<String> signup(@RequestBody Map<String, Long> params) {
        try {
            communityService.signupActivity(params.get("activityId"), params.get("userId"));
            return Result.success("报名成功");
        } catch (Exception e) { return Result.error(e.getMessage()); }
    }

    @GetMapping("/signup/my")
    public Result<List<Map<String, Object>>> getMySignups(@RequestParam Long userId) {
        return Result.success(communityService.getMySignups(userId));
    }

    @PostMapping("/post/add")
    public Result<String> addPost(@RequestBody Post post) {
        return communityService.savePost(post) ? Result.success("发布成功") : Result.error("发布失败");
    }

    @DeleteMapping("/post/{id}")
    public Result<String> deletePost(@PathVariable Long id) {
        return postMapper.deleteById(id) > 0 ? Result.success("删除成功") : Result.error("删除失败");
    }

    @PostMapping("/activity/add")
    public Result<String> saveActivity(@RequestBody Activity activity) {
        int rows = (activity.getActivityId() != null) ? activityMapper.updateById(activity) : activityMapper.insert(activity);
        return rows > 0 ? Result.success("保存成功") : Result.error("保存失败");
    }

    @DeleteMapping("/activity/{id}")
    public Result<String> deleteActivity(@PathVariable Long id) {
        return activityMapper.deleteById(id) > 0 ? Result.success("删除成功") : Result.error("删除失败");
    }

    @GetMapping("/admin/signup/list")
    public Result listSignupsForAdmin() { return Result.success(activitySignupMapper.selectSignupListWithDetails()); }

    @PostMapping("/admin/signup/audit/{signupId}")
    public Result auditSignup(@PathVariable Long signupId, Integer status) {
        ActivitySignup s = new ActivitySignup(); s.setSignupId(signupId); s.setStatus(status);
        activitySignupMapper.updateById(s); return Result.success();
    }

    @GetMapping("/admin/signup/list/{activityId}")
    public Result listSignupsByActivity(@PathVariable Long activityId) {
        return Result.success(activitySignupMapper.selectSignupsByActivityId(activityId));
    }
}