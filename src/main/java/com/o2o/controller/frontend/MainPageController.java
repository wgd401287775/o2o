package com.o2o.controller.frontend;

import com.o2o.mapper.ShopCategoryMapper;
import com.o2o.pojo.HeadLine;
import com.o2o.pojo.ShopCategory;
import com.o2o.service.HeadLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/frontend")
public class MainPageController {

    @Autowired
    private HeadLineService headLineService;
    @Autowired
    private ShopCategoryMapper shopCategoryMapper;

    @RequestMapping("/getmainpageinfo")
    public Map<String, Object> getMainPageInfo(){
        Map<String, Object> result = new HashMap<>();
        try {
            HeadLine headLine = new HeadLine();
            headLine.setEnableStatus(1);
            List<HeadLine> headLineList = headLineService.getHeadLineList(headLine);
            List<ShopCategory> shopCategoryList = shopCategoryMapper.queryShopCategory(null);
            result.put("success", true);
            result.put("headLineList", headLineList);
            result.put("shopCategoryList", shopCategoryList);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("errorMsg", e.getMessage());
        }
        return result;
    }
}
