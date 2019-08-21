package com.o2o.service;

import com.o2o.dto.ProductExecution;
import com.o2o.pojo.Product;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.List;

public interface ProductService  {
    ProductExecution getProductList(Product product);

    ProductExecution insertProduct(Product product, CommonsMultipartFile productImg, List<CommonsMultipartFile> detailImgList);

    ProductExecution modifyProduct(Product product, CommonsMultipartFile productImg, List<CommonsMultipartFile> detailImgList);

    Product queryProductById(long productId);
}
