package com.o2o.service.impl;

import com.o2o.mapper.HeadLineMapper;
import com.o2o.pojo.HeadLine;
import com.o2o.service.HeadLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HeadLineServiceImpl implements HeadLineService {
    @Autowired
    private HeadLineMapper headLineMapper;
    @Override
    public List<HeadLine> getHeadLineList(HeadLine headLine) {
        return headLineMapper.getHeadLineList(headLine);
    }
}
