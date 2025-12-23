package com.fujian.heritage.service;

import com.fujian.heritage.entity.User;
import com.fujian.heritage.mapper.PostLikeMapper;
import com.fujian.heritage.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 协同过滤推荐服务
 * 核心算法：基于用户的协同过滤 (User-Based Collaborative Filtering)
 */
@Service
public class RecommendationService {

    @Autowired
    private PostLikeMapper postLikeMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 为指定用户推荐帖子
     * @param currentUserId 当前登录用户ID
     * @return 推荐的帖子ID列表
     */
    public List<Long> recommend(Long currentUserId) {
        System.out.println("========== 开始执行推荐算法 (User: " + currentUserId + ") ==========");

        // 1. 获取当前用户点过赞的帖子列表
        List<Long> currentUserLikes = postLikeMapper.selectPostIdsByUserId(currentUserId);
        System.out.println("步骤1 [当前用户画像]: 用户 " + currentUserId + " 点赞过的帖子ID: " + currentUserLikes);

        if (currentUserLikes.isEmpty()) {
            System.out.println("-> 结束: 当前用户没有点赞记录，无法计算相似度 (冷启动问题)");
            System.out.println("========================================================");
            return Collections.emptyList();
        }

        // 2. 获取所有用户 (注意：实际生产环境中不能全表查询，通常会查活跃用户或限制数量)
        List<User> allUsers = userMapper.selectList(null);

        // 存储相似度结果：Map<用户ID, 相似度分数>
        Map<Long, Double> similarityMap = new HashMap<>();

        System.out.println("步骤2 [寻找相似邻居]: 开始遍历其他用户计算相似度...");

        for (User user : allUsers) {
            Long otherUserId = user.getUserId();

            // 跳过自己
            if (otherUserId.equals(currentUserId)) continue;

            // 获取该用户的点赞列表
            List<Long> otherLikes = postLikeMapper.selectPostIdsByUserId(otherUserId);

            // 如果对方没点过赞，直接跳过
            if (otherLikes.isEmpty()) continue;

            // === 计算 Jaccard 相似系数 ===
            // 公式: (A ∩ B) / (A ∪ B)

            // 计算交集 (共同喜好)
            List<Long> intersection = new ArrayList<>(currentUserLikes);
            intersection.retainAll(otherLikes);

            // 计算并集 (所有涉及的帖子)
            Set<Long> union = new HashSet<>(currentUserLikes);
            union.addAll(otherLikes);

            if (union.size() > 0) {
                double similarity = (double) intersection.size() / union.size();

                // 只有存在相似度 (即有共同点赞) 才记录
                if (similarity > 0) {
                    similarityMap.put(otherUserId, similarity);
                    System.out.println("   >>> 发现相似用户: " + otherUserId +
                            " | 共同点赞: " + intersection +
                            " | 相似度: " + String.format("%.2f", similarity));
                }
            }
        }

        if (similarityMap.isEmpty()) {
            System.out.println("-> 结束: 未找到任何有共同喜好的相似用户");
            System.out.println("========================================================");
            return Collections.emptyList();
        }

        // 3. 选取最相似的 K 个用户 (这里取 Top 5)
        List<Long> topKUsers = similarityMap.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        System.out.println("步骤3 [Top-K 邻居]: 最终选定参考用户集 -> " + topKUsers);

        // 4. 产生推荐内容
        // 逻辑: 把这些相似用户看过、但我没看过的帖子找出来
        Set<Long> recommendPostIds = new HashSet<>();

        for (Long similarUser : topKUsers) {
            List<Long> items = postLikeMapper.selectPostIdsByUserId(similarUser);

            for (Long item : items) {
                // 核心过滤: 必须是我没点过赞的
                if (!currentUserLikes.contains(item)) {
                    // 防止重复添加 (Set自动处理)
                    if (recommendPostIds.add(item)) {
                        System.out.println("   >>> 生成推荐候选: 帖子 " + item + " (来源: 用户 " + similarUser + ")");
                    }
                }
            }
        }

        System.out.println("========== 算法执行完毕，最终推荐列表: " + recommendPostIds + " ==========");

        return new ArrayList<>(recommendPostIds);
    }
}