package com.o2o.service;

import com.o2o.dto.PersonInfoExecution;
import com.o2o.pojo.PersonInfo;

public interface PersonInfoService {

    PersonInfo getPersonInfoById(Long userId);

    PersonInfoExecution addPersonInfo(PersonInfo personInfo);
}
