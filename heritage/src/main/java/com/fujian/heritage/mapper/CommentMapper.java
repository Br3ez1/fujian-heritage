package com.fujian.heritage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fujian.heritage.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    // 关联查询：查评论的同时，把发评论人的昵称和头像查出来
    @Select("SELECT c.*, u.nickname, u.avatar " +
            "FROM biz_comment c " +
            "LEFT JOIN sys_user u ON c.user_id = u.user_id " +
            "WHERE c.post_id = #{postId} " +
            "ORDER BY c.create_time ASC")
    List<Comment> selectCommentsByPostId(Long postId);
}