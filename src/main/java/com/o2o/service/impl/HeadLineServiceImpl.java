package com.o2o.service.impl;

import com.o2o.cache.JedisUtil;
import com.o2o.mapper.HeadLineMapper;
import com.o2o.pojo.HeadLine;
import com.o2o.service.HeadLineService;
import com.o2o.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HeadLineServiceImpl implements HeadLineService {
    @Autowired
    private JedisUtil.Strings jedisStrings;
    @Autowired
    private JedisUtil.Keys jedisKeys;
    @Autowired
    private HeadLineMapper headLineMapper;
    private static String HLLISTKEY = "headlinelist";
    @Override
    public List<HeadLine> getHeadLineList(HeadLine headLine) {
        List<HeadLine> headLineList = null;
        String key = HLLISTKEY;
        if (!jedisKeys.exists(key)) {
            headLineList = headLineMapper.getHeadLineList(headLine);
            String jsonString = JsonUtils.objectToJson(headLineList);
            jedisStrings.set(key, jsonString);
        } else {
            String jsonString = jedisStrings.get(key);
            headLineList = JsonUtils.jsonToList(jsonString, HeadLine.class);
        }
        return headLineList;
    }
}
