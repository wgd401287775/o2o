package com.o2o.controller.frontend;

import com.o2o.dto.ShopExecution;
import com.o2o.pojo.Area;
import com.o2o.pojo.Shop;
import com.o2o.pojo.ShopCategory;
import com.o2o.service.AreaService;
import com.o2o.service.ShopCategoryService;
import com.o2o.service.ShopService;
import com.o2o.utils.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/frontend")
public class ShopListController {
    @Autowired
    private AreaService areaService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private ShopService shopService;

    @RequestMapping(value = "/listshopspageinfo", method = RequestMethod.POST)
    public Map<String, Object> listShopsPageInfo(
            @RequestParam(value = "parentId", required = false) Long parentId) {
        Map<String, Object> map = new HashMap<>();
        try {
            List<ShopCategory> shopCategoryList;
            if (parentId != null) {
                ShopCategory shopCategory = new ShopCategory();
                shopCategory.setParentId(parentId);
                shopCategoryList = shopCategoryService.queryShopCategory(shopCategory);
            } else {
                shopCategoryList = shopCategoryService.queryShopCategory(null);
            }
            List<Area> areaList = areaService.queryAreaList();
            map.put("success", true);
            map.put("areaList", areaList);
            map.put("shopCategoryList", shopCategoryList);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success", false);
            map.put("errorMsg", e.getMessage());
        }
        return map;
    }

    @RequestMapping(value = "/listshops", method = RequestMethod.GET)
    public Map<String, Object> listShops(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        if(pageIndex > 0 && pageSize > 0){
            long parentId = HttpServletRequestUtil.getLong(request, "parentId");
            long shopCategoryId = HttpServletRequestUtil.getLong(request,
                    "shopCategoryId");
            long areaId = HttpServletRequestUtil.getLong(request, "areaId");
            String shopName = HttpServletRequestUtil.getString(request,
                    "shopName");
            Shop shopCondition = compactShopCondition4Search(parentId,
                    shopCategoryId, areaId, shopName);
            ShopExecution se = shopService.getShopList(shopCondition, pageIndex, pageSize);
            map.put("success", true);
            map.put("shopList", se.getShopList());
            map.put("count", se.getCount());
        } else {
            map.put("success", false);
            map.put("errorMsg", "empty pageSize or pageIndex");
        }
        return map;
    }

    private Shop compactShopCondition4Search(long parentId, long shopCategoryId, long areaId, String shopName){
        Shop shopCondition = new Shop();
        if (parentId != -1L) {
            ShopCategory parentCategory = new ShopCategory();
            parentCategory.setShopCategoryId(parentId);
            shopCondition.setParentCategory(parentCategory);
        }
        if (shopCategoryId != -1L) {
            ShopCategory shopCategory = new ShopCategory();
            shopCategory.setShopCategoryId(shopCategoryId);
            shopCondition.setShopCategory(shopCategory);
        }
        if (areaId != -1L) {
            Area area = new Area();
            area.setAreaId(areaId);
            shopCondition.setArea(area);
        }

        if (shopName != null) {
            shopCondition.setShopName(shopName);
        }
        shopCondition.setEnableStatus(1);
        return shopCondition;
    }
}
