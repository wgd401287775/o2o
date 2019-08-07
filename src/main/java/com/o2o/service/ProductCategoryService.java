package com.o2o.service;

import com.o2o.dto.ProductCategoryExecution;
import com.o2o.pojo.ProductCategory;

import java.util.List;

public interface ProductCategoryService {

    List<ProductCategory> getProductCategoryList(long shopId);

    ProductCategoryExecution batchInsertProductCategory(List<ProductCategory> productCategoryList);

    ProductCategoryExecution deleteProductCategory(long shopId, long productCategoryId);
}
