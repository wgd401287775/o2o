package com.o2o.mapper;

import com.o2o.pojo.PersonInfo;

public interface PersonInfoMapper {

    int insertPersonInfo(PersonInfo personInfo);

    PersonInfo getPersonInfobById(long userId);

}
