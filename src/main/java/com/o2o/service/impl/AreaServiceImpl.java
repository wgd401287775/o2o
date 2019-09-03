package com.o2o.service.impl;

import com.o2o.cache.JedisUtil;
import com.o2o.mapper.AreaMapper;
import com.o2o.pojo.Area;
import com.o2o.service.AreaService;
import com.o2o.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {
    @Autowired
    private JedisUtil.Strings jedisStrings;
    @Autowired
    private JedisUtil.Keys jedisKeys;

    @Autowired
    private AreaMapper areaMapper;

    private static String AREALISTKEY = "arealist";

    @Override
    public List<Area> queryAreaList() {
        String key = AREALISTKEY;
        List<Area> areaList;
        if (!jedisKeys.exists(key)) {
            areaList = areaMapper.queryAreaList();
            String jsonString = JsonUtils.objectToJson(areaList);
            jedisStrings.set(key, jsonString);
        } else {
            String jsonString = jedisStrings.get(key);
            areaList = JsonUtils.jsonToList(jsonString, Area.class);
        }
        return areaList;
    }
}
