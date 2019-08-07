package com.o2o.service.impl;

import com.o2o.dto.ProductCategoryExecution;
import com.o2o.enums.ProductCategoryStateEnum;
import com.o2o.mapper.ProductCategoryMapper;
import com.o2o.pojo.ProductCategory;
import com.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryMapper productCategoryMapper;
    @Override
    public List<ProductCategory> getProductCategoryList(long shopId) {
        return productCategoryMapper.queryProductCategoryList(shopId);
    }

    @Override
    @Transactional
    public ProductCategoryExecution batchInsertProductCategory(List<ProductCategory> productCategoryList) {
        if(productCategoryList != null || productCategoryList.size() >= 0) {
            try {
                int resultNum = productCategoryMapper.batchInsertProductCategory(productCategoryList);
                if(resultNum > 0){
                    return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
                } else {
                    throw new RuntimeException("批量添加商品类别失败");
                }
            } catch (Exception e) {
                throw new RuntimeException("批量添加商品类别异常，" + e.getMessage());
            }
        } else {
            return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
        }
    }

    @Override
    @Transactional
    public ProductCategoryExecution deleteProductCategory(long shopId, long productCategoryId) {
        try {
            int effectNum = productCategoryMapper.deleteProductCategory(shopId, productCategoryId);
            if(effectNum <= 0){
                throw new RuntimeException("删除商品类别失败");
            } else {
                return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
            }
        } catch (Exception e) {
            throw new RuntimeException("删除商品类别失败," + e.getMessage());
        }
    }
}
