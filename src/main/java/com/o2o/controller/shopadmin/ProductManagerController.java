package com.o2o.controller.shopadmin;

import com.o2o.dto.ProductExecution;
import com.o2o.enums.ProductStateEnum;
import com.o2o.pojo.Product;
import com.o2o.pojo.ProductCategory;
import com.o2o.pojo.Shop;
import com.o2o.service.ProductCategoryService;
import com.o2o.service.ProductService;
import com.o2o.utils.CodeUtil;
import com.o2o.utils.HttpServletRequestUtil;
import com.o2o.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
                Product product = new Product();
                product.setShop(shop);
                ProductExecution pe = productService.getProductList(product);
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

    @RequestMapping(value = "/productchangestatus", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> productChangeStatus(HttpServletRequest request){
        Map<String, Object> result = new HashMap<>();
        Shop shop = (Shop) request.getSession().getAttribute("currentShop");
        String productStr = HttpServletRequestUtil.getString(request, "productStr");
        Product product = JsonUtils.jsonToPojo(productStr, Product.class);
        product.setShop(shop);
        ProductExecution pe = productService.modifyProduct(product, null, null);
        if(pe.getState() == ProductStateEnum.SUCCESS.getState()){
            result.put("success", true);
        } else {
            result.put("success", false);
            result.put("errorMsg", "商品上下架失败！");
        }
        return result;
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

    @RequestMapping(value = "/productmodify", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> modifyProduct(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        boolean checkVerifyCode = CodeUtil.checkVerifyCode(request);
        if(!checkVerifyCode) {
            resultMap.put("success", false);
            resultMap.put("errorMsg", "输入的验证码有误！");
            return resultMap;
        }
        String productStr = HttpServletRequestUtil.getString(request, "productStr");
        Product product = JsonUtils.jsonToPojo(productStr, Product.class);
        CommonsMultipartFile productImg = null;
        List<CommonsMultipartFile> detailImgList = new ArrayList<>();
        CommonsMultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if(resolver.isMultipart(request)){
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            productImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("smallImg");
            for (int i = 0; i < 6; i++){
                CommonsMultipartFile detailImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("detailImg" + i);
                if(detailImg != null) {
                    detailImgList.add(detailImg);
                }
            }
        }
        if(product != null && product.getProductId() != null){
            try {
                Shop shop = (Shop) request.getSession().getAttribute("currentShop");
                product.setShop(shop);
                ProductExecution pe = productService.modifyProduct(product, productImg, detailImgList);
                if (pe.getState() == ProductStateEnum.SUCCESS.getState()){
                    resultMap.put("success", true);
                } else {
                    resultMap.put("success", false);
                    resultMap.put("errorMsg", pe.getStateInfo());
                }
            } catch (Exception e){
                resultMap.put("success", false);
                resultMap.put("errorMsg", e.getMessage());
            }
        } else {
            resultMap.put("success", false);
            resultMap.put("errorMsg", "请输入店铺id");
        }
        return resultMap;
    }

    @RequestMapping(value = "/productinsert", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> insertProduct(HttpServletRequest request){
        Map<String, Object> result = new HashMap<>();
        boolean checkVerifyCode = CodeUtil.checkVerifyCode(request);
        if(!checkVerifyCode) {
            result.put("success", false);
            result.put("errorMsg", "输入的验证码有误！");
            return result;
        }
        String productStr = HttpServletRequestUtil.getString(request, "productStr");
        Product product = JsonUtils.jsonToPojo(productStr, Product.class);
        CommonsMultipartFile productImg = null;
        List<CommonsMultipartFile> detailImgList = new ArrayList<>();
        CommonsMultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if(resolver.isMultipart(request)){
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            productImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("smallImg");
            for (int i = 0; i < 6; i++){
                CommonsMultipartFile detailImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("detailImg" + i);
                if(detailImg != null) {
                    detailImgList.add(detailImg);
                }
            }
        } else {
            result.put("success", false);
            result.put("errorMsg", "上传图片不能为空");
            return result;
        }
        if(product != null && productImg != null && detailImgList.size() > 0){
            Shop shop = (Shop) request.getSession().getAttribute("currentShop");
            product.setShop(shop);
            ProductExecution pe = productService.insertProduct(product, productImg, detailImgList);
            if(pe.getState() == ProductStateEnum.SUCCESS.getState()){
                result.put("success", true);
            } else {
                result.put("success", false);
                result.put("errorMsg", pe.getStateInfo());
            }
        } else {
            result.put("success", false);
            result.put("errorMsg", "请输入商品信息");
        }
        return result;
    }

    @RequestMapping("/getproductbyproductid")
    @ResponseBody
    public Map<String, Object> getProductByProductId(@RequestParam long productId){
        Product product = productService.queryProductById(productId);
        List<ProductCategory> productCategoryList = productCategoryService.getProductCategoryList(product.getShop().getShopId());
        Map<String, Object> result = new HashMap<>();
        if(product != null){
            result.put("success", true);
            result.put("product", product);
            result.put("productCategoryList", productCategoryList);
        } else {
            result.put("success", false);
            result.put("errorMsg", "product empty");
        }
        return result;
    }
}
