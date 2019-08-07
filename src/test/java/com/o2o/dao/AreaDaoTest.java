package com.o2o.dao;

import com.o2o.BaseTest;
import com.o2o.mapper.AreaMapper;
import com.o2o.pojo.Area;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AreaDaoTest extends BaseTest {
    @Autowired
    private AreaMapper areaMapper;

    @Test
    public void testArea() {
        List<Area> areas = areaMapper.queryAreaList();
        System.out.println(areas);
    }
}
