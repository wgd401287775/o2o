package com.o2o.mapper;

import com.o2o.pojo.Product;

import java.util.List;

public interface ProductMapper {

    /**
     * 获取店铺下的商品列表
     * @param shopId
     * @return
     */
    List<Product> queryProductList(long shopId);
}
