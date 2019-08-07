package com.o2o.service.impl;

import com.o2o.dto.ProductExecution;
import com.o2o.enums.ProductStateEnum;
import com.o2o.mapper.ProductMapper;
import com.o2o.pojo.Product;
import com.o2o.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;
    @Override
    public ProductExecution getProductList(long shopId) {
        List<Product> productList = productMapper.queryProductList(shopId);
        return new ProductExecution(ProductStateEnum.SUCCESS, productList);
    }
}
