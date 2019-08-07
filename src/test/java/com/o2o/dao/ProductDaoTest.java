package com.o2o.dao;

import com.o2o.BaseTest;
import com.o2o.mapper.ProductMapper;
import com.o2o.pojo.Product;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ProductDaoTest extends BaseTest {
    @Autowired
    private ProductMapper productMapper;

    @Test
    public void testGetProductList(){
        List<Product> products = productMapper.queryProductList(37l);
        System.out.println(products);
    }
}
