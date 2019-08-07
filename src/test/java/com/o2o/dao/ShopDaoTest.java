package com.o2o.dao;

import com.o2o.BaseTest;
import com.o2o.mapper.ShopMapper;
import com.o2o.pojo.Shop;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ShopDaoTest extends BaseTest {
    @Autowired
    private ShopMapper shopMapper;

    @Test
    public void testShopDao(){
        Shop shop = shopMapper.queryShopById(15l);
        System.out.println(shop);
    }
}
