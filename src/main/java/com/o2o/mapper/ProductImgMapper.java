package com.o2o.mapper;

import com.o2o.pojo.ProductImg;

import java.util.List;

public interface ProductImgMapper {

    int batchInsertProductImg(List<ProductImg> productImg);

    List<ProductImg> queryProductImgByProductId(long productId);

    int deleteProductImg(long productId);
}
