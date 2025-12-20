package com.fujian.heritage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fujian.heritage.entity.Product;
import java.util.List;

public interface ProductService extends IService<Product> {
    // 根据分类查询
    List<Product> getProductsByCategory(String category);

    // 保存或更新商品
    boolean saveProduct(Product product);
}