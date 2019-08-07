package com.o2o.service;

import com.o2o.dto.ProductExecution;

public interface ProductService  {
    ProductExecution getProductList(long shopId);
}
