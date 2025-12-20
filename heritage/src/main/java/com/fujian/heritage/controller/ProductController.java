package com.fujian.heritage.controller;

import com.fujian.heritage.common.Result;
import com.fujian.heritage.entity.Product;
import com.fujian.heritage.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    // 获取列表
    @GetMapping("/list")
    public Result<List<Product>> list(@RequestParam(required = false) String category) {
        return Result.success(productService.getProductsByCategory(category));
    }

    // 获取详情
    @GetMapping("/{id}")
    public Result<Product> getById(@PathVariable Long id) {
        return Result.success(productService.getById(id));
    }

    // 新增或更新商品 (核心接口)
    @PostMapping("/save")
    public Result<String> save(@RequestBody Product product) {
        try {
            boolean success = productService.saveProduct(product);
            return success ? Result.success("操作成功") : Result.error("操作失败");
        } catch (Exception e) {
            return Result.error("系统异常：" + e.getMessage());
        }
    }

    // 删除商品
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id) {
        boolean success = productService.removeById(id);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }
}