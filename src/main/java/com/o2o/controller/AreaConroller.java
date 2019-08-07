package com.o2o.controller;

import com.o2o.pojo.Area;
import com.o2o.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class AreaConroller {

    @Autowired
    private AreaService areaService;

    @RequestMapping(value = "/areaList", method = RequestMethod.GET)
    @ResponseBody
    public List<Area> queryAreaList(){
        return areaService.queryAreaList();
    }
}
