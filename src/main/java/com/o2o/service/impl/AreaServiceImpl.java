package com.o2o.service.impl;

import com.o2o.mapper.AreaMapper;
import com.o2o.pojo.Area;
import com.o2o.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaMapper areaMapper;
    @Override
    public List<Area> queryAreaList() {
        return areaMapper.queryAreaList();
    }
}
