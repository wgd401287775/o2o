package com.o2o.service;

import com.o2o.mapper.AreaMapper;
import com.o2o.pojo.Area;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface AreaService {

    List<Area> queryAreaList();
}
