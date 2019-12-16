package com.o2o.controller.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/shop")
public class ShopAdminController {

    @RequestMapping("/shopoperation")
    public String shopOperation() {
        return "shop/shopoperation";
    }

    @RequestMapping("/shoplist")
    public String shopList(){
        return "shop/shoplist";
    }

    @RequestMapping("/shopmanage")
    public String shopManage(){
        return "shop/shopmanage";
    }

    @RequestMapping("/productcategorymanage")
    public String productCategoryManage() {
        return "shop/productcategorymanage";
    }

    @RequestMapping("/productmanage")
    public String productManage(){
        return "shop/productmanage";
    }

    @RequestMapping("/productedit")
    public String productEdit(){
        return "shop/productedit";
    }

    @RequestMapping("/register")
    public String register(){
        return "shop/register";
    }

    @RequestMapping("/ownerlogin")
    public String ownerlogin(){
        return "shop/ownerlogin";
    }
}
