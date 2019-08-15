package com.o2o.service.impl;

import com.o2o.mapper.ProductImgMapper;
import com.o2o.service.ProductImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductImgServiceImpl implements ProductImgService {

    @Autowired
    private ProductImgMapper productImgMapper;
}
