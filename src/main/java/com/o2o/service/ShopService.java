package com.o2o.service;

import com.o2o.dto.ShopExecution;
import com.o2o.pojo.Shop;

import java.io.InputStream;

public interface ShopService {

    ShopExecution insertShop(Shop shop, InputStream shopImg, String fileName) throws  RuntimeException;

    Shop queryShopById(long shopId);

    ShopExecution modifyShop(Shop shop, InputStream shopImg, String fileName) throws  RuntimeException;

    ShopExecution getShopList(Shop shop, int pageIndex, int pageSize);
}
