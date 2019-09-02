package com.o2o.service.impl;

import com.o2o.dto.PersonInfoExecution;
import com.o2o.enums.PersonInfoStateEnum;
import com.o2o.mapper.PersonInfoMapper;
import com.o2o.pojo.PersonInfo;
import com.o2o.service.PersonInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonInfoServiceImpl implements PersonInfoService {

    @Autowired
    private PersonInfoMapper personInfoMapper;

    @Override
    public PersonInfo getPersonInfoById(Long userId) {
        return personInfoMapper.getPersonInfobById(userId);
    }

    @Override
    @Transactional
    public PersonInfoExecution addPersonInfo(PersonInfo personInfo) {
        if (personInfo == null) {
            return new PersonInfoExecution(PersonInfoStateEnum.EMPTY);
        }
        try {
            int i = personInfoMapper.insertPersonInfo(personInfo);
            if (i > 0){
                personInfo = personInfoMapper.getPersonInfobById(personInfo.getUserId());
                return new PersonInfoExecution(PersonInfoStateEnum.SUCCESS, personInfo);
            } else {
                return new PersonInfoExecution(PersonInfoStateEnum.INNER_ERROR);
            }
        } catch (Exception e) {
            throw new RuntimeException("addPersonInfo error: " + e.getMessage());
        }
    }
}
