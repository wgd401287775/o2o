package com.o2o.mapper;

import com.o2o.pojo.HeadLine;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HeadLineMapper {
    List<HeadLine> getHeadLineList(@Param("headLine") HeadLine headLine);
}
