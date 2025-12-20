package com.fujian.heritage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fujian.heritage.entity.Product;
import com.fujian.heritage.mapper.ProductMapper;
import com.fujian.heritage.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Override
    public List<Product> getProductsByCategory(String category) {
        QueryWrapper<Product> query = new QueryWrapper<>();
        if (StringUtils.hasText(category)) {
            query.eq("category", category);
        }
        return baseMapper.selectList(query);
    }

    @Override
    public boolean saveProduct(Product product) {
        // 简单的业务逻辑：如果没有图片，给一个默认图片
        if (!StringUtils.hasText(product.getImgUrl())) {
            // 这里使用一个占位图，实际项目中应实现文件上传
            product.setImgUrl("https://img.zcool.cn/community/01d90d5764ff4f0000018c1b30521b.jpg@1280w_1l_2o_100sh.jpg");
        }
        // MyBatis-Plus 提供的 saveOrUpdate，有ID则更新，无ID则新增
        return this.saveOrUpdate(product);
    }
}