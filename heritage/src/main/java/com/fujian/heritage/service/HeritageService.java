package com.fujian.heritage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fujian.heritage.entity.Heritage;
import java.util.List;
import java.util.Map;

public interface HeritageService extends IService<Heritage> {
    // 前台搜索 (只查已发布)
    List<Heritage> search(String name, String category);

    // 后台列表 (查所有，支持筛选)
    List<Heritage> getAdminList(String name, String category, Integer status);

    // 审核操作
    boolean audit(Long id, Integer status);

    // 仪表盘数据
    Map<String, Object> getDashboardData();
}