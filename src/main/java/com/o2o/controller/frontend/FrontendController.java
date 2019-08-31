package com.o2o.controller.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/frontend")
public class FrontendController {

    @RequestMapping("/index")
    public String index(){
        return "frontend/index";
    }

    @RequestMapping("/shoplist")
    public String shoplist(){
        return "frontend/shoplist";
    }

    @RequestMapping("/shopdetail")
    public String shopdetail(){
        return "frontend/shopdetail";
    }
}
