package com.o2o.controller.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.o2o.dto.ShopExecution;
import com.o2o.enums.ShopStateEnum;
import com.o2o.pojo.Area;
import com.o2o.pojo.PersonInfo;
import com.o2o.pojo.Shop;
import com.o2o.pojo.ShopCategory;
import com.o2o.service.AreaService;
import com.o2o.service.ShopCategoryService;
import com.o2o.service.ShopService;
import com.o2o.utils.CodeUtil;
import com.o2o.utils.HttpServletRequestUtil;
import com.o2o.utils.JsonUtils;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shop")
public class ShopManagerController {

    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private AreaService areaService;

    @RequestMapping(value = "/registershop", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> registerShop(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        if(!CodeUtil.checkVerifyCode(request)){
            resultMap.put("success", false);
            resultMap.put("errMsg", "验证码错误");
            return resultMap;
        }
        // 1.接受并转化相应的参数，包括店铺信息以及图片信息
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        Shop shop = JsonUtils.jsonToPojo(shopStr, Shop.class);
        // 获取request中的文件
        CommonsMultipartFile shopImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext()); // 图片解析器
        if(commonsMultipartResolver.isMultipart(request)){
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        } else {
            resultMap.put("success", false);
            resultMap.put("errMsg", "上传图片不能为空");
            return resultMap;
        }
        if(shop != null && shopImg != null) {
            shop.setOwnerId(10l); // 为店铺添加ownerId
            try {
                ShopExecution se = shopService.insertShop(shop, shopImg.getInputStream(), shopImg.getOriginalFilename());
                if (se.getState() == ShopStateEnum.CHECK.getState()) {
                    resultMap.put("success", true);
                    return resultMap;
                } else {
                    resultMap.put("success", false);
                    resultMap.put("errMsg", se.getStateInfo());
                    return resultMap;
                }
            } catch (Exception e) {
                resultMap.put("success", false);
                resultMap.put("errMsg", e.getMessage());
                return resultMap;
            }
        } else {
            resultMap.put("success", false);
            resultMap.put("errMsg", "请输入店铺信息");
            return resultMap;
        }
    }

    @RequestMapping(value = "/modifyshop", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> modifyShop(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        if(!CodeUtil.checkVerifyCode(request)){
            resultMap.put("success", false);
            resultMap.put("errMsg", "验证码错误");
            return resultMap;
        }
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        Shop shop = JsonUtils.jsonToPojo(shopStr, Shop.class);
        CommonsMultipartFile shopImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        if(commonsMultipartResolver.isMultipart(request)){
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        }

        if(shop != null && shop.getShopId() != null){
            try {
                ShopExecution se = null;
                if(shopImg != null) {
                    se = shopService.modifyShop(shop, shopImg.getInputStream(), shopImg.getOriginalFilename());
                } else {
                    se = shopService.modifyShop(shop, null, null);
                }
                if(se.getState() == ShopStateEnum.SUCCESS.getState()) {
                    resultMap.put("success", true);
                    return resultMap;
                } else {
                    resultMap.put("success", false);
                    resultMap.put("errMsg", se.getStateInfo());
                    return resultMap;
                }
            } catch (Exception e) {
                resultMap.put("success", false);
                resultMap.put("errMsg", e.getMessage());
                return resultMap;
            }
        }else{
            resultMap.put("success", false);
            resultMap.put("errMsg", "请输入店铺id");
            return resultMap;
        }
    }

    @RequestMapping(value = "/getshopinitinfo", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getShopInitInfo(){
        Map<String, Object> resultMap = new HashMap<>();
        try {
            List<ShopCategory> categoryList = shopCategoryService.queryShopCategory(new ShopCategory());
            List<Area> areaList = areaService.queryAreaList();
            resultMap.put("success", true);
            resultMap.put("categoryList", categoryList);
            resultMap.put("areaList", areaList);
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("errorMsg", e.getMessage());
        }
        return resultMap;
    }

    @RequestMapping(value = "/getshopbyid", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getshopById(HttpServletRequest request) {
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        Map<String, Object> resultMap = new HashMap<>();
        if(shopId > 0) {
            try {
                Shop shop = shopService.queryShopById(shopId);
                List<Area> areaList = areaService.queryAreaList();
                resultMap.put("success", true);
                resultMap.put("shop", shop);
                resultMap.put("areaList", areaList);
            } catch (Exception e) {
                resultMap.put("success", false);
                resultMap.put("errorMsg", e.getMessage());
            }
        } else {
            resultMap.put("success", false);
            resultMap.put("errorMsg", "empty shopId");
        }
        return resultMap;
    }

    @RequestMapping(value = "/getshoplist", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getShopList(HttpServletRequest request){
        PersonInfo user = new PersonInfo();
        user.setUserId(10l);
        user.setName("栋哥");
        request.getSession().setAttribute("user", user);
        user = (PersonInfo) request.getSession().getAttribute("user");
        Map resultMap = new HashMap();
        try {
            Shop shop = new Shop();
            shop.setOwnerId(user.getUserId());
            ShopExecution se = shopService.getShopList(shop, 0, 100);
            resultMap.put("success", true);
            resultMap.put("shopList", se.getShopList());
            resultMap.put("user", user);
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("errorMsg", e.getMessage());
        }
        return resultMap;
    }

    @RequestMapping(value = "/getshopmanageinfo", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getShopManageInfo(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if (shopId <= 0) {
            Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
            if(currentShop == null){
                resultMap.put("redirect", true);
                resultMap.put("url", "/o2o/shop/shoplist");
            }else{
                resultMap.put("redirect", false);
                resultMap.put("shopId", currentShop.getShopId());
            }
        } else {
            Shop shop = new Shop();
            shop.setShopId(shopId);
            request.getSession().setAttribute("currentShop", shop);
            resultMap.put("redirect", false);
        }
        return resultMap;
    }
}
