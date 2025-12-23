package com.fujian.heritage.controller;

import com.fujian.heritage.common.Result;
import com.fujian.heritage.entity.Heritage;
import com.fujian.heritage.service.HeritageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/heritage")
@CrossOrigin(origins = "*")
public class HeritageController {

    @Autowired
    private HeritageService heritageService;

    // 前台列表
    @GetMapping("/list")
    public Result<List<Heritage>> list(@RequestParam(required = false) String name,
                                       @RequestParam(required = false) String category,
                                       @RequestParam(required = false) String region) {
        return Result.success(heritageService.search(name, category, region));
    }

    // 后台管理列表
    @GetMapping("/admin/list")
    public Result<List<Heritage>> adminList(@RequestParam(required = false) String name,
                                            @RequestParam(required = false) String category,
                                            @RequestParam(required = false) Integer status) {
        return Result.success(heritageService.getAdminList(name, category, status));
    }

    // 新增/编辑
    @PostMapping("/save")
    public Result<String> save(@RequestBody Heritage heritage) {
        if (heritage.getHeritageId() == null) {
            heritage.setCreateTime(LocalDateTime.now());
            // 新增默认待审核
            if (heritage.getStatus() == null) heritage.setStatus(0);
        }
        boolean success = heritageService.saveOrUpdate(heritage);
        return success ? Result.success("保存成功") : Result.error("保存失败");
    }

    // 审核
    @PostMapping("/audit/{id}")
    public Result<String> audit(@PathVariable Long id, @RequestParam Integer status) {
        boolean success = heritageService.audit(id, status);
        return success ? Result.success("审核操作成功") : Result.error("操作失败");
    }

    // 删除
    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Long id) {
        boolean success = heritageService.removeById(id);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }

    // 统计
    @GetMapping("/stats")
    public Result<Map<String, Object>> stats() {
        return Result.success(heritageService.getDashboardData());
    }
}