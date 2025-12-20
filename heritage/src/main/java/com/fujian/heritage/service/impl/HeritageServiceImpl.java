package com.fujian.heritage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fujian.heritage.entity.Heritage;
import com.fujian.heritage.mapper.HeritageMapper;
import com.fujian.heritage.service.HeritageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HeritageServiceImpl extends ServiceImpl<HeritageMapper, Heritage> implements HeritageService {

    @Autowired
    private HeritageMapper heritageMapper;

    @Override
    public List<Heritage> search(String name, String category) {
        QueryWrapper<Heritage> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1); // 前台只看已发布
        if (StringUtils.hasText(name)) {
            wrapper.like("name", name);
        }
        if (StringUtils.hasText(category) && !"全部".equals(category)) {
            wrapper.eq("category", category);
        }
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<Heritage> getAdminList(String name, String category, Integer status) {
        QueryWrapper<Heritage> wrapper = new QueryWrapper<>();
        if (StringUtils.hasText(name)) {
            wrapper.like("name", name);
        }
        if (StringUtils.hasText(category)) {
            wrapper.eq("category", category);
        }
        if (status != null) {
            wrapper.eq("status", status);
        }
        wrapper.orderByDesc("create_time");
        return baseMapper.selectList(wrapper);
    }

    @Override
    public boolean audit(Long id, Integer status) {
        Heritage h = new Heritage();
        h.setHeritageId(id);
        h.setStatus(status);
        return this.updateById(h);
    }

    @Override
    public Map<String, Object> getDashboardData() {
        Map<String, Object> map = new HashMap<>();
        try {
            map.put("pieData", heritageMapper.countByCategory());
            map.put("barData", heritageMapper.countByRegion());
        } catch (Exception e) {
            // 忽略图表错误，防止影响主流程
        }
        return map;
    }
}