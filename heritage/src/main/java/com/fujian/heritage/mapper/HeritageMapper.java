package com.fujian.heritage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fujian.heritage.entity.Heritage;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface HeritageMapper extends BaseMapper<Heritage> {

    // 统计各类别数量 (给饼图用)
    // 返回格式如: [{name: '传统技艺', value: 10}, {name: '民俗', value: 5}]
    @Select("SELECT category as name, COUNT(*) as value FROM biz_heritage GROUP BY category")
    List<Map<String, Object>> countByCategory();

    // 统计各地区数量 (给柱状图用)
    // 返回格式如: [{region: '福州市', count: 20}]
    @Select("SELECT region, COUNT(*) as count FROM biz_heritage GROUP BY region")
    List<Map<String, Object>> countByRegion();
}