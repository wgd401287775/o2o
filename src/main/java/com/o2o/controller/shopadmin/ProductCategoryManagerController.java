package com.o2o.controller.shopadmin;

import com.o2o.dto.OResult;
import com.o2o.dto.ProductCategoryExecution;
import com.o2o.enums.ProductCategoryStateEnum;
import com.o2o.pojo.ProductCategory;
import com.o2o.pojo.Shop;
import com.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shop")
public class ProductCategoryManagerController {
    @Autowired
    private ProductCategoryService productCategoryService;

    @RequestMapping(value = "/getproductcategorylist", method = RequestMethod.GET)
    @ResponseBody
    public OResult<List<ProductCategory>> getProductCategoryList(HttpServletRequest request){
        Shop shop = (Shop) request.getSession().getAttribute("currentShop");
        if(shop != null && shop.getShopId() > 0) {
            List<ProductCategory> productCategoryList = productCategoryService.getProductCategoryList(shop.getShopId());
            return new OResult(true, productCategoryList);
        } else {
            ProductCategoryStateEnum ps = ProductCategoryStateEnum.INNER_ERROR;
            return new OResult(false, ps.getState(), ps.getStateInfo());
        }
    }

    @RequestMapping(value = "/addproductcategorys", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addProductCategorys(HttpServletRequest request,
                                                   @RequestBody List<ProductCategory> productCategories){
        Map<String, Object> map = new HashMap<>();
        Shop shop = (Shop) request.getSession().getAttribute("currentShop");
        if(shop == null){
            map.put("success", false);
            map.put("errorMsg", "no shopId");
            return map;
        }
        if(productCategories != null && productCategories.size() > 0){
            try {
                for (ProductCategory pc : productCategories) {
                    pc.setShopId(shop.getShopId());
                    pc.setCreateTime(new Date());
                    pc.setLastEditTime(new Date());
                }
                ProductCategoryExecution pe = productCategoryService.batchInsertProductCategory(productCategories);
                if(pe.getState() == ProductCategoryStateEnum.SUCCESS.getState()){
                    map.put("success", true);
                } else {
                    map.put("success", false);
                    map.put("errorMsg", pe.getStateInfo());
                }
            } catch (Exception e) {
                map.put("success", false);
                map.put("errorMsg", e.getMessage());
            }
        } else {
            map.put("success", false);
            map.put("errorMsg", "请至少输入一个商品类别");
        }
        return map;
    }

    @RequestMapping(value = "/removeproductcategory", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> deleteProductCategory(long productCategoryId, HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        if(productCategoryId > 0){
            Shop shop = (Shop) request.getSession().getAttribute("currentShop");
            if(shop == null){
                map.put("success", false);
                map.put("errorMsg", "no shopId");
                return map;
            }
            try {
                ProductCategoryExecution pe = productCategoryService.deleteProductCategory(shop.getShopId(), productCategoryId);
                if(pe.getState() == ProductCategoryStateEnum.SUCCESS.getState()){
                    map.put("success", true);
                } else {
                    map.put("success", false);
                    map.put("errorMsg", pe.getState());
                }
            } catch (Exception e) {
                map.put("success", false);
                map.put("errorMsg", e.getMessage());
            }
        } else {
            map.put("success", false);
            map.put("errorMsg", "请至少输入一个商品类别");
        }
        return map;
    }
}
