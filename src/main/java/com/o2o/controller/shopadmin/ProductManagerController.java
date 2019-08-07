package com.o2o.controller.shopadmin;

import com.o2o.dto.ProductExecution;
import com.o2o.pojo.ProductCategory;
import com.o2o.pojo.Shop;
import com.o2o.service.ProductCategoryService;
import com.o2o.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shop")
public class ProductManagerController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCategoryService productCategoryService;

    @RequestMapping("/getproductlist")
    @ResponseBody
    public Map<String, Object> getProductList(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap();
        Shop shop = (Shop) request.getSession().getAttribute("currentShop");
        if(shop != null) {
            try {
                ProductExecution pe = productService.getProductList(shop.getShopId());
                resultMap.put("success", true);
                resultMap.put("productList", pe.getProductList());
            } catch (Exception e) {
                resultMap.put("success", false);
                resultMap.put("errorMsg", e.getMessage());
            }
        } else {
            resultMap.put("success", false);
            resultMap.put("errorMsg", "empty pageSize or pageIndex or shopId");
        }
        return resultMap;
    }

    @RequestMapping("/getproductcategorylistbyshopid")
    @ResponseBody
    public Map<String, Object> getProductInfo(HttpServletRequest request){
        Shop shop = (Shop) request.getSession().getAttribute("currentShop");
        List<ProductCategory> productCategoryList = productCategoryService.getProductCategoryList(shop.getShopId());
        Map resultMap = new HashMap();
        resultMap.put("success", true);
        resultMap.put("productCategoryList", productCategoryList);
        return resultMap;
    }
}
