package com.o2o.service.impl;

import com.o2o.dto.ShopExecution;
import com.o2o.enums.ShopStateEnum;
import com.o2o.mapper.ShopMapper;
import com.o2o.pojo.Shop;
import com.o2o.service.ShopService;
import com.o2o.utils.ImageUtil;
import com.o2o.utils.PageCalculator;
import com.o2o.utils.PathUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopMapper shopMapper;

    @Override
    @Transactional
    public ShopExecution insertShop(Shop shop, InputStream shopImg, String fileName) throws RuntimeException {
        // 判断空值
        if(shop == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP_INFO);
        }
        // 给店铺信息赋初始值
        shop.setCreateTime(new Date());
        shop.setLastEditTime(new Date());
        shop.setEnableStatus(1);
        int result = shopMapper.insertShop(shop);
        if (result > 0) {
            if(shopImg != null) {
                try {
                    // 存储图片
                    addShopImg(shop, shopImg, fileName);
                }catch (Exception e) {
                    throw new RuntimeException("图片上传失败");
                }
                result = shopMapper.updateShop(shop);
                if (result <= 0) {
                    throw new RuntimeException("更新图片地址失败");
                }
            }
        } else {
            throw new RuntimeException("店铺添加失败");
        }
        return new ShopExecution(ShopStateEnum.CHECK, shop);
    }

    @Override
    public Shop queryShopById(long shopId) {
        return shopMapper.queryShopById(shopId);
    }

    @Override
    @Transactional
    public ShopExecution modifyShop(Shop shop, InputStream shopImg, String fileName) throws RuntimeException {
        if(shop == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP_INFO);
        }else{
            try {
                shop.setLastEditTime(new Date());
                if(shopImg != null){
                    Shop tempShop = shopMapper.queryShopById(shop.getShopId());
                    String oldImg = tempShop.getShopImg();
                    if(StringUtils.isNotEmpty(oldImg)){
                        ImageUtil.deleteFileOrPath(tempShop.getShopImg());
                    }
                    addShopImg(shop, shopImg, fileName);
                }
                int effectedNum = shopMapper.updateShop(shop);
                if(effectedNum > 0) {
                    shop = shopMapper.queryShopById(shop.getShopId());
                    return new ShopExecution(ShopStateEnum.SUCCESS, shop);
                } else {
                    return new ShopExecution(ShopStateEnum.INNER_ERROR);
                }
            } catch (Exception e) {
                throw new RuntimeException("modifyShop error ： " + e.getMessage());
            }
        }
    }

    @Override
    public ShopExecution getShopList(Shop shop, int pageIndex, int pageSize) {
        int rowIndex = PageCalculator.calculatorRowIndex(pageIndex, pageSize);
        List<Shop> shopList = shopMapper.getShopList(shop, rowIndex, pageSize);
        int count = shopMapper.getShopCount(shop);
        ShopExecution se = new ShopExecution();
        if (shopList != null) {
            se.setShopList(shopList);
            se.setCount(count);
        } else {
            se.setState(ShopStateEnum.INNER_ERROR.getState());
        }
        return se;
    }

    private void addShopImg(Shop shop, InputStream shopImg, String fileName) {
        // 获取shop图片目录的相对值路径
        String imagePath = PathUtil.getShopImagePath(shop.getShopId());
        String shopImgAddr = ImageUtil.generateThumbnail(shopImg, fileName, imagePath);
        shop.setShopImg(shopImgAddr);
    }


}
