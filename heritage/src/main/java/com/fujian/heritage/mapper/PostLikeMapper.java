package com.fujian.heritage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fujian.heritage.entity.PostLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface PostLikeMapper extends BaseMapper<PostLike> {
    // 辅助查询：查找某个用户点赞过的所有帖子ID
    @Select("SELECT post_id FROM biz_post_like WHERE user_id = #{userId}")
    List<Long> selectPostIdsByUserId(Long userId);
}