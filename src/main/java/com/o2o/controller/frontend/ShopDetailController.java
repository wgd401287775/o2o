package com.o2o.controller.frontend;

import com.o2o.dto.ProductExecution;
import com.o2o.enums.ProductStateEnum;
import com.o2o.pojo.Product;
import com.o2o.pojo.ProductCategory;
import com.o2o.pojo.Shop;
import com.o2o.service.ProductCategoryService;
import com.o2o.service.ProductService;
import com.o2o.service.ShopService;
import com.o2o.utils.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/frontend")
public class ShopDetailController {
    @Autowired
    private ShopService shopService;
    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private ProductService productService;

    @RequestMapping("/getshoppageinfo")
    public Map<String, Object> getShopPageInfo(HttpServletRequest request){
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        Map<String, Object> map = new HashMap<>();
        if(shopId < 0) {
            map.put("success", false);
            map.put("errorMsg", "empty shopId");
        } else {
            try {
                Shop shop = shopService.queryShopById(shopId);
                List<ProductCategory> productCategoryList = productCategoryService.getProductCategoryList(shopId);
                map.put("success", true);
                map.put("shop", shop);
                map.put("productCategoryList", productCategoryList);
            } catch (Exception e) {
                e.printStackTrace();
                map.put("success", false);
                map.put("errorMsg", e.getMessage());
            }
        }
        return map;
    }

    @RequestMapping("/listproducts")
    public Map<String, Object> listProducts(HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        int pageNum = HttpServletRequestUtil.getInt(request, "pageNum");
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if (pageSize < 0 || pageNum < 0 || shopId < 0) {
            map.put("success", false);
            map.put("errorMsg", "empty pageSize or pageIndex or shopId");
        } else {
            String productName = HttpServletRequestUtil.getString(request, "productName");
            long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
            Product product = compactProductCondition4Search(shopId, productCategoryId, productName);
            ProductExecution pe = productService.getPageProductList(product, pageNum, pageSize);
            if(pe.getState() == ProductStateEnum.SUCCESS.getState()){
                map.put("success", true);
                map.put("productList", pe.getProductList());
                map.put("count", pe.getCount());
            } else {
                map.put("success", false);
                map.put("errorMsg", pe.getStateInfo());
            }
        }
        return map;
    }

    private Product compactProductCondition4Search(long shopId, long productCategoryId, String productName) {
        Product product = new Product();
        if (shopId > 0){
            Shop shop = new Shop();
            shop.setShopId(shopId);
            product.setShop(shop);
        }
        if(productCategoryId > 0){
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductCategoryId(productCategoryId);
            product.setProductCategory(productCategory);
        }
        if (productName != null) {
            product.setProductName(productName);
        }
        product.setEnableStatus(1);
        return product;
    }
}
