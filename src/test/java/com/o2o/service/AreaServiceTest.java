package com.o2o.service;

import com.o2o.BaseTest;
import com.o2o.pojo.Area;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AreaServiceTest extends BaseTest {

    @Autowired
    private AreaService areaService;

    @Test
    @Ignore
    public void testAreaService() {
        List<Area> areas = areaService.queryAreaList();
        System.out.println(areas);
    }
}
