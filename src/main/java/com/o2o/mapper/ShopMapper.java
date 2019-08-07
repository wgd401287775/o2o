package com.o2o.mapper;

import com.o2o.pojo.Shop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopMapper {
    int insertShop(Shop shop);

    int updateShop(Shop shop);

    Shop queryShopById(long shopId);

    List<Shop> getShopList(@Param("shopCondition") Shop shop, @Param("rowIndex")int rowIndex, @Param("pageSize")int pageSize);

    int getShopCount(@Param("shopCondition") Shop shop);
}
