package com.o2o.mapper;

import com.o2o.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {

    /**
     * 获取店铺下的商品列表
     * @param product
     * @return
     */
    List<Product> queryProductList(@Param("product") Product product);

    /**
     * 插入商品
     *
     * @param product
     * @return
     */
    int insertProduct(Product product);

    /**
     * 根据商品ID获取商品
     * @param productId
     * @return
     */
    Product queryProductByProductId(long productId);

    /**
     * 商品编辑
     * @param product
     * @return
     */
    int modifyProduct(Product product);
}
