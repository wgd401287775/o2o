package com.o2o.service;

import com.o2o.BaseTest;
import com.o2o.dto.ShopExecution;
import com.o2o.enums.ShopStateEnum;
import com.o2o.pojo.Area;
import com.o2o.pojo.Shop;
import com.o2o.pojo.ShopCategory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;

public class ShopServiceTest extends BaseTest {

    @Autowired
    private  ShopService shopService;

    @Test
    public void insertShopTest() throws Exception{
        Shop shop = new Shop();
        Area area = new Area();
        ShopCategory shopCategory = new ShopCategory();
        area.setAreaId(3L);
        shopCategory.setShopCategoryId(10L);
        shop.setArea(area);
        shop.setOwnerId(10L);
        shop.setShopCategory(shopCategory);
        shop.setShopName("测试的店铺6");
        shop.setShopDesc("test6");
        shop.setShopAddr("test6");
        shop.setPhone("test6");
        shop.setCreateTime(new Date());
        shop.setEnableStatus(ShopStateEnum.CHECK.getState());
        shop.setAdvice("审核中");

        File img = new File("E:/img/timg.jpg");
        InputStream inputStream = new FileInputStream(img);
        ShopExecution shopExecution = shopService.insertShop(shop, inputStream, "timg.jpg");
        System.out.println(shopExecution);
    }
}
